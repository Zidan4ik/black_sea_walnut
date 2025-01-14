package org.example.black_sea_walnut.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    public static String toFormatDateFromDB(LocalDate date, String outputFormat) {
        DateTimeFormatter input = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter output = DateTimeFormatter.ofPattern(outputFormat);
        LocalDate localDate = LocalDate.parse(date.toString(), input);
        return localDate.format(output);
    }

    public static LocalDate toFormatDateToDB(String date, String pattern) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern));
    }
}
