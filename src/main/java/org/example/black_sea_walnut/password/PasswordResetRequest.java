package org.example.black_sea_walnut.password;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.example.black_sea_walnut.validator.annotation.EmailValidation;
import org.example.black_sea_walnut.validator.annotation.IsNoExistEmail;
import org.example.black_sea_walnut.validator.annotation.PasswordValidationForReset;
import org.example.black_sea_walnut.validator.groupValidation.EmailValidGroups;

@Data
@PasswordValidationForReset
public class PasswordResetRequest {
    @NotBlank(message = "{error.field.empty}", groups = EmailValidGroups.NotBlankCheck.class)
    @EmailValidation(groups = EmailValidGroups.EmailCheck.class)
    @IsNoExistEmail(groups = EmailValidGroups.EmailExistenceCheck.class)
    private String email;
    @NotBlank(message = "{error.field.empty}")
    private String newPassword;
    private String confirmPassword;
}
