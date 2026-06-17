package vn.edu.nlu.fit.thuctapltw.Util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class DateUtil {
    public static LocalDate parseDateOrDefault(String date, LocalDate defaultDate) {
        if (date == null || date.isBlank()) {
            return defaultDate;
        }

        try {
            return LocalDate.parse(date.trim());
        } catch (DateTimeParseException e) {
            return defaultDate;
        }
    }
}
