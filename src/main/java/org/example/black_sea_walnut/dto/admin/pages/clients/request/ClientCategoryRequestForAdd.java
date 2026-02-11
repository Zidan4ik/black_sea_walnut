package org.example.black_sea_walnut.dto.admin.pages.clients.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Setter;
import org.example.black_sea_walnut.enums.MediaType;
import org.example.black_sea_walnut.validator.annotation.MediaValidation;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ClientCategoryRequestForAdd {
    private Long clientsCategoryId;
    private Boolean clientsCategoryIsActive;
    private MediaType mediaTypeSvg;
    private MediaType mediaTypeImage;
    @NotBlank(message = "{error.field.empty}")
    @Length(max = 100, message = "{error.field.valid.length.title}")
    private String clientsCategoryTitleUk;
    @NotBlank(message = "{error.field.empty}")
    @Length(max = 100, message = "{error.field.valid.length.title}")
    private String clientsCategoryTitleEn;
    @NotBlank(message = "{error.field.empty}")
    @Length(max = 100, message = "{error.field.valid.length.title}")
    private String clientsCategorySubtitleUk;
    @NotBlank(message = "{error.field.empty}")
    @Length(max = 100, message = "{error.field.valid.length.title}")
    private String clientsCategorySubtitleEn;
    @Size(max = 500, message = "{error.field.valid.size}")
    private String clientsCategoryDescriptionUk;
    @Size(max = 500, message = "{error.field.valid.size}")
    private String clientsCategoryDescriptionEn;
    @Setter
    private String clientsCategoryPathToImage;
    @Setter
    private String clientsCategoryPathToSvg;
    @MediaValidation(message = "{error.file.valid}", allowedTypes = {"image/png", "image/jpg", "image/jpeg"})
    private MultipartFile clientsCategoryFileImage;
    @MediaValidation(message = "{error.file.valid}", allowedTypes = {"image/png", "image/jpg", "image/jpeg", "image/svg+xml"})
    private MultipartFile clientsCategoryFileSvg;
}
