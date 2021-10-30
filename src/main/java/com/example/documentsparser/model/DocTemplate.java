package com.example.documentsparser.model;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class DocTemplate {

    private File sourceFile;
    private String name;
    private Map<String, String> tags_values;
    private Map<String, Template> templates;

    public DocTemplate(String templateName) {
        this.name = templateName;
        tags_values = new HashMap<>();
    }

}
