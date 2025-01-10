package org.example.black_sea_walnut.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = NumberNullValidator.class)
public @interface NumberNullValidation {
    public String message() default "The number should be present!";
    public Class<?>[] groups() default {};
    public Class<? extends Payload>[] payload() default {};
}
