package org.example.black_sea_walnut.dto.admin.pages.main.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.black_sea_walnut.entity.History;
import org.example.black_sea_walnut.enums.MediaType;
import org.example.black_sea_walnut.mapper.pages.HistoryMainMapper;
import org.example.black_sea_walnut.service.Uploadable;
import org.example.black_sea_walnut.service.file.FileProcessable;
import org.example.black_sea_walnut.service.user.Saveable;
import org.example.black_sea_walnut.validator.annotation.MediaValidation;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Getter
public class EcoProductionRequestForAdd implements Saveable<History, HistoryMainMapper>, Uploadable, FileProcessable {
    private Long mainEcoProductionId;
    private Boolean mainEcoProductionIsActive;
    @Setter
    private MediaType mediaType;
    @NotBlank(message = "{error.field.empty}")
    @Length(max = 100, message = "{error.field.valid.length.title}")
    private String mainEcoProductionTitleUk;
    @NotBlank(message = "{error.field.empty}")
    @Length(max = 100, message = "{error.field.valid.length.title}")
    private String mainEcoProductionTitleEn;
    @Size(max = 150, message = "{error.field.valid.size}")
    private String mainEcoProductionDescriptionUk;
    @Size(max = 150, message = "{error.field.valid.size}")
    private String mainEcoProductionDescriptionEn;
    @Setter
    private String mainEcoProductionPathToBanner;
    @MediaValidation(message = "{error.file.valid}", allowedTypes = {"image/png", "image/jpg", "image/jpeg"})
    private MultipartFile mainEcoProductionFileBanner;

    @Override
    public String getSubFolder() {
        return "pages/main/eco-production-block";
    }

    @Override
    public Long getId() {
        return this.getMainEcoProductionId();
    }

    @Override
    public void updateEntity(History entity, HistoryMainMapper mapper) {
        mapper.toEntityFromRequestForAdd(this, entity);
    }

    @Override
    public MultipartFile getFileImage() {
        return this.mainEcoProductionFileBanner;
    }

    @Override
    public String getPathToImage() {
        return this.mainEcoProductionPathToBanner;
    }

    @Override
    public void setPathToImage(String path) {
        this.mainEcoProductionPathToBanner = path;
    }
}
