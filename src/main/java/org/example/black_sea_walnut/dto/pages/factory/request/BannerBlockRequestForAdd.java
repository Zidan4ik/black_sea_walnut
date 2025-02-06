package org.example.black_sea_walnut.dto.pages.factory.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Setter;
import org.example.black_sea_walnut.enums.MediaType;
import org.example.black_sea_walnut.validator.annotation.MediaValidation;
import org.springframework.web.multipart.MultipartFile;

@Data
public class BannerBlockRequestForAdd {
    private Long factoryBannerId;
    private Boolean factoryBannerIsActive;
    @Setter
    private MediaType mediaType;
    @NotBlank(message = "{error.field.empty}")
    private String factoryBannerTitleUk;
    @NotBlank(message = "{error.field.empty}")
    private String factoryBannerTitleEn;
    @Size(max = 150, message = "{error.field.valid.size}")
    private String factoryBannerDescriptionUk;
    @Size(max = 150, message = "{error.field.valid.size}")
    private String factoryBannerDescriptionEn;
    @Setter
    private String factoryBannerPathToBanner;
    @MediaValidation(message = "{error.file.valid}", allowedTypes = {"image/png", "image/jpg", "image/jpeg"})
    private MultipartFile factoryBannerFile;
}   
