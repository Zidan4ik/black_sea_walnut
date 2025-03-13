package org.example.black_sea_walnut.dto.contact;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.example.black_sea_walnut.validator.annotation.CoordinatesValidation;
import org.example.black_sea_walnut.validator.annotation.EmailValidation;
import org.example.black_sea_walnut.validator.annotation.IsNoExistEmail;
import org.example.black_sea_walnut.validator.annotation.PhoneFormatValidation;
import org.example.black_sea_walnut.validator.groupValidation.EmailValidGroups;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
public class ContactDtoForAdd {
    private Long id;
    @PhoneFormatValidation
    @NotBlank(message = "{error.field.empty}")
    @Length(max = 15,message = "{error.field.phone.size}")
    private String phone1;
    @PhoneFormatValidation
    @NotBlank(message = "{error.field.empty}")
    @Length(max = 15,message = "{error.field.phone.size}")
    private String phone2;
    @NotBlank(message = "{error.field.empty}", groups = EmailValidGroups.NotBlankCheck.class)
    @EmailValidation(groups = EmailValidGroups.EmailCheck.class)
    @IsNoExistEmail(groups = EmailValidGroups.EmailExistenceCheck.class)
    private String email;
    @NotBlank(message = "{error.field.empty}")
    private String addressWork;
    @NotBlank(message = "{error.field.empty}")
    private String addressFactory;
    @CoordinatesValidation
    @NotBlank(message = "{error.field.empty}")
    private String coordinates;
    @NotBlank(message = "{error.field.empty}")
    private String telegram;
    @NotBlank(message = "{error.field.empty}")
    private String viber;
    @NotBlank(message = "{error.field.empty}")
    private String watsapp;
    @NotBlank(message = "{error.field.empty}")
    private String facebook;
    @NotBlank(message = "{error.field.empty}")
    private String instagram;
    @NotBlank(message = "{error.field.empty}")
    private String youtube;
}
