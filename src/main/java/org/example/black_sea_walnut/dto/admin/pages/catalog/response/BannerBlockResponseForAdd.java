package org.example.black_sea_walnut.dto.admin.pages.catalog.response;

import lombok.Builder;
import lombok.Getter;
import org.example.black_sea_walnut.service.history.HistoryResponse;

@Builder
@Getter
public class BannerBlockResponseForAdd implements HistoryResponse {
    private Long catalogBannerId;
    private Boolean catalogBannerIsActive;
    private String catalogBannerPathToImage;
}
