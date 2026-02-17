package org.example.black_sea_walnut.dto.web.security;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.example.black_sea_walnut.enums.Role;
import org.example.black_sea_walnut.validator.annotation.*;
import org.example.black_sea_walnut.validator.groupValidation.PasswordValidGroups;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@PasswordValidation(groups = PasswordValidGroups.NotBlankConfirm.class)
public class UserRequestForRegistration {
    private Long id;
    @NotBlank(message = "{error.field.empty}")
    @Length(max = 100, message = "{error.field.valid.length.title}")
    private String fullName;
    @PhoneFormatValidation
    @NotBlank(message = "{error.field.empty}")
    @Length(max = 15,message = "{error.field.phone.size}")
    private String phone;
    @EmailValidation
    @NotBlank(message = "{error.field.empty}")
    @IsExistEmail
    private String email;
    private Long countryForDeliveryId;
    private Long regionForDeliveryId;
    private Long cityForDeliveryId;
    private Long countryForDeliveryIdLegal;
    private Long regionForDeliveryIdLegal;
    private Long cityForDeliveryIdLegal;
    private String address;
    private String addressLegal;
    private String index;
    private String paymentDetails;
    private String registrationType;
    private String status;
    private String pathToImage;
    private boolean isFop;
    @MediaValidation(message = "{error.file.valid}", allowedTypes = {"image/png", "image/jpg", "image/jpeg"})
    private MultipartFile fileImage;
    @NotBlank(message = "{error.field.password.empty}", groups =  PasswordValidGroups.NotBlankNew.class)
    private String password;
    @NotBlank(message = "{error.field.password.empty}", groups =  PasswordValidGroups.NotBlankConfirm.class)
    private String passwordConfirm;
    private Role role;
}