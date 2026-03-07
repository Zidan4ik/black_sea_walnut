package org.example.black_sea_walnut.dto.admin.user.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.example.black_sea_walnut.entity.User;
import org.example.black_sea_walnut.enums.Role;
import org.example.black_sea_walnut.mapper.UserMapper;
import org.example.black_sea_walnut.service.user.Saveable;
import org.example.black_sea_walnut.service.user.UserUpdater;
import org.example.black_sea_walnut.service.user.adress.HasAdditionalAddress;
import org.example.black_sea_walnut.service.user.adress.HasMainAddress;
import org.example.black_sea_walnut.validator.annotation.EmailValidation;
import org.example.black_sea_walnut.validator.annotation.IsNoExistEmail;
import org.example.black_sea_walnut.validator.annotation.MediaValidation;
import org.example.black_sea_walnut.validator.annotation.PhoneFormatValidation;
import org.example.black_sea_walnut.validator.groupValidation.EmailValidGroups;
import org.example.black_sea_walnut.validator.groupValidation.PhoneValidGroups;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserFopRequestForAdd implements UserUpdater, Saveable<User,UserMapper>, HasMainAddress, HasAdditionalAddress {
    private Long id;
    @NotBlank(message = "{error.field.empty}")
    @Length(max = 100, message = "{error.field.valid.length.title}")
    private String fullName;
    @PhoneFormatValidation(groups = PhoneValidGroups.NotPhoneFormatValidation.class)
    @NotBlank(message = "{error.field.empty}", groups = PhoneValidGroups.NotBlankCheck.class)
    @Length(max = 15, message = "{error.field.phone.size}", groups = PhoneValidGroups.NotLength.class)
    private String phone;
    @NotBlank(message = "{error.field.empty}", groups = EmailValidGroups.NotBlankCheck.class)
    @EmailValidation(groups = EmailValidGroups.EmailCheck.class)
    @IsNoExistEmail(groups = EmailValidGroups.EmailExistenceCheck.class)
    private String email;
    private Long countryForDeliveryId;
    private Long regionForDeliveryId;
    private Long cityForDeliveryId;
    private String departmentForDeliveryId;
    private String registrationType;
    private String status;
    private String edrpou;
    private Long regionAdditionallyId;
    private Long cityAdditionallyId;
    @NotBlank(message = "{error.field.empty}")
    private String addressAdditionally;
    private String password;
    private Role role;
    private String pathToImage;
    @MediaValidation(message = "{error.file.valid}", allowedTypes = {"image/png", "image/jpg", "image/jpeg"})
    private MultipartFile fileImage;

    @Override
    public void updateEntity(User user, UserMapper mapper) {
        mapper.updateEntityFromRequest(this, user);
    }

    public Integer getDepartmentAsInt() {
        try {
            return (departmentForDeliveryId != null) ? Integer.parseInt(departmentForDeliveryId) : null;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    public String getAddress() {
        return this.addressAdditionally;
    }
}
