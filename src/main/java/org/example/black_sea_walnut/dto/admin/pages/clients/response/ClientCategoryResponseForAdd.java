package org.example.black_sea_walnut.dto.admin.pages.clients.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ClientCategoryResponseForAdd {
    private Long clientCategoryId;
    private Boolean clientCategoryIsActive;
    private String clientCategoryTitleUk;
    private String clientCategoryTitleEn;
    private String clientCategorySubtitleUk;
    private String clientCategorySubtitleEn;
    private String clientCategoryDescriptionUk;
    private String clientCategoryDescriptionEn;
    private String clientCategoryPathToImage;
    private String clientCategoryPathToSvg;
}
