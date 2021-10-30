package com.example.documentsparser.Documents.Parser.DOCX;

import com.example.documentsparser.model.DocTemplate;
import com.example.documentsparser.Documents.Parser.DocParser;
import com.example.documentsparser.Documents.Parser.ParsingLogic;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

@Service
public class DocxParser implements DocParser {

    @Autowired
    ParsingLogic parsingLogic;

    public void initTemplate(DocTemplate docTemplate) {
        try {

            FileInputStream fis = new FileInputStream(docTemplate.getSourceFile());
            XWPFDocument document = new XWPFDocument(fis);
            Iterator paragraphs = document.getParagraphsIterator();
            Iterator tables = document.getTablesIterator();

            while (paragraphs.hasNext()) {
                XWPFParagraph paragraph = (XWPFParagraph) paragraphs.next();
                parsingLogic.findTagsInString(docTemplate, paragraph.getText());
            }
            while (tables.hasNext()) {
                XWPFTable table = (XWPFTable) tables.next();
                Iterator<XWPFTableRow> rows = table.getRows().iterator();
                while (rows.hasNext()) {
                    XWPFTableRow row = rows.next();
                    Iterator<XWPFTableCell> cells = row.getTableCells().iterator();
                    while (cells.hasNext()) {
                        parsingLogic.findTagsInString(docTemplate, cells.next().getText());
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void parseDocument(DocTemplate docTemplate, File output) {
        try {
            FileInputStream fis = new FileInputStream(docTemplate.getSourceFile());
            XWPFDocument document = new XWPFDocument(fis);
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            Iterator tables = document.getTablesIterator();

            parseParagraphs(docTemplate, paragraphs);

            while (tables.hasNext()) {
                XWPFTable table = (XWPFTable) tables.next();
                Iterator<XWPFTableRow> rows = table.getRows().iterator();
                while (rows.hasNext()) {
                    XWPFTableRow row = rows.next();
                    List<XWPFTableCell> cells = row.getTableCells();
                    for (XWPFTableCell cell : cells) {
                        List<XWPFParagraph> cellParagraphs = cell.getParagraphs();
                        parseParagraphs(docTemplate, cellParagraphs);
                    }
                }
            }

            FileOutputStream fos = new FileOutputStream(output);
            document.write(fos);
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseParagraphs(DocTemplate docTemplate, List<XWPFParagraph> paragraphs) {
        for (XWPFParagraph paragraph : paragraphs) {
            String text = parsingLogic
                    .parseString(
                            docTemplate,
                            paragraph.getText());
            int size = paragraph.getRuns().size();
            for (int i = 1; i < size; i++) {
                paragraph.removeRun(1);
            }
            XWPFRun run = paragraph.getRuns().get(0);
            run.setText(text, 0);
        }
    }


//    TEXTBOXES
//    public void wordReplaceTextInTextBox(XWPFDocument document, String tag, String value) throws XmlException {
//        for (XWPFParagraph paragraph : document.getParagraphs()) {
//            XmlCursor cursor = paragraph.getCTP().newCursor();
//            cursor.selectPath("declare namespace w='http://schemas.openxmlformats.org/wordprocessingml/2006/main' .//*/w:txbxContent/w:p/w:r");
//
//            List<XmlObject> textBoxes = new ArrayList<XmlObject>();
//
//            while (cursor.hasNextSelection()) {
//                cursor.toNextSelection();
//                XmlObject textbox = cursor.getObject();
//                textBoxes.add(textbox);
//            }
//            for (XmlObject obj : textBoxes) {
//                CTR ctr = CTR.Factory.parse(obj.xmlText());
//
//                XWPFRun run = new XWPFRun(ctr, (IRunBody) paragraph);
//                String text = run.text();
//                if (text != null && text.contains(value)) {
//                    text = text.replace(tag, value);
//                    run.setText(text, 0);
//                }
//                obj.set(run.getCTR());
//            }
//        }
//    }
}
