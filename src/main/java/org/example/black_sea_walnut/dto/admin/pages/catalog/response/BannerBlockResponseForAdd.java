package org.example.black_sea_walnut.dto.admin.pages.catalog.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BannerBlockResponseForAdd {
    private Long catalogBannerId;
    private Boolean catalogBannerIsActive;
    private String catalogBannerPathToImage;
}
