package com.example.tareaspring.utils.validators.anotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CustomDateValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface  CustomDateFormat {
    String message() default "Invalid date";

    String pattern() default "yyyy-MM-dd";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
