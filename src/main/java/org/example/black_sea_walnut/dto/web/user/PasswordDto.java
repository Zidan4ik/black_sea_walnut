package org.example.black_sea_walnut.dto.web.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.example.black_sea_walnut.validator.annotation.password.MatchCurrentPasswordValidation;
import org.example.black_sea_walnut.validator.annotation.password.MatchNewPassword;
import org.example.black_sea_walnut.validator.groupValidation.PasswordValidGroups;

@Builder
@Data
@MatchNewPassword(groups = PasswordValidGroups.NotBlankConfirm.class)
public class PasswordDto {
    @MatchCurrentPasswordValidation(groups = PasswordValidGroups.NotMatchCurrentPasswordValidation.class)
    @NotBlank(message = "{error.field.empty}", groups = PasswordValidGroups.NotBlankCheck.class)
    private String currentPassword;
    @NotBlank(message = "{error.field.empty}", groups = PasswordValidGroups.NotBlankNew.class)
    private String newPassword;
    @NotBlank(message = "{error.field.empty}", groups = PasswordValidGroups.NotBlankConfirm.class)
    private String confirmPassword;
}
