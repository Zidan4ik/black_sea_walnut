package org.example.black_sea_walnut.validator.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.black_sea_walnut.validator.annotation.NumberNullValidation;

public class NumberNullValidator implements ConstraintValidator<NumberNullValidation, Long> {
    @Override
    public boolean isValid(Long number, ConstraintValidatorContext constraintValidatorContext) {
        return number != null;
    }
}
