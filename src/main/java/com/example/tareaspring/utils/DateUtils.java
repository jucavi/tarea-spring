package com.example.tareaspring.utils;

// TODO: Merge with DateUtilsValidations

import com.example.tareaspring.errors.DateFormatException;
import lombok.extern.log4j.Log4j2;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

/**
 * Common task for dates manipulation and validations
 */
public class DateUtils {

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
