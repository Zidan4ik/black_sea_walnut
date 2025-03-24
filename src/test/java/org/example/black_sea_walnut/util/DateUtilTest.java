package org.example.black_sea_walnut.util;


import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class DateUtilTest {
    @Test
    void shouldFormatDateFromDB() {
        LocalDate date = LocalDate.of(2025, 3, 23);
        String outputFormat = "dd/MM/yyyy";
        String formattedDate = DateUtil.toFormatDateFromDB(date, outputFormat);
        assertThat(formattedDate).isEqualTo("23/03/2025");
    }

    @Test
    void shouldFormatDateToDB() {
        String dateString = "23/03/2025";
        String pattern = "dd/MM/yyyy";
        LocalDate formattedDate = DateUtil.toFormatDateToDB(dateString, pattern);
        assertThat(formattedDate).isEqualTo(LocalDate.of(2025, 3, 23));
    }

    @Test
    void shouldReturnSameDateWhenSameFormatIsUsedForDBAndOutput() {
        LocalDate date = LocalDate.of(2025, 3, 23);
        String outputFormat = "yyyy-MM-dd";
        String formattedDate = DateUtil.toFormatDateFromDB(date, outputFormat);
        assertThat(formattedDate).isEqualTo("2025-03-23");
    }

    @Test
    void shouldThrowExceptionWhenInvalidDateFormatIsProvidedForToFormatDateToDB() {String invalidDateString = "2025-03-23";
        String invalidPattern = "dd/MM/yyyy";
        assertThatThrownBy(() -> DateUtil.toFormatDateToDB(invalidDateString, invalidPattern))
                .isInstanceOf(java.time.format.DateTimeParseException.class);
    }
}