package com.example.tareaspring.utils;

// TODO: Merge with DateUtilsValidations

import com.example.tareaspring.errors.DateFormatException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Common task for dates manipulation and validations
 */
public class DateUtils {

    /**
     * Convert to Local date from string with format passed as parameter.
     * @param strDate string date representation
     * @param formatter formatter. Default {@code DateTimeFormatter.ISO_LOCAL_DATE}
     * @return local date
     */
    public static LocalDate fromStringToLocaldate(String strDate, DateTimeFormatter formatter) {

        LocalDate localDate;

        try {
            if (formatter == null) {
                localDate = LocalDate.parse(strDate);
            } else {
                localDate = LocalDate.parse(strDate, formatter);
            }
        } catch (Exception ex) {
            throw new DateFormatException("Invalid date");
        }

        return localDate;
    }


    /**
     * Convert from local date to string representation with pattern passed as parameter
     * @param localDate local date
     * @param pattern string pattern. Default {@code "yyyy-MM-dd"}
     * @return string representation of {@code local date}
     */
    public static String fromSLocalDateToString(LocalDate localDate, String pattern) {

        String strDate;

        try {
            DateTimeFormatter formatter;

            if (pattern == null) {
                formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            } else {
                formatter = DateTimeFormatter.ofPattern(pattern);
            }

            strDate = localDate.format(formatter);
        } catch (Exception ex) {
            throw new DateFormatException("Invalid date");
        }

        return strDate;
    }
}
