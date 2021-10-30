package com.example.documentsparser.Parser;


import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public interface DocParser {

    Set<String> findByRegex(File inputFile, Pattern pattern);

    void replaceContent(File inputFile, File outputFile, Map<String, String> replaces);

}
