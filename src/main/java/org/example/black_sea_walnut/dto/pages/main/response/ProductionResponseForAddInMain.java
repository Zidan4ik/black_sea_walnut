package org.example.black_sea_walnut.dto.pages.main.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProductionResponseForAddInMain {
    private Long mainProductionId;
    private boolean mainProductionIsActive;
    private String mainProductionTitleUk;
    private String mainProductionTitleEn;
    private String mainProductionDescriptionUk;
    private String mainProductionDescriptionEn;
}
