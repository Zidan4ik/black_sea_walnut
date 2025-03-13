package org.example.black_sea_walnut.dto.web.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.example.black_sea_walnut.validator.annotation.password.MatchCurrentPasswordValidation;
import org.example.black_sea_walnut.validator.annotation.password.MatchNewPassword;

@Builder
@Data
@MatchNewPassword
public class PasswordDto {
    @MatchCurrentPasswordValidation
    @NotBlank(message = "{error.field.empty}")
    private String currentPassword;
    @NotBlank(message = "{error.field.empty}")
    private String newPassword;
    private String confirmPassword;
}
