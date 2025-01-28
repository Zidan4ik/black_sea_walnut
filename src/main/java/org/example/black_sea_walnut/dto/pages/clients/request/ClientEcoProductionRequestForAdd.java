package org.example.black_sea_walnut.dto.pages.clients.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Setter;
import org.example.black_sea_walnut.enums.MediaType;
import org.example.black_sea_walnut.validator.annotation.MediaValidation;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ClientEcoProductionRequestForAdd {
    private Long clientsEcoProductionId;
    private Boolean clientsEcoProductionIsActive;
    private MediaType mediaType;
    @NotBlank(message = "{error.field.empty}")
    private String clientsEcoProductionTitleUk;
    @NotBlank(message = "{error.field.empty}")
    private String clientsEcoProductionTitleEn;
    @Size(max = 150, message = "{error.field.valid.size}")
    private String clientsEcoProductionDescriptionUk;
    @Size(max = 150, message = "{error.field.valid.size}")
    private String clientsEcoProductionDescriptionEn;
    @Setter
    private String clientsEcoProductionPathToBanner;
    @MediaValidation(allowedTypes = {"image/png", "image/jpg", "image/jpeg"})
    private MultipartFile clientsEcoProductionFile;
}
