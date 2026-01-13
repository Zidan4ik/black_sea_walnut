package org.example.black_sea_walnut.validator.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.black_sea_walnut.validator.validator.IsExistDiscountValidator;
import org.example.black_sea_walnut.validator.validator.IsExistTasteValidator;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = IsExistDiscountValidator.class)
public @interface IsExistDiscountValidation {
    public String message() default "{error.field.discount.IsExist}";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};
}
