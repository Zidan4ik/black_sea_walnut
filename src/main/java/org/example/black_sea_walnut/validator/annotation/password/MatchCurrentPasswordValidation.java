package org.example.black_sea_walnut.validator.annotation.password;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.black_sea_walnut.validator.validator.password.MatchCurrentPasswordValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = MatchCurrentPasswordValidator.class)
@Target({ElementType.FIELD, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MatchCurrentPasswordValidation {

    String message() default "{text.matchPassword}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
