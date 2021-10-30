package com.example.documentsparser.Documents.Parser;


import com.example.documentsparser.model.DocTemplate;

import java.io.File;

public interface DocParser {

    void initTemplate(DocTemplate docTemplate);

    void parseDocument(DocTemplate docTemplate, File output);

}
