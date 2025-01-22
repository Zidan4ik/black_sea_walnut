package org.example.black_sea_walnut.dto.pages.main.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class EcoProductionResponseForAdd {
    private Long mainEcoProductionId;
    private boolean mainEcoProductionIsActive;
    private String mainEcoProductionTitleUk;
    private String mainEcoProductionTitleEn;
    private String mainEcoProductionDescriptionUk;
    private String mainEcoProductionDescriptionEn;
    private String mainEcoProductionPathToBanner;
}