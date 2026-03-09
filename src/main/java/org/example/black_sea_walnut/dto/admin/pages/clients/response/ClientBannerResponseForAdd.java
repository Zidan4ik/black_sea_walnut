package org.example.black_sea_walnut.dto.admin.pages.clients.response;

import lombok.Builder;
import lombok.Data;
import org.example.black_sea_walnut.service.history.DtoResponse;

@Data
@Builder
public class ClientBannerResponseForAdd implements DtoResponse {
    private Long clientsBannerId;
    private Boolean clientsBannerIsActive;
    private String clientsBannerTitleUk;
    private String clientsBannerTitleEn;
    private String clientsBannerDescriptionUk;
    private String clientsBannerDescriptionEn;
    private String clientsBannerPathToBanner;
}
