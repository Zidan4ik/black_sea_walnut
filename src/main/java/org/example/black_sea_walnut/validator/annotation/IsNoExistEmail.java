package org.example.black_sea_walnut.validator.annotation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.black_sea_walnut.validator.validator.IsNoExistEmailValidator;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = IsNoExistEmailValidator.class)
public @interface IsNoExistEmail {
    public String message() default "{error.field.email.noIsExist}";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};
}
