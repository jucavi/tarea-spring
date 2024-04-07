package com.example.tareaspring.utils.validators.anotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CustomDateValidator implements ConstraintValidator<CustomDateFormat, String> {

    DateTimeFormatter formatter;
    @Override
    public void initialize(CustomDateFormat constraintAnnotation) {
        formatter = DateTimeFormatter.ofPattern(constraintAnnotation.pattern());
    }

    @Override
    public boolean isValid(String date, ConstraintValidatorContext constraintValidatorContext) {

        try {
            LocalDate.parse(date, formatter);
        } catch (DateTimeParseException ex) {
            return false;
        }

        return true;
    }
}
