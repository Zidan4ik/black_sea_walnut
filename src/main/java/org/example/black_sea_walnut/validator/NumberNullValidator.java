package org.example.black_sea_walnut.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NumberNullValidator implements ConstraintValidator<NumberNullValidation, Long> {
    @Override
    public boolean isValid(Long number, ConstraintValidatorContext constraintValidatorContext) {
        return number != null;
    }
}
