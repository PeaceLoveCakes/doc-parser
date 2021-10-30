package com.example.documentsparser.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Template {

    private String name;
    private String description;
    private List<Attribute> attributes;

    public Template addAttribute(Attribute attribute) {
        attributes.add(attribute);
        return this;
    }
}
