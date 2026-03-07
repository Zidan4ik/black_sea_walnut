package org.example.black_sea_walnut.dto.admin.pages.main.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
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

import javax.swing.text.html.parser.Entity;

@Data
public class AimBlockRequestForAdd implements Saveable<History, HistoryMainMapper>, Uploadable, FileProcessable {
    private Long mainAimId;
    private Boolean mainAimIsActive;
    @Setter
    private MediaType mediaType;
    @NotBlank(message = "{error.field.empty}")
    @Length(max = 100, message = "{error.field.valid.length.title}")
    private String mainAimTitleUk;
    @NotBlank(message = "{error.field.empty}")
    @Length(max = 100, message = "{error.field.valid.length.title}")
    private String mainAimTitleEn;
    @Size(max = 150, message = "{error.field.valid.size}")
    private String mainAimDescriptionUk;
    @Size(max = 150, message = "{error.field.valid.size}")
    private String mainAimDescriptionEn;
    @Setter
    private String mainAimPathToBanner;
    @MediaValidation(message = "{error.file.valid}", allowedTypes = {"image/png", "image/jpg", "image/jpeg"})
    private MultipartFile mainAimFileBanner;

    @Override
    public String getSubFolder() {
        return "pages/main/aim-block";
    }

    @Override
    public Long getId() {
        return this.getMainAimId();
    }

    @Override
    public MultipartFile getFileImage() {
        return this.mainAimFileBanner;
    }

    @Override
    public void updateEntity(History entity, HistoryMainMapper mapper) {
        mapper.toEntityFromRequestForAdd(this, entity);
    }

    @Override
    public String getPathToImage() {
        return this.mainAimPathToBanner;
    }

    @Override
    public void setPathToImage(String path) {
        this.mainAimPathToBanner = path;
    }
}
