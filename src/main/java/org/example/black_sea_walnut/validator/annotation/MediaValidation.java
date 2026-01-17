package org.example.black_sea_walnut.validator.annotation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.black_sea_walnut.validator.validator.MediaValidator;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = MediaValidator.class)
public @interface MediaValidation {
    public String message() default "{error.file.valid}";

    String[] allowedTypes() default {};

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};
}
