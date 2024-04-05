package com.example.tareaspring.utils.validators.date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class CustomDateValidator implements ConstraintValidator<CustomDateConstraint, LocalDate> {

    private static final String DATE_PATTERN = "yyyy-MM-dd";

    @Override
    public boolean isValid(LocalDate customDateField,
                           ConstraintValidatorContext cxt) {
        SimpleDateFormat o = new SimpleDateFormat(DATE_PATTERN);
        try {
            o.setLenient(false);
            Date d = o.parse(customDateField.toString());
            return true;

        } catch (ParseException e) {
            //
        }

        return false;
    }

}
