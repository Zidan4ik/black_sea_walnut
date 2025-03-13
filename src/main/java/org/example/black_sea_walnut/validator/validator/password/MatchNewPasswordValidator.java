package org.example.black_sea_walnut.validator.validator.password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.black_sea_walnut.dto.web.user.PasswordDto;
import org.example.black_sea_walnut.validator.annotation.password.MatchNewPassword;

public class MatchNewPasswordValidator implements ConstraintValidator<MatchNewPassword, PasswordDto> {
    private String message;

    @Override
    public void initialize(MatchNewPassword constraintAnnotation) {
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(PasswordDto dto, ConstraintValidatorContext context) {
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