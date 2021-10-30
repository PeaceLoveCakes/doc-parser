package com.example.documentsparser.Documents.Parser;

import com.example.documentsparser.model.Attribute;
import com.example.documentsparser.model.DocTemplate;
import com.example.documentsparser.model.Template;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class ParsingLogic {

    private final String LETTERS = "A-Za-zА-Яа-я_ё\\-";
    private final Pattern TAG_PATTERN = Pattern.compile(String.format("<![%s][%s\\s]+>", LETTERS, LETTERS));
    private final Pattern TEMPLATE_PATTERN = Pattern.compile(String.format("<[%s]+\\.[%s]+>", LETTERS, LETTERS));

    public void findTagsInString(DocTemplate docTemplate, String content) {
        if (content == null || content.isEmpty()) return;

        docTemplate.getTags_values().putAll(extractTags(content));

        docTemplate.setTemplates(extractTemplates(content, docTemplate.getTemplates()));
    }

    private Map<String, String> extractTags(String content) {
        Map<String, String> result = new HashMap<>();
        Matcher matcher = TAG_PATTERN.matcher(content);
        while (matcher.find()) {
            String tag = content.substring(matcher.start() + 2, matcher.end() - 1);
            result.put(tag.strip(), null);
        }
        return result;
    }

    private Map<String, Template> extractTemplates(String content, Map<String, Template> templates) {

        Matcher matcher = TEMPLATE_PATTERN.matcher(content);

        while (matcher.find()) {
            String temp = content.substring(matcher.start() + 1, matcher.end() - 1);
            String tKey = temp.replaceAll("[.*\\.]", "");
            String aCode = temp.replaceAll("[\\..*]", "");
            Template template;
            Attribute attribute = new Attribute().setCode(aCode);
            if (templates.containsKey(tKey)) {
                template = templates.get(tKey);
                if (!template.getAttributes().contains(attribute)) {
                    template.addAttribute(attribute);
                }
            } else template = new Template().addAttribute(attribute);
            templates.put(tKey, template);
        }
        return templates;
    }

    public String parseString(DocTemplate docTemplate, String string) {

        string = parseStringByTags(string, docTemplate.getTags_values());
        string = parseStringByTemplates(string, docTemplate.getTemplates());

        return string;
    }

    public String parseStringByTags(String input, Map<String, String> tags_values) {
        String result = input;

        for (String key : tags_values.keySet()) {
            result = result.replaceAll("<!" + key + ">",
                    tags_values.get(key).isEmpty() ? "" : tags_values.get(key));
        }

        return result;
    }

    public String parseStringByTemplates(String input, Map<String, Template> templates) {
        String result = input;

        for (String key : templates.keySet()) {
            for (Attribute attribute : templates.get(key).getAttributes()) {
                if (!(attribute.getValue() == null || attribute.getValue().isEmpty())) {
                    result = result.replaceAll(
                            String.format("<%s.%s>", key, attribute.getCode()),
                            attribute.getValue());
                }
            }
        }
        return result;
    }

}
