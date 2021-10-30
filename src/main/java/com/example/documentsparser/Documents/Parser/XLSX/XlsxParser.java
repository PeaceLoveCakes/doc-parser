package com.example.documentsparser.Documents.Parser.XLSX;

import com.example.documentsparser.model.DocTemplate;
import com.example.documentsparser.Documents.Parser.DocParser;
import com.example.documentsparser.Documents.Parser.ParsingLogic;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;

@Service
public class XlsxParser implements DocParser {

    @Autowired
    ParsingLogic dps;

    public void initTemplate(DocTemplate docTemplate) {
        try {

            FileInputStream fileIs = new FileInputStream(docTemplate.getSourceFile());

            XSSFWorkbook workbook = new XSSFWorkbook(fileIs);
            XSSFSheet sheet = workbook.cloneSheet(0);


            Iterator<Row> rowIterator = sheet.rowIterator();
            while (rowIterator.hasNext()) {

                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {

                    Cell cell = cellIterator.next();
                    String content = "";

                    try {
                        content = cell.getStringCellValue();
                    } catch (Exception e) {
                    }

                    dps.findTagsInString(docTemplate, content);

                }
            }

            fileIs.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseDocument(DocTemplate docTemplate, File result) {
        try {

            FileInputStream fileIs = new FileInputStream(docTemplate.getSourceFile());

            XSSFWorkbook workbook = new XSSFWorkbook(fileIs);
            XSSFSheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.rowIterator();
            while (rowIterator.hasNext()) {

                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {

                    Cell cell = cellIterator.next();
                    String content = "";

                    try {
                        content = cell.getStringCellValue();
                        cell.setCellValue(dps.parseString(docTemplate, content));
                    } catch (Exception e) {
                    }

                }
            }

            FileOutputStream out = new FileOutputStream(result);
            workbook.write(out);

            fileIs.close();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
