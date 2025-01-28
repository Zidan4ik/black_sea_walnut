package org.example.black_sea_walnut.dto.pages.clients.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientBannerResponseForAdd {
    private Long clientsBannerId;
    private Boolean clientsBannerIsActive;
    private String clientsBannerTitleUk;
    private String clientsBannerTitleEn;
    private String clientsBannerDescriptionUk;
    private String clientsBannerDescriptionEn;
    private String clientsBannerPathToBanner;
}
