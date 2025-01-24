package org.example.black_sea_walnut.dto.pages.factory.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FactoryBannerBlockResponseForAdd {
    private Long factoryBannerId;
    private Boolean factoryBannerIsActive;
    private String factoryBannerPathToImage;
    private String factoryBannerTitleUk;
    private String factoryBannerTitleEn;
    private String factoryBannerDescriptionUk;
    private String factoryBannerDescriptionEn;
}
