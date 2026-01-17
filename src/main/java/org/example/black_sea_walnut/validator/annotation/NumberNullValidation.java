package org.example.black_sea_walnut.validator.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.black_sea_walnut.validator.validator.NumberNullValidator;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = NumberNullValidator.class)
public @interface NumberNullValidation {
    public String message() default "{error.field.empty.number}";
    public Class<?>[] groups() default {};
    public Class<? extends Payload>[] payload() default {};
}
