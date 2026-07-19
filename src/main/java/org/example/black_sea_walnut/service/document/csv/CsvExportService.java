package org.example.black_sea_walnut.service.document.csv;

import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class CsvExportService {
    public <T> byte[] export(List<T> data) {

        if (data == null || data.isEmpty()) {
            return new byte[0];
        }

        StringBuilder csv = new StringBuilder();

        Class<?> clazz = data.get(0).getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            csv.append(fields[i].getName());

            if (i < fields.length - 1) {
                csv.append(",");
            }
        }

        csv.append("\n");

        // Rows
        for (T item : data) {

            for (int i = 0; i < fields.length; i++) {

                Field field = fields[i];
                field.setAccessible(true);

                try {
                    Object value = field.get(item);
                    csv.append(escape(value));
                } catch (IllegalAccessException e) {
                    csv.append("");
                }

                if (i < fields.length - 1) {
                    csv.append(",");
                }
            }

            csv.append("\n");
        }

        return addBom(csv.toString());
    }

    private String escape(Object value) {

        if (value == null) {
            return "";
        }

        String text = value.toString();

        if (text.contains(",") || text.contains("\"") || text.contains("\n")) {
            text = "\"" + text.replace("\"", "\"\"") + "\"";
        }

        return text;
    }

    private byte[] addBom(String text) {

        byte[] bom = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
        byte[] content = text.getBytes(StandardCharsets.UTF_8);

        byte[] result = new byte[bom.length + content.length];

        System.arraycopy(bom, 0, result, 0, bom.length);
        System.arraycopy(content, 0, result, bom.length, content.length);

        return result;
    }
}
