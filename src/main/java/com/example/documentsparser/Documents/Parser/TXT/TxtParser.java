package com.example.documentsparser.Documents.Parser.TXT;

import com.example.documentsparser.Documents.DocService;
import com.example.documentsparser.model.DocTemplate;
import com.example.documentsparser.Documents.Parser.DocParser;
import com.example.documentsparser.Documents.Parser.ParsingLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Service
public class TxtParser implements DocParser {

    @Autowired
    ParsingLogic parsingLogic;

    public void initTemplate(DocTemplate docTemplate) {
        //read doc
        String content = readDoc(docTemplate.getName());

        parsingLogic.findTagsInString(docTemplate, content);

    }

    public void parseDocument(DocTemplate docTemplate, File result) {
        try {
            //read doc
            String content = readDoc(docTemplate.getName());

            //parse doc
            String output = parsingLogic.parseString(docTemplate, content);

            //result
            FileOutputStream fillDoc = new FileOutputStream(result);
            fillDoc.write(output.getBytes(StandardCharsets.UTF_8));
            fillDoc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readDoc(String templateName) {
        String content = "";
        try {
            BufferedReader document = new BufferedReader(new InputStreamReader(
                    new FileInputStream(DocService.TEMPLATES_FOLDER.getAbsolutePath() + "/" + templateName)));
            for (String line : document.lines().toArray(String[]::new)) {
                content += line + "\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

}
