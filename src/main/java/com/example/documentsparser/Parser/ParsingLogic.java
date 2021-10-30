package com.example.documentsparser.Parser;

import lombok.experimental.UtilityClass;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class ParsingLogic {

    public Set<String> findByRegex(String content, Pattern pattern) {
        Set<String> result = new HashSet<>();
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            String value = content.substring(matcher.start(), matcher.end());
            result.add(value);
        }
        return result;
    }

    public String replaceContent(String content, Map<String, String> replaces) {

        for (String key : replaces.keySet()){
            content = content.replaceAll(key, replaces.get(key));
        }

        return content;
    }

}
