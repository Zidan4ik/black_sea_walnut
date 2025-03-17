package org.example.black_sea_walnut.dto.admin.new_;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.black_sea_walnut.enums.MediaType;
import org.example.black_sea_walnut.validator.annotation.MediaValidation;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Getter
@Setter
public class NewRequestForAdd {
    private Long id;
    private boolean isActive;
    @NotBlank(message = "{error.field.empty}")
    @Length(max = 100,message = "{error.field.valid.length.title}")
    private String titleUA;
    @NotBlank(message = "{error.field.empty}")
    @Length(max = 100,message = "{error.field.valid.length.title}")
    private String titleENG;
    private String descriptionUA;
    private String descriptionENG;
    @NotBlank(message = "{error.field.empty}")
    private String dateOfPublication;
    private String pathToImage;
    @MediaValidation(message = "{error.file.valid}", allowedTypes = {"image/png", "image/jpg", "image/jpeg", "video/mp4"})
    private MultipartFile file;
    private MediaType mediaType;
}