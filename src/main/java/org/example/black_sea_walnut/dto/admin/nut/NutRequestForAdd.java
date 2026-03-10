package org.example.black_sea_walnut.dto.admin.nut;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.black_sea_walnut.entity.Nut;
import org.example.black_sea_walnut.mapper.NutMapper;
import org.example.black_sea_walnut.service.Uploadable;
import org.example.black_sea_walnut.service.file.FileProcessable;
import org.example.black_sea_walnut.service.user.Saveable;
import org.example.black_sea_walnut.validator.annotation.MediaValidation;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Getter
public class NutRequestForAdd implements Saveable<Nut, NutMapper>, Uploadable, FileProcessable {
    private Long id;
    private boolean isActive;
    @NotBlank(message = "{error.field.empty}")
    @Length(max = 100,message = "{error.field.valid.length.title}")
    private String titleUk;
    @NotBlank(message = "{error.field.empty}")
    @Length(max = 100,message = "{error.field.valid.length.title}")
    private String titleEn;
    private String descriptionUk;
    private String descriptionEn;
    @Setter
    private String pathToImage;
    @Setter
    private String pathToSvg;
    @MediaValidation(message = "{error.file.valid}", allowedTypes = {"image/png", "image/jpg", "image/jpeg"})
    private MultipartFile fileImage;
    @MediaValidation(message = "{error.file.valid}", allowedTypes = {"image/png", "image/jpg", "image/jpeg","image/svg+xml"})
    private MultipartFile fileSvg;

    @Override
    public String getSubFolder() {
        return "nuts";
    }

    @Override
    public void updateEntity(Nut entity, NutMapper mapper) {
        mapper.toEntityFromRequestAdd(this,entity);
    }
}
