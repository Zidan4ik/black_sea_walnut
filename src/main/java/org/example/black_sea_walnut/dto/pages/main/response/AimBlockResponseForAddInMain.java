package org.example.black_sea_walnut.dto.pages.main.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AimBlockResponseForAddInMain {
    private Long mainAimId;
    private boolean mainAimIsActive;
    private String mainAimTitleUk;
    private String mainAimTitleEn;
    private String mainAimDescriptionUk;
    private String mainAimDescriptionEn;
    private String mainAimPathToBanner;
}
