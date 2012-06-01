package org.freespace.testingplatform.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;


import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

@Component
public class ExcelReader {

    private static TestResourceLoader resourceLoader;

    @Autowired
    public void setResourceLoader(TestResourceLoader resourceLoader) {
        ExcelReader.resourceLoader = resourceLoader;
    }

    public static String[][] readXLSFile(String xlFilePath, String sheetName, String tableName) {
        String[][] tabArray = null;
        try {
            Workbook workbook = loadXlsFile(xlFilePath);
            Sheet sheet = workbook.getSheet(sheetName);
            int startRow, startCol, endRow, endCol, ci, cj;
            Cell tableStart = sheet.findCell(tableName);
            startRow = tableStart.getRow();
            startCol = tableStart.getColumn();

            Cell tableEnd = sheet.findCell(tableName, startCol + 1, startRow + 1, 100, 64000, false);

            endRow = tableEnd.getRow();
            endCol = tableEnd.getColumn();
            System.out.println("startRow=" + startRow + ", endRow=" + endRow + ", " +
                    "startCol=" + startCol + ", endCol=" + endCol);
            tabArray = new String[endRow - startRow - 1][endCol - startCol - 1];
            ci = 0;

            for (int i = startRow + 1; i < endRow; i++, ci++) {
                cj = 0;
                for (int j = startCol + 1; j < endCol; j++, cj++) {
                    tabArray[ci][cj] = sheet.getCell(j, i).getContents();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (tabArray);
    }

    public static Object[][] readXLSFileAsBeans(String xlsFilePath, String sheetName, Class<?> beanClazz,
                                                boolean horizontal) {
        List<?> beans = readBeanList(xlsFilePath, sheetName, beanClazz, horizontal);
        Object[][] result = new Object[beans.size()][];
        int i = 0;
        for (Object bean : beans) {
            result[i] = new Object[]{bean};
            ++i;
        }
        return result;
    }

    public static <T> List<T> readBeanList(String xlsFilePath, String sheetName, Class<T> beanClazz,
                                           boolean horizontal) {
        Workbook workbook = loadXlsFile(xlsFilePath);
        Sheet sheet = workbook.getSheet(sheetName);
        List<T> result = new ArrayList<T>();

        for (int i = 1; i < getSheetRows(sheet, horizontal); i++) {
            Cell[] row = getSheetRow(horizontal, sheet, i);

            T bean = BeanUtils.instantiate(beanClazz);
            BeanWrapper wrapper = new BeanWrapperImpl(bean);
            for (Cell cell : row) {
                String key = getCellHeader(sheet, cell, horizontal);
                if (!key.isEmpty()) {
                    String value = cell.getContents();
                    wrapper.setPropertyValue(key, value);
                }
            }
            result.add(bean);
        }
        return result;
    }

    private static String getCellHeader(Sheet sheet, Cell cell, boolean horizontal) {
        Cell headerCell = horizontal ? sheet.getCell(cell.getColumn(), 0) : sheet.getCell(0, cell.getRow());
        return headerCell.getContents();
    }

    private static Cell[] getSheetRow(boolean horizontal, Sheet sheet, int i) {
        return horizontal ? sheet.getRow(i) : sheet.getColumn(i);
    }

    private static int getSheetRows(Sheet sheet, boolean horizontal) {
        return horizontal ? sheet.getRows() : sheet.getColumns();
    }

    private static Workbook loadXlsFile(String xlsFilePath) {
        try {
            Resource resource = resourceLoader.getResource("classpath:"+xlsFilePath);
            return Workbook.getWorkbook(resource.getFile());
        } catch (IOException e) {
            throw new RuntimeException("Error while loading file " + xlsFilePath, e);
        } catch (BiffException e) {
            throw new RuntimeException("Error while parsing file " + xlsFilePath, e);
        }
    }

}
