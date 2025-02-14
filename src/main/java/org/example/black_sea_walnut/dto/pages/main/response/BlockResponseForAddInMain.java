package org.example.black_sea_walnut.dto.pages.main.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BlockResponseForAddInMain {
    private Long mainId;
    private boolean mainIsActive;
    private String mainTitleUk;
    private String mainTitleEn;
    private String mainDescriptionUk;
    private String mainDescriptionEn;
    private String mainPathToBanner;
}
