package org.example.black_sea_walnut.validator.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.black_sea_walnut.password.PasswordResetRequest;
import org.example.black_sea_walnut.validator.annotation.PasswordValidationForReset;

public class PasswordValidatorForReset implements ConstraintValidator<PasswordValidationForReset, PasswordResetRequest> {
    private String message;

    @Override
    public void initialize(PasswordValidationForReset constraintAnnotation) {
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(PasswordResetRequest dto, ConstraintValidatorContext context) {
        if (dto == null || dto.getNewPassword() == null || dto.getConfirmPassword() == null) return false;
        boolean isValid = dto.getNewPassword().equals(dto.getConfirmPassword());
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation();
        }
        return isValid;
    }
}
