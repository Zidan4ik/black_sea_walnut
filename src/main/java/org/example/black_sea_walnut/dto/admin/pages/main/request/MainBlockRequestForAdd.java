package org.example.black_sea_walnut.dto.admin.pages.main.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
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
public class MainBlockRequestForAdd implements Saveable<History, HistoryMainMapper>, Uploadable, FileProcessable {
    private Long mainId;
    private Boolean mainIsActive;
    @Setter
    private MediaType mediaType;
    @NotBlank(message = "{error.field.empty}")
    @Length(max = 100,message = "{error.field.valid.length.title}")
    private String mainTitleUk;
    @NotBlank(message = "{error.field.empty}")
    @Length(max = 100,message = "{error.field.valid.length.title}")
    private String mainTitleEn;
    @Size(max = 150, message = "{error.field.valid.size}")
    private String mainDescriptionUk;
    @Size(max = 150, message = "{error.field.valid.size}")
    private String mainDescriptionEn;
    @Setter
    private String mainPathToBanner;

    @Override
    public void updateEntity(History entity, HistoryMainMapper mapper) {
        mapper.toEntityFromRequestForAdd(this,entity);
    }

    @MediaValidation(message = "{error.file.valid}", allowedTypes = {"image/png", "image/jpg", "image/jpeg"})
    private MultipartFile mainFileBanner;

    @Override
    public String getSubFolder() {
        return "pages/main/main-block";
    }

    @Override
    public MultipartFile getFileImage() {
        return this.mainFileBanner;
    }

    @Override
    public String getPathToImage() {
        return this.mainPathToBanner;
    }

    @Override
    public void setPathToImage(String path) {
        this.setMainPathToBanner(path);
    }

    @Override
    public Long getId() {
        return this.getMainId();
    }
}