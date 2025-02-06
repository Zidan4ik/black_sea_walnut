package org.example.black_sea_walnut.dto.pages.catalog.request;

import lombok.Getter;
import lombok.Setter;
import org.example.black_sea_walnut.enums.MediaType;
import org.example.black_sea_walnut.validator.annotation.MediaValidation;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class BannerBlockRequestForAdd {
    private Long catalogBannerId;
    private Boolean catalogBannerIsActive;
    private String catalogBannerPathToImage;
    private MediaType mediaType;
    @MediaValidation(message = "{error.file.valid}", allowedTypes = {"image/png", "image/jpg", "image/jpeg"})
    private MultipartFile catalogBannerFile;
}
