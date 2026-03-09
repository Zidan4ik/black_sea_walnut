package org.example.black_sea_walnut.dto.admin.pages.catalog.response;

import lombok.Builder;
import lombok.Getter;
import org.example.black_sea_walnut.service.history.DtoResponse;

@Builder
@Getter
public class BannerBlockResponseForAdd implements DtoResponse {
    private Long catalogBannerId;
    private Boolean catalogBannerIsActive;
    private String catalogBannerPathToImage;
}
