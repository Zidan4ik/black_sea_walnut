package org.example.black_sea_walnut.dto.admin.pages.clients.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Setter;
import org.example.black_sea_walnut.entity.History;
import org.example.black_sea_walnut.enums.MediaType;
import org.example.black_sea_walnut.mapper.pages.HistoryClientsMapper;
import org.example.black_sea_walnut.service.Uploadable;
import org.example.black_sea_walnut.service.file.FileProcessable;
import org.example.black_sea_walnut.service.user.Saveable;
import org.example.black_sea_walnut.validator.annotation.MediaValidation;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ClientBannerRequestForAdd implements Saveable<History, HistoryClientsMapper>, Uploadable, FileProcessable {
    private Long clientsBannerId;
    private Boolean clientsBannerIsActive;
    private MediaType mediaType;
    @NotBlank(message = "{error.field.empty}")
    private String clientsBannerTitleUk;
    @NotBlank(message = "{error.field.empty}")
    private String clientsBannerTitleEn;
    @Size(max = 150, message = "{error.field.valid.size}")
    private String clientsBannerDescriptionUk;
    @Size(max = 150, message = "{error.field.valid.size}")
    private String clientsBannerDescriptionEn;
    @Setter
    private String clientsBannerPathToBanner;
    @MediaValidation(message = "{error.file.valid}", allowedTypes = {"image/png", "image/jpg", "image/jpeg"})
    private MultipartFile clientsBannerFile;

    @Override
    public void updateEntity(History entity, HistoryClientsMapper mapper) {
        mapper.toEntityFromRequestBannerBlock(this,entity);
    }

    @Override
    public String getSubFolder() {
        return "pages/clients/banner-block";
    }

    @Override
    public MultipartFile getFileImage() {
        return this.getClientsBannerFile();
    }

    @Override
    public String getPathToImage() {
        return this.getClientsBannerPathToBanner();
    }

    @Override
    public void setPathToImage(String path) {
        this.setClientsBannerPathToBanner(path);
    }

    @Override
    public Long getId() {
        return this.getClientsBannerId();
    }
}