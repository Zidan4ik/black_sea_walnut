package org.example.black_sea_walnut.validator.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.black_sea_walnut.dto.web.security.UserRequestForRegistration;
import org.example.black_sea_walnut.validator.annotation.PasswordValidation;

public class PasswordValidator implements ConstraintValidator<PasswordValidation, UserRequestForRegistration> {
    private String message;

    @Override
    public void initialize(PasswordValidation constraintAnnotation) {
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(UserRequestForRegistration dto, ConstraintValidatorContext context) {
        if (dto == null || dto.getPassword() == null || dto.getPasswordConfirm() == null) return false;
        boolean isValid = dto.getPassword().equals(dto.getPasswordConfirm());
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode("passwordConfirm")
                    .addConstraintViolation();
        }
        return isValid;
    }
}