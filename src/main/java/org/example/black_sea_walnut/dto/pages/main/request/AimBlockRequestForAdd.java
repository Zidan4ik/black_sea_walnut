package org.example.black_sea_walnut.dto.pages.main.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Setter;
import org.example.black_sea_walnut.enums.MediaType;
import org.example.black_sea_walnut.validator.annotation.MediaValidation;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AimBlockRequestForAdd {
    private Long mainAimId;
    private Boolean mainAimIsActive;
    @Setter
    private MediaType mediaType;
    @NotBlank(message = "{error.field.empty}")
    private String mainAimTitleUk;
    @NotBlank(message = "{error.field.empty}")
    private String mainAimTitleEn;
    @Size(max = 150, message = "{error.field.valid.size}")
    private String mainAimDescriptionUk;
    @Size(max = 150, message = "{error.field.valid.size}")
    private String mainAimDescriptionEn;
    @Setter
    private String mainAimPathToBanner;
    @MediaValidation(allowedTypes = {"image/png", "image/jpg", "image/jpeg"})
    private MultipartFile mainAimFileBanner;
}
