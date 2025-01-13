package org.example.black_sea_walnut.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.black_sea_walnut.enums.MediaType;
import org.example.black_sea_walnut.validator.annotation.MediaValidation;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Getter
@Setter
public class ResponseNewForAdd {
    private Long id;
    private boolean isActive;
    @NotBlank(message = "{error.field.empty}")
    private String titleUA;
    @NotBlank(message = "{error.field.empty}")
    private String titleENG;
    private String descriptionUA;
    private String descriptionENG;
    @NotBlank(message = "{error.field.empty}")
    private String dateOfPublication;
    private String pathToImage;
    @MediaValidation(allowedTypes = {"image/png","image/jpg","image/jpeg","video/mp4"})
    private MultipartFile file;
    private MediaType mediaType;
}