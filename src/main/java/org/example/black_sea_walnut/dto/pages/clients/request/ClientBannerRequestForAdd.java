package org.example.black_sea_walnut.dto.pages.clients.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Setter;
import org.example.black_sea_walnut.enums.MediaType;
import org.example.black_sea_walnut.validator.annotation.MediaValidation;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ClientBannerRequestForAdd {
    private Long clientsBannerId;
    private Boolean clientsBannerIsActive;
    private MediaType mediaType;
    @NotBlank(message = "{error.field.empty}")
    private String clientsBannerTitleUk;
    @NotBlank(message = "{error.field.empty}")
    private String clientsBannerTitleEn;
    @Size(max = 150, message = "{error.field.valid.size}")
    private String clientsBannerDescriptionUk;
    @Size(max = 150, message = "{error.field.valid.size}")
    private String clientsBannerDescriptionEn;
    @Setter
    private String clientsBannerPathToBanner;
    @MediaValidation(message = "{error.file.valid}", allowedTypes = {"image/png", "image/jpg", "image/jpeg"})
    private MultipartFile clientsBannerFile;
}