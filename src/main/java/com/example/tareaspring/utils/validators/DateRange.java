package com.example.tareaspring.utils.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = DateRangeValidator.class)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DateRange {
    String message() default "{mob.concept.admin.models.constraint.DateRange.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String before();

    String after();
}