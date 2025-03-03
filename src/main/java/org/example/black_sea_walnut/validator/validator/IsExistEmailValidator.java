package org.example.black_sea_walnut.validator.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.service.UserService;
import org.example.black_sea_walnut.validator.annotation.IsExistEmail;


@RequiredArgsConstructor
public class IsExistEmailValidator implements ConstraintValidator<IsExistEmail, String> {
    private final UserService userService;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return email != null && !userService.isExistUserByEmail(email);
    }
}
