package com.example.tareaspring.utils.validators.utils;

import java.time.LocalDate;

public class DateUtilsValidator {

    public static boolean isDateInRange(LocalDate since, LocalDate until, LocalDate date) {
        boolean isAfterOrEqualSince = date.isAfter(since) || date.isEqual(since);
        boolean isBeforeOrEqualUntil = date.isBefore(until) || date.isEqual(until);

        // if date in range keep it
        return isAfterOrEqualSince && isBeforeOrEqualUntil;
    }
}
