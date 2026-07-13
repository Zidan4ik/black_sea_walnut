package org.example.black_sea_walnut.service.document.pdf;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.example.black_sea_walnut.service.history.DtoResponse;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class PdfExportService {
    public byte[] exportToPdf(Object dataObject, String titleText) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate(), 36, 36, 36, 36);
        PdfWriter.getInstance(document, out);
        document.open();

        byte[] fontBytes = getClass().getResourceAsStream("/static/fonts/arial.ttf").readAllBytes();
        BaseFont bf = BaseFont.createFont("arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, true, fontBytes, null);
        Font titleFont = new Font(bf, 16, Font.BOLD, new Color(31, 41, 55));

        Paragraph title = new Paragraph(titleText, titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        if (dataObject instanceof Collection) {
            Collection<?> collection = (Collection<?>) dataObject;
            if (!collection.isEmpty()) {
                generateTableForClass(document, collection, collection.iterator().next().getClass(), bf);
            }
        } else if (dataObject instanceof DtoResponse) {
            buildPdfFromMainDto(document, dataObject, bf);
        }

        document.close();
        return out.toByteArray();
    }

    private void buildPdfFromMainDto(Document document, Object mainDto, BaseFont bf) throws Exception {
        Field[] fields = mainDto.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            Object fieldValue = field.get(mainDto);
            if (fieldValue == null) continue;

            if (fieldValue instanceof Collection) {
                Collection<?> collection = (Collection<?>) fieldValue;
                if (!collection.isEmpty()) {
                    Object firstItem = collection.iterator().next();
                    if (firstItem instanceof DtoResponse) {
                        Font sectionFont = new Font(bf, 12, Font.BOLD, new Color(51, 125, 90));
                        Paragraph sectionTitle = new Paragraph("Категорія: " + field.getName().toUpperCase(), sectionFont);
                        sectionTitle.setSpacingBefore(15);
                        sectionTitle.setSpacingAfter(10);
                        document.add(sectionTitle);
                        generateTableForClass(document, collection, firstItem.getClass(), bf);
                    }
                }
            }
        }
    }

    private void generateTableForClass(Document document, Collection<?> data, Class<?> clazz, BaseFont bf) throws Exception {
        Field[] fields = clazz.getDeclaredFields();
        List<Field> validFields = new ArrayList<>();
        for (Field f : fields) {
            if (!Collection.class.isAssignableFrom(f.getType())) {
                f.setAccessible(true);
                validFields.add(f);
            }
        }
        if (validFields.isEmpty()) return;
        PdfPTable table = new PdfPTable(validFields.size());
        table.setWidthPercentage(100);
        table.setSpacingAfter(15);

        Font headerFont = new Font(bf, 10, Font.BOLD, new Color(31, 41, 55));
        Font textFont = new Font(bf, 9, Font.NORMAL, new Color(55, 65, 81));

        for (Field field : validFields) {
            String columnName = field.getName();
            columnName = columnName.substring(0, 1).toUpperCase() + columnName.substring(1);

            PdfPCell headerCell = new PdfPCell(new Paragraph(columnName, headerFont));
            headerCell.setBorder(Rectangle.BOTTOM);
            headerCell.setBorderWidthBottom(1.5f);
            headerCell.setBorderColor(new Color(51, 125, 90));
            headerCell.setPadding(8);
            headerCell.setBackgroundColor(new Color(240, 247, 244));
            table.addCell(headerCell);
        }

        boolean isRowEven = false;
        Color zebraColor = new Color(249, 250, 251);

        for (Object item : data) {
            for (Field field : validFields) {
                Object value = field.get(item);
                String cellText = (value != null) ? value.toString() : "-";

                PdfPCell cell = new PdfPCell(new Paragraph(cellText, textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setPadding(8);

                if (isRowEven) {
                    cell.setBackgroundColor(zebraColor);
                }
                table.addCell(cell);
            }
            isRowEven = !isRowEven;
        }

        document.add(table);
    }}
