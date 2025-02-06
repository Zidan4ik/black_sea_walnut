package org.example.black_sea_walnut.validator.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.black_sea_walnut.validator.annotation.CoordinatesValidation;

import java.util.regex.Pattern;

public class CoordinatesValidator implements ConstraintValidator<CoordinatesValidation, String> {
    private static final Pattern COORDINATES_PATTERN = Pattern.compile(
            "^[-+]?(([0-8]\\d|\\d)(\\.\\d+)?|90(\\.0+)?),\\s*[-+]?((1[0-7]\\d(\\.\\d+)" +
                    "?)|(180(\\.0+)?)|(\\d\\d(\\.\\d+)?)|(\\d(\\.\\d+)?))$"
    );
    @Override
    public boolean isValid(String coordinates, ConstraintValidatorContext context) {
        if (coordinates == null) {
            return true;
        }
        return COORDINATES_PATTERN.matcher(coordinates).matches();
    }
}