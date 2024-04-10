package com.example.tareaspring.utils;

// TODO: Merge with DateUtilsValidations

import com.example.tareaspring.errors.DateFormatException;
import org.springframework.lang.NonNull;

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
    public static String fromLocalDateToString(@NonNull LocalDate localDate, String pattern) {

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


    /**
     * Checks if date belongs to a range date
     * @param startDate start date of the range
     * @param endDate end date of a range
     * @param date date to be checked
     * @return if date belongs to range date {@code true}, otherwise {@code false}
     */
    public static boolean isDateInRange(@NonNull LocalDate startDate, @NonNull LocalDate endDate, @NonNull LocalDate date) {
        boolean isAfterOrEqualSince = date.isAfter(startDate) || date.isEqual(startDate);
        boolean isBeforeOrEqualUntil = date.isBefore(endDate) || date.isEqual(endDate);

        return isAfterOrEqualSince && isBeforeOrEqualUntil;
    }

    /**
     * Check if [starDate, endDate] is included in [since, until]
     */
    public static boolean isRangeDateSubset(
            LocalDate startDate,
            LocalDate endDate,
            LocalDate since,
            LocalDate until) {

        // if both edges in
        //                   [since,                until]
        //                      [startDate,  endDate]
        // is at least a subset
        return (isDateInRange(since, until, startDate)
                && isDateInRange(since, until, endDate));
    }
}
