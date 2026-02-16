package org.example.black_sea_walnut.validator.annotation.password;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.black_sea_walnut.validator.validator.password.MatchNewPasswordValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MatchNewPasswordValidator.class)
public @interface MatchNewPassword {
    String message() default "{error.field.password.similar}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
