package org.example.black_sea_walnut.dto.admin.pages.factory.response;

import lombok.Builder;
import lombok.Getter;
import org.example.black_sea_walnut.service.history.DtoResponse;

@Builder
@Getter
public class FactoryBannerBlockResponseForAdd implements DtoResponse {
    private Long factoryBannerId;
    private Boolean factoryBannerIsActive;
    private String factoryBannerPathToImage;
    private String factoryBannerTitleUk;
    private String factoryBannerTitleEn;
    private String factoryBannerDescriptionUk;
    private String factoryBannerDescriptionEn;
}
