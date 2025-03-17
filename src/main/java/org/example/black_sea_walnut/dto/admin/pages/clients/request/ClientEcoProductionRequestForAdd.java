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
public class ClientEcoProductionRequestForAdd {
    private Long clientsEcoProductionId;
    private Boolean clientsEcoProductionIsActive;
    private MediaType mediaType;
    @NotBlank(message = "{error.field.empty}")
    @Length(max = 100,message = "{error.field.valid.length.title}")
    private String clientsEcoProductionTitleUk;
    @NotBlank(message = "{error.field.empty}")
    @Length(max = 100,message = "{error.field.valid.length.title}")
    private String clientsEcoProductionTitleEn;
    @Size(max = 150, message = "{error.field.valid.size}")
    private String clientsEcoProductionDescriptionUk;
    @Size(max = 150, message = "{error.field.valid.size}")
    private String clientsEcoProductionDescriptionEn;
    @Setter
    private String clientsEcoProductionPathToBanner;
    @MediaValidation(message = "{error.file.valid}", allowedTypes = {"image/png", "image/jpg", "image/jpeg"})
    private MultipartFile clientsEcoProductionFile;
}
