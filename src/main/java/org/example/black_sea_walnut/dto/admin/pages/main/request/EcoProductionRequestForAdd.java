package org.example.black_sea_walnut.dto.admin.pages.main.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.black_sea_walnut.enums.MediaType;
import org.example.black_sea_walnut.validator.annotation.MediaValidation;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Getter
public class EcoProductionRequestForAdd {
    private Long mainEcoProductionId;
    private Boolean mainEcoProductionIsActive;
    @Setter
    private MediaType mediaType;
    @NotBlank(message = "{error.field.empty}")
    @Length(max = 100,message = "{error.field.valid.length.title}")
    private String mainEcoProductionTitleUk;
    @NotBlank(message = "{error.field.empty}")
    @Length(max = 100,message = "{error.field.valid.length.title}")
    private String mainEcoProductionTitleEn;
    @Size(max = 150, message = "{error.field.valid.size}")
    private String mainEcoProductionDescriptionUk;
    @Size(max = 150, message = "{error.field.valid.size}")
    private String mainEcoProductionDescriptionEn;
    @Setter
    private String mainEcoProductionPathToBanner;
    @MediaValidation(message = "{error.file.valid}", allowedTypes = {"image/png", "image/jpg", "image/jpeg"})
    private MultipartFile mainEcoProductionFileBanner;
}
