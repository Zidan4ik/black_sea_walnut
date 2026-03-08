package org.example.black_sea_walnut.dto.admin.pages.factory.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Setter;
import org.example.black_sea_walnut.entity.History;
import org.example.black_sea_walnut.enums.MediaType;
import org.example.black_sea_walnut.mapper.pages.HistoryFactoryMapper;
import org.example.black_sea_walnut.service.Uploadable;
import org.example.black_sea_walnut.service.file.FileProcessable;
import org.example.black_sea_walnut.service.user.Saveable;
import org.example.black_sea_walnut.validator.annotation.MediaValidation;
import org.springframework.web.multipart.MultipartFile;

@Data
public class BannerBlockRequestForAdd implements Saveable<History, HistoryFactoryMapper>, Uploadable, FileProcessable {
    private Long factoryBannerId;
    private Boolean factoryBannerIsActive;
    @Setter
    private MediaType mediaType;
    @NotBlank(message = "{error.field.empty}")
    private String factoryBannerTitleUk;
    @NotBlank(message = "{error.field.empty}")
    private String factoryBannerTitleEn;
    @Size(max = 150, message = "{error.field.valid.size}")
    private String factoryBannerDescriptionUk;
    @Size(max = 150, message = "{error.field.valid.size}")
    private String factoryBannerDescriptionEn;
    @Setter
    private String factoryBannerPathToBanner;
    @MediaValidation(message = "{error.file.valid}", allowedTypes = {"image/png", "image/jpg", "image/jpeg"})
    private MultipartFile factoryBannerFile;

    @Override
    public void updateEntity(History entity, HistoryFactoryMapper mapper) {
        mapper.toEntityFromRequestBannerBlock(this,entity);
    }

    @Override
    public String getSubFolder() {
        return "pages/factory/banner-block";
    }

    @Override
    public MultipartFile getFileImage() {
        return this.getFactoryBannerFile();
    }

    @Override
    public String getPathToImage() {
        return this.getFactoryBannerPathToBanner();
    }

    @Override
    public void setPathToImage(String path) {
        this.setFactoryBannerPathToBanner(path);
    }

    @Override
    public Long getId() {
        return this.getFactoryBannerId();
    }
}
