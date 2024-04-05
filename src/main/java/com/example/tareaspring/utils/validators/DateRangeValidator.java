package com.example.tareaspring.utils.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.time.LocalDate;

public class DateRangeValidator implements ConstraintValidator<DateRange, Object> {
    private String beforeFieldName;
    private String afterFieldName;

    @Override
    public void initialize(DateRange constraintAnnotation) {
        beforeFieldName = constraintAnnotation.before();
        afterFieldName = constraintAnnotation.after();
    }

    @Override
    public boolean isValid(final Object value, ConstraintValidatorContext context) {
        try {
            final Field beforeDateField = value.getClass().getDeclaredField(beforeFieldName);
            beforeDateField.setAccessible(true);

            final Field afterDateField = value.getClass().getDeclaredField(afterFieldName);
            afterDateField.setAccessible(true);

            final LocalDate beforeDate = (LocalDate) beforeDateField.get(value);
            final LocalDate afterDate = (LocalDate) afterDateField.get(value);
            return beforeDate.isBefore(afterDate);

        } catch (NoSuchFieldException | IllegalAccessException ignored) {
            return false;
        }
    }
}