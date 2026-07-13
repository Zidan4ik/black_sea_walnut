package org.example.black_sea_walnut.service.document.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public interface ExcelExporter<T> {

    default ByteArrayInputStream exportToExcel(
            String sheetName,
            Class<T> entityClass,
            List<T> data,
            Map<String, BiFunction<T, Row, String>> customCellMappers
    ) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet(sheetName);

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);
            headerCellStyle.setFillForegroundColor(IndexedColors.SEA_GREEN.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Field[] fields = entityClass.getDeclaredFields();
            List<Field> validFields = new ArrayList<>();

            for (Field field : fields) {
                if (!Collection.class.isAssignableFrom(field.getType())) {
                    validFields.add(field);
                }
            }

            Row headerRow = sheet.createRow(0);
            int colIndex = 0;

            for (Field field : validFields) {
                Cell cell = headerRow.createCell(colIndex++);
                cell.setCellValue(field.getName().toUpperCase());
                cell.setCellStyle(headerCellStyle);
            }

            if (customCellMappers != null) {
                for (String customHeader : customCellMappers.keySet()) {
                    Cell cell = headerRow.createCell(colIndex++);
                    cell.setCellValue(customHeader.toUpperCase());
                    cell.setCellStyle(headerCellStyle);
                }
            }

            int rowIndex = 1;
            for (T entity : data) {
                Row row = sheet.createRow(rowIndex++);
                int currentCellIndex = 0;

                for (Field field : validFields) {
                    field.setAccessible(true);
                    Object value = field.get(entity);
                    Cell cell = row.createCell(currentCellIndex++);

                    if (value != null) {
                        if (value instanceof Number) {
                            cell.setCellValue(((Number) value).doubleValue());
                        } else if (value instanceof Boolean) {
                            cell.setCellValue((Boolean) value ? "YES" : "NO");
                        } else if (value instanceof LocalDateTime) {
                            cell.setCellValue(((LocalDateTime) value).toString());
                        } else {
                            cell.setCellValue(value.toString());
                        }
                    } else {
                        cell.setCellValue("");
                    }
                }

                if (customCellMappers != null) {
                    for (BiFunction<T, Row, String> mapper : customCellMappers.values()) {
                        Cell cell = row.createCell(currentCellIndex++);
                        String customValue = mapper.apply(entity, row);
                        cell.setCellValue(customValue != null ? customValue : "");
                    }
                }
            }

            for (int i = 0; i < colIndex; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException | IllegalAccessException e) {
            throw new RuntimeException("Помилка генерації Excel через Рефлексію: " + e.getMessage());
        }
    }
}
