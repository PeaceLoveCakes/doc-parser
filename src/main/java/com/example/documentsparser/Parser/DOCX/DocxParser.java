package com.example.documentsparser.Parser.DOCX;

import com.example.documentsparser.Parser.DocParser;
import com.example.documentsparser.Parser.ParsingLogic;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;


public class DocxParser implements DocParser {

    public Set<String> findByRegex(File inputFile, Pattern pattern) {
        Set<String> result = new HashSet<>();
        try {

            FileInputStream fis = new FileInputStream(inputFile);
            XWPFDocument document = new XWPFDocument(fis);
            Iterator paragraphs = document.getParagraphsIterator();
            Iterator tables = document.getTablesIterator();

            while (paragraphs.hasNext()) {
                XWPFParagraph paragraph = (XWPFParagraph) paragraphs.next();
                ParsingLogic.findByRegex(paragraph.getText(), pattern);
            }
            while (tables.hasNext()) {
                XWPFTable table = (XWPFTable) tables.next();
                Iterator<XWPFTableRow> rows = table.getRows().iterator();
                while (rows.hasNext()) {
                    XWPFTableRow row = rows.next();
                    Iterator<XWPFTableCell> cells = row.getTableCells().iterator();
                    while (cells.hasNext()) {
                        result.addAll(ParsingLogic.findByRegex(cells.next().getText(), pattern));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public void replaceContent(File inputFile, File outputFile, Map<String, String> replaces) {
        try {
            FileInputStream fis = new FileInputStream(inputFile);
            XWPFDocument document = new XWPFDocument(fis);
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            Iterator tables = document.getTablesIterator();

            parseParagraphs(paragraphs, replaces);

            while (tables.hasNext()) {
                XWPFTable table = (XWPFTable) tables.next();
                Iterator<XWPFTableRow> rows = table.getRows().iterator();
                while (rows.hasNext()) {
                    XWPFTableRow row = rows.next();
                    List<XWPFTableCell> cells = row.getTableCells();
                    for (XWPFTableCell cell : cells) {
                        List<XWPFParagraph> cellParagraphs = cell.getParagraphs();
                        parseParagraphs(cellParagraphs, replaces);
                    }
                }
            }

            FileOutputStream fos = new FileOutputStream(outputFile);
            document.write(fos);
            fis.close();
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseParagraphs(List<XWPFParagraph> paragraphs, Map<String, String> replaces) {
        for (XWPFParagraph paragraph : paragraphs) {
            String text = ParsingLogic.replaceContent(paragraph.getText(), replaces);
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
//        for (XWPFTextBox box : doc.getTextBoxes()) {
//            for (XWPFParagraph para : box.getParagraphs()) {
//                for (XWPFRun run : para.getRuns()) {
//                    String text = run.getText(0);
//                    if (text != null) {
//                        // Loop through each key in the replacements map
//                        for (String key : replacements.keySet()) {
//                            if (text.contains(key)) {
//                                // Replace the key with the corresponding value
//                                text = text.replace(key, replacements.get(key));
//                                run.setText(text, 0);
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
}
