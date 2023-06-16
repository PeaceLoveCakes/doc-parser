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
import java.util.stream.Collectors;

public class DocService {

    private final TxtParser txtParser;
    private final XlsxParser xlsxParser;
    private final DocxParser docxParser;

    private final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd-MM-yy HH-mm");

    public DocService() {
        txtParser = new TxtParser();
        xlsxParser = new XlsxParser();
        docxParser = new DocxParser();
    }

    public Set<String> findByRegex(File inputFile, Pattern pattern) {
        DocParser parser = getFileParser(inputFile);
        return parser.findByRegex(inputFile, pattern);
    }

    public Set<String> findByRegex(File inputFile, String pattern) {
        return findByRegex(inputFile, Pattern.compile(pattern));
    }

    public Map<String, Set<String>> findByRegex(File inputFile, Set<Pattern> patterns) {
        Map<String, Set<String>> result = new HashMap<>();
        DocParser parser = getFileParser(inputFile);
        for (Pattern pattern : patterns){
            result.put(pattern.toString(), parser.findByRegex(inputFile, pattern));
        }
        return result;
    }

    public Map<String, Set<String>> findByRegex(File inputFile, String... patterns) {
        Set<Pattern> patternSet = Arrays.stream(patterns)
                .map(Pattern::compile).collect(Collectors.toSet());
        return findByRegex(inputFile, patternSet);
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
