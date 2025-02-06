package org.example.black_sea_walnut.validator.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.black_sea_walnut.validator.validator.EmailFormatValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EmailFormatValidator.class)
@Target({ElementType.FIELD, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailValidation {
    String message() default "{error.field.email.format}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}