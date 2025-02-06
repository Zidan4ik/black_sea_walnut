package org.example.black_sea_walnut.validator.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.black_sea_walnut.validator.validator.PhoneFormatValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PhoneFormatValidator.class)
@Target({ElementType.FIELD, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneFormatValidation {
    String message() default "{error.field.phone.format}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
