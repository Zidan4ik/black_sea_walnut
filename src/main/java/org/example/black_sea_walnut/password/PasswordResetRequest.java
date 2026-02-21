package org.example.black_sea_walnut.password;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.example.black_sea_walnut.validator.annotation.EmailValidation;
import org.example.black_sea_walnut.validator.annotation.IsNoExistEmail;
import org.example.black_sea_walnut.validator.annotation.PasswordValidationForReset;
import org.example.black_sea_walnut.validator.groupValidation.EmailValidGroups;
import org.example.black_sea_walnut.validator.groupValidation.PasswordValidGroups;

@Data
@PasswordValidationForReset
public class PasswordResetRequest {
    @NotBlank(message = "{error.field.empty}", groups = PasswordValidGroups.class)
    private String newPassword;
    @NotBlank(message = "{error.field.empty}", groups = PasswordValidGroups.class)
    private String confirmPassword;
}
