package org.example.black_sea_walnut.dto.admin.user.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.example.black_sea_walnut.enums.Role;
import org.example.black_sea_walnut.validator.annotation.EmailValidation;
import org.example.black_sea_walnut.validator.annotation.IsNoExistEmail;
import org.example.black_sea_walnut.validator.annotation.MediaValidation;
import org.example.black_sea_walnut.validator.annotation.PhoneFormatValidation;
import org.example.black_sea_walnut.validator.groupValidation.EmailValidGroups;
import org.example.black_sea_walnut.validator.groupValidation.PhoneValidGroups;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserLegalRequestForView {
    private Long id;
    @NotBlank(message = "{error.field.empty}")
    @Length(max = 100,message = "{error.field.valid.length.title}")
    private String fullName;
    @PhoneFormatValidation(groups = PhoneValidGroups.NotPhoneFormatValidation.class)
    @NotBlank(message = "{error.field.empty}",groups = PhoneValidGroups.NotBlankCheck.class)
    @Length(max = 15,message = "{error.field.phone.size}",groups = PhoneValidGroups.NotLength.class)
    private String phone;
    @NotBlank(message = "{error.field.empty}", groups = EmailValidGroups.NotBlankCheck.class)
    @EmailValidation(groups = EmailValidGroups.EmailCheck.class)
    @IsNoExistEmail(groups = EmailValidGroups.EmailExistenceCheck.class)
    private String email;
    private Long regionForDeliveryId;
    private Long cityForDeliveryId;
    @NotBlank(message = "{error.field.empty}")
    private String departmentForDelivery;
    private String registrationType;
    private String status;
    private String pathToImage;
    @MediaValidation(message = "{error.file.valid}", allowedTypes = {"image/png", "image/jpg", "image/jpeg"})
    private MultipartFile fileImage;
    @NotBlank(message = "{error.field.empty}")
    private String okpo;
    private Long regionAdditionallyId;
    private Long cityAdditionallyId;
    @NotBlank(message = "{error.field.empty}")
    private String addressAdditionally;
    @NotBlank(message = "{error.field.empty}")
    private String indexLawful;
    private String password;
    private Role role;
}
