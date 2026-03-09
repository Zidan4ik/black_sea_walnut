package org.example.black_sea_walnut.dto.admin.pages.main.response;

import lombok.Builder;
import lombok.Getter;
import org.example.black_sea_walnut.service.history.DtoResponse;

@Builder
@Getter
public class AimBlockResponseForAddInMain implements DtoResponse {
    private Long mainAimId;
    private boolean mainAimIsActive;
    private String mainAimTitleUk;
    private String mainAimTitleEn;
    private String mainAimDescriptionUk;
    private String mainAimDescriptionEn;
    private String mainAimPathToBanner;
}
