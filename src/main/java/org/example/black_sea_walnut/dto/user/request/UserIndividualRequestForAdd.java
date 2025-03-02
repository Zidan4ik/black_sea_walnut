package org.example.black_sea_walnut.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.example.black_sea_walnut.enums.Role;
import org.example.black_sea_walnut.validator.annotation.MediaValidation;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserIndividualRequestForAdd {
    private Long id;
    @NotBlank(message = "{error.field.empty}")
    @Length(max = 100,message = "{error.field.valid.length.title}")
    private String fullName;
    @NotBlank(message = "{error.field.empty}")
    private String phone;
    @NotBlank(message = "{error.field.empty}")
    private String email;
    private Long regionForDeliveryId;
    private Long cityForDeliveryId;
    private int departmentForDeliveryId;
    private String registrationType;
    private String status;
    private String pathToImage;
    @MediaValidation(message = "{error.file.valid}", allowedTypes = {"image/png", "image/jpg", "image/jpeg"})
    private MultipartFile fileImage;
    private String password;
    private String passwordConfirm;
    private Role role;
}