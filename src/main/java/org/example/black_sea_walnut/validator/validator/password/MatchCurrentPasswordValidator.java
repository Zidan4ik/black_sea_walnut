package org.example.black_sea_walnut.validator.validator.password;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.entity.User;
import org.example.black_sea_walnut.service.UserService;
import org.example.black_sea_walnut.validator.annotation.password.MatchCurrentPasswordValidation;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class MatchCurrentPasswordValidator implements ConstraintValidator<MatchCurrentPasswordValidation, String> {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            User user = userService.getByEmail(userDetails.getUsername()).orElseThrow(
                    (() -> new EntityNotFoundException("User with email: " + userDetails.getUsername() + " was not found!"))
            );
            return passwordEncoder.matches(password, user.getPassword());
        }
        return false;
    }
}
