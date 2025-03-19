package org.example.black_sea_walnut.dto.web.security;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.example.black_sea_walnut.validator.annotation.EmailValidation;
import org.example.black_sea_walnut.validator.annotation.IsNoExistEmail;
import org.example.black_sea_walnut.validator.groupValidation.EmailValidGroups;

@Data
@Builder
public class EmailRecoveryDto {
    @NotBlank(message = "{error.field.empty}", groups = EmailValidGroups.NotBlankCheck.class)
    @EmailValidation(groups = EmailValidGroups.EmailCheck.class)
    @IsNoExistEmail(groups = EmailValidGroups.EmailExistenceCheck.class)
    private String email;
}
