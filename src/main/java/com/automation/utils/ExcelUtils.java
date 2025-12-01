package com.automation.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ExcelUtils.java
 * Utility class for reading Excel files using Apache POI.
 * Supports data-driven testing with TestNG DataProvider.
 * 
 * Features:
 * - Read entire sheets or specific rows
 * - Convert rows to Object[][] for DataProvider
 * - Map-based row access by column headers
 */
public class ExcelUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(ExcelUtils.class);

    /**
     * Read Excel sheet and return as Object[][] for DataProvider
     * @param filePath Path to Excel file
     * @param sheetName Name of sheet to read
     * @return Object[][] suitable for TestNG DataProvider
     */
    public static Object[][] readExcelData(String filePath, String sheetName) {
        List<Object[]> dataList = new ArrayList<>();
        
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet not found: " + sheetName);
            }
            
            int rowCount = sheet.getLastRowNum();
            int colCount = sheet.getRow(0).getLastCellNum();
            
            // Skip header row (row 0)
            for (int i = 1; i <= rowCount; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                
                String[] rowData = new String[colCount];
                for (int j = 0; j < colCount; j++) {
                    Cell cell = row.getCell(j);
                    rowData[j] = getCellValue(cell);
                }
                dataList.add(new Object[]{rowData});
            }
            
            logger.info("✓ Read " + dataList.size() + " rows from sheet: " + sheetName);
            
        } catch (IOException e) {
            logger.error("✗ Error reading Excel file", e);
            throw new RuntimeException("Failed to read Excel file: " + filePath, e);
        }
        
        return dataList.toArray(new Object[0][]);
    }

    /**
     * Read Excel sheet with headers as Map keys
     * @param filePath Path to Excel file
     * @param sheetName Name of sheet to read
     * @return List of Maps with column headers as keys
     */
    public static List<Map<String, String>> readExcelDataAsMap(String filePath, String sheetName) {
        List<Map<String, String>> dataList = new ArrayList<>();
        
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet not found: " + sheetName);
            }
            
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                throw new RuntimeException("Header row not found");
            }
            
            // Extract headers
            List<String> headers = new ArrayList<>();
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                Cell cell = headerRow.getCell(i);
                headers.add(getCellValue(cell));
            }
            
            // Read data rows
            int rowCount = sheet.getLastRowNum();
            for (int i = 1; i <= rowCount; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                
                Map<String, String> rowMap = new HashMap<>();
                for (int j = 0; j < headers.size(); j++) {
                    Cell cell = row.getCell(j);
                    rowMap.put(headers.get(j), getCellValue(cell));
                }
                dataList.add(rowMap);
            }
            
            logger.info("✓ Read " + dataList.size() + " rows from sheet: " + sheetName);
            
        } catch (IOException e) {
            logger.error("✗ Error reading Excel file", e);
            throw new RuntimeException("Failed to read Excel file: " + filePath, e);
        }
        
        return dataList;
    }

    /**
     * Get cell value as String
     * @param cell Excel Cell
     * @return Cell value as String
     */
    private static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf((long)cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
}
