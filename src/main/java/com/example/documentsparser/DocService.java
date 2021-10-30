package com.example.documentsparser;

import com.example.documentsparser.Parser.DOCX.DocxParser;
import com.example.documentsparser.Parser.DocParser;
import com.example.documentsparser.Parser.TXT.TxtParser;
import com.example.documentsparser.Parser.XLSX.XlsxParser;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class DocService {

//    private final File ROOT_DIR = new File(System.getProperty("user.dir") + "/documents");
//    private final File TMP_DIR;

    private final TxtParser txtParser;
    private final XlsxParser xlsxParser;
    private final DocxParser docxParser;

    private final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd-MM-yy HH-mm");

    public DocService() {
        txtParser = new TxtParser();
        xlsxParser = new XlsxParser();
        docxParser = new DocxParser();
    }

    public Map<String, Set<String>> findByRegex(File inputFile, Set<String> patterns) {
        Map<String, Set<String>> result = new HashMap<>();
        DocParser parser = getFileParser(inputFile);
        for (String pattern : patterns){
            result.put(pattern, parser.findByRegex(inputFile, Pattern.compile(pattern)));
        }
        return result;
    }

    public Set<String> findByRegex(File inputFile, String pattern) {
        DocParser parser = getFileParser(inputFile);
        return parser.findByRegex(inputFile, Pattern.compile(pattern));
    }

    public void replaceContent(File inputFile, File outputFile, Map<String, String> replaces) {
        DocParser parser = getFileParser(inputFile);
        parser.replaceContent(inputFile, outputFile, replaces);
    }

    private String getFileType(File file) {
        if (file.isDirectory()) {
            return "directory";
        }

        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }

        return name.substring(lastIndexOf + 1);
    }

    private DocParser getFileParser(File file) {
        String fileType = getFileType(file);

        switch (fileType) {
            case "txt":
                return txtParser;
            case "xlsx":
                return xlsxParser;
            case "docx":
                return docxParser;
            default:
                throw new IllegalArgumentException("Unsupported file type: " + fileType);
        }
    }

    private String outputFileName(File file){
        List<String> name = Arrays.asList(file.getName().split("\\."));
        return name.get(0).trim() + " от " + LocalDateTime.now().format(dateTimeFormat) + "." + name.get(name.size()-1);
    }
}

//
//    private final TxtParser txtParser = new TxtParser();
//    private final XlsxParser xlsxParser = new XlsxParser();
//    private final DocxParser docxParser = new DocxParser();
//
//    @Getter private final File DOCS_DIR;
//    @Getter private final File TEMPLATES_FOLDER;
//    @Getter private final File OUTPUT_FOLDER;
//
//    public DocService(String rootDir) {
//        DOCS_DIR = new File(rootDir);
//        TEMPLATES_FOLDER = new File(DOCS_DIR + "/templates");
//        OUTPUT_FOLDER = new File(DOCS_DIR + "/output");
//        DOCS_DIR.mkdir();
//        TEMPLATES_FOLDER.mkdir();
//        OUTPUT_FOLDER.mkdir();
//    }
//
//    public DpsDocTemplate getTemplate(String templateName) {
//        DpsDocTemplate dpsDocTemplate = new DpsDocTemplate(templateName);
//        dpsDocTemplate.setSourceFile(getTemplateFile(templateName));
//        readTemplate(dpsDocTemplate);
//        return dpsDocTemplate;
//    }
//
//    public void parseDocument(DpsDocTemplate dpsDocTemplate, String resultName) {
//        dpsDocTemplate.setSourceFile(getTemplateFile(dpsDocTemplate.getName()));
//        DocParser parser = getFileParser(dpsDocTemplate.getSourceFile());
//
//        parser.parseDocument(dpsDocTemplate,
//                createOutputFile(resultName + getFileType(dpsDocTemplate.getSourceFile())));
//    }
//
//    private void readTemplate(DpsDocTemplate dpsDocTemplate) {
//        DocParser parser = getFileParser(dpsDocTemplate.getSourceFile());
//        if (parser != null) parser.initTemplate(dpsDocTemplate);
//    }
//
//    private File getTemplateFile(String templateName) {
//        return new File(TEMPLATES_FOLDER + "/" + templateName);
//    }
//
//    private File createOutputFile(String fileName) {
//        File output = new File(OUTPUT_FOLDER + "/" + fileName);
//        try {
//            output.createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return output;
//    }
//
//    private String getFileType(File file) {
//        if (file.isDirectory()) return "directory";
//
//        if (file.getName().contains(".xlsx")) {
//            return ".xlsx";
//        } else if (file.getName().contains(".txt")) {
//            return ".txt";
//        } else if (file.getName().contains(".docx")) {
//            return ".docx";
//        }
//
//        return "";
//    }
//
//    private DocParser getFileParser(File file) {
//        if (file.isDirectory()) return null;
//
//        if (file.getName().contains(".xlsx")) {
//            return xlsxParser;
//        } else if (file.getName().contains(".txt")) {
//            return txtParser;
//        } else if (file.getName().contains(".docx")) {
//            return docxParser;
//        }
//
//        return null;
//    }
