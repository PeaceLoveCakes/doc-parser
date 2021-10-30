package com.example.documentsparser.Parser.XLSX;

import com.example.documentsparser.Parser.DocParser;
import com.example.documentsparser.Parser.ParsingLogic;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class XlsxParser implements DocParser {

    public Set<String> findByRegex(File inputFile, Pattern pattern) {
        Set<String> result = new HashSet<>();
        try {
            FileInputStream fis = new FileInputStream(inputFile);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                XSSFSheet sheet = workbook.getSheetAt(i);
                Iterator<Row> rowIterator = sheet.rowIterator();

                while (rowIterator.hasNext()) {

                    Row row = rowIterator.next();
                    Iterator<Cell> cellIterator = row.cellIterator();
                    while (cellIterator.hasNext()) {

                        Cell cell = cellIterator.next();
                        String content = "";
                        if (cell.getCellType() == CellType.STRING) {
                            content = cell.getStringCellValue();
                            result.addAll(ParsingLogic.findByRegex(content, pattern));
                        }
                    }
                }
            }
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void replaceContent(File inputFile, File outputFile, Map<String, String> replaces) {
        try {

            FileInputStream fis = new FileInputStream(inputFile);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
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
                        cell.setCellValue(ParsingLogic.replaceContent(content, replaces));
                    } catch (Exception e) {
                    }

                }
            }

            FileOutputStream fos = new FileOutputStream(outputFile);
            workbook.write(fos);

            fis.close();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
