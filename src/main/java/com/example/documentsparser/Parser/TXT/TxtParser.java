package com.example.documentsparser.Parser.TXT;

import com.example.documentsparser.Parser.DocParser;
import com.example.documentsparser.Parser.ParsingLogic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class TxtParser implements DocParser {

    public Set<String> findByRegex(File inputFile, Pattern pattern) {
        String content = readDoc(inputFile);
        return ParsingLogic.findByRegex(content, pattern);
    }

    public void replaceContent(File inputFile, File outputFile, Map<String, String> replaces) {
        try {
            String content = readDoc(inputFile);

            String output = ParsingLogic.replaceContent(content, replaces);

            FileOutputStream fillDoc = new FileOutputStream(outputFile);
            fillDoc.write(output.getBytes(StandardCharsets.UTF_8));
            fillDoc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readDoc(File file) {
        String content = "";
        try {
            BufferedReader document = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file)));
            for (String line : document.lines().toArray(String[]::new)) {
                content += line + "\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

}
