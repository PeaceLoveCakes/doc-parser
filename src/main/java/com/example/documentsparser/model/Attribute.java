package com.example.documentsparser.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = {"code"})
public class Attribute {

    private String name;
    private String code;
    private AttributeType type;
    private String value;

}
