package org.example.black_sea_walnut.validator.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.black_sea_walnut.validator.annotation.EmailValidation;

import java.util.regex.Pattern;

public class EmailFormatValidator implements ConstraintValidator<EmailValidation, String> {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );
    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null) {
            return true;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
}