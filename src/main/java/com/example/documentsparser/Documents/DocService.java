package com.example.documentsparser.Documents;

import com.example.documentsparser.Documents.Parser.DOCX.DocxParser;
import com.example.documentsparser.Documents.Parser.DocParser;
import com.example.documentsparser.Documents.Parser.TXT.TxtParser;
import com.example.documentsparser.Documents.Parser.XLSX.XlsxParser;
import com.example.documentsparser.model.DocTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class DocService {

    @Autowired
    private TxtParser txtParser;
    @Autowired
    private XlsxParser xlsxParser;
    @Autowired
    private DocxParser docxParser;

    public static final File DOCS_DIR = new File(System.getProperty("user.dir") );
    public static final File TEMPLATES_FOLDER = new File(DOCS_DIR + "/Templates");
    public static final File OUTPUT_FOLDER = new File(DOCS_DIR + "/Output");

    private DocService() {
        DOCS_DIR.mkdir();
        TEMPLATES_FOLDER.mkdir();
        OUTPUT_FOLDER.mkdir();
    }

    public DocTemplate getTemplate(String templateName) {
        DocTemplate docTemplate = new DocTemplate(templateName);
        docTemplate.setSourceFile(getTemplateFile(templateName));
        readTemplate(docTemplate);
        return docTemplate;
    }

    public void parseDocument(DocTemplate docTemplate, String resultName) {
        docTemplate.setSourceFile(getTemplateFile(docTemplate.getName()));
        DocParser parser = getFileParser(docTemplate.getSourceFile());

        parser.parseDocument(docTemplate,
                createOutputFile(resultName + getFileType(docTemplate.getSourceFile())));
    }

    private void readTemplate(DocTemplate docTemplate) {
        DocParser parser = getFileParser(docTemplate.getSourceFile());
        if (parser != null) parser.initTemplate(docTemplate);
    }

    private File getTemplateFile(String templateName) {
        return new File(TEMPLATES_FOLDER + "/" + templateName);
    }

    private File createOutputFile(String fileName) {
        File output = new File(OUTPUT_FOLDER + "/" + fileName);
        try {
            output.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }

    private String getFileType(File file) {
        if (file.isDirectory()) return "directory";

        if (file.getName().contains(".xlsx")) {
            return ".xlsx";
        } else if (file.getName().contains(".txt")) {
            return ".txt";
        } else if (file.getName().contains(".docx")) {
            return ".docx";
        }

        return "";
    }

    private DocParser getFileParser(File file) {
        if (file.isDirectory()) return null;

        if (file.getName().contains(".xlsx")) {
            return xlsxParser;
        } else if (file.getName().contains(".txt")) {
            return txtParser;
        } else if (file.getName().contains(".docx")) {
            return docxParser;
        }

        return null;
    }

}
