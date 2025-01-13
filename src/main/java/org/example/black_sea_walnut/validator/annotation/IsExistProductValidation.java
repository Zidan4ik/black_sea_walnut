package org.example.black_sea_walnut.validator.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.black_sea_walnut.validator.validator.IsExistProductValidator;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = IsExistProductValidator.class)
public @interface IsExistProductValidation {
    public String message() default "Product is already exist in database!";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};
}
