package org.example.black_sea_walnut.dto.admin.pages.catalog.request;

import lombok.Getter;
import lombok.Setter;
import org.example.black_sea_walnut.entity.History;
import org.example.black_sea_walnut.enums.MediaType;
import org.example.black_sea_walnut.mapper.pages.HistoryCatalogMapper;
import org.example.black_sea_walnut.service.Uploadable;
import org.example.black_sea_walnut.service.file.FileProcessable;
import org.example.black_sea_walnut.service.user.Saveable;
import org.example.black_sea_walnut.validator.annotation.MediaValidation;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class BannerBlockRequestForAdd implements Saveable<History, HistoryCatalogMapper>, Uploadable, FileProcessable {
    private Long catalogBannerId;
    private Boolean catalogBannerIsActive;
    private String catalogBannerPathToImage;
    private MediaType mediaType;
    @MediaValidation(message = "{error.file.valid}", allowedTypes = {"image/png", "image/jpg", "image/jpeg"})
    private MultipartFile catalogBannerFile;

    @Override
    public Long getId() {
        return this.getCatalogBannerId();
    }

    @Override
    public void updateEntity(History entity, HistoryCatalogMapper mapper) {
        mapper.toEntityFromRequestBannerBlock(this,entity);
    }

    @Override
    public String getSubFolder() {
        return "pages/catalog/banner-block";
    }

    @Override
    public MultipartFile getFileImage() {
        return this.getCatalogBannerFile();
    }

    @Override
    public String getPathToImage() {
        return this.getCatalogBannerPathToImage();
    }

    @Override
    public void setPathToImage(String path) {
        setCatalogBannerPathToImage(path);
    }
}
