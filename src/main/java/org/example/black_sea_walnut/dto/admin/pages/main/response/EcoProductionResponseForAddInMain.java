package org.example.black_sea_walnut.dto.admin.pages.main.response;

import lombok.Builder;
import lombok.Getter;
import org.example.black_sea_walnut.service.history.HistoryResponse;

@Builder
@Getter
public class EcoProductionResponseForAddInMain implements HistoryResponse {
    private Long mainEcoProductionId;
    private boolean mainEcoProductionIsActive;
    private String mainEcoProductionTitleUk;
    private String mainEcoProductionTitleEn;
    private String mainEcoProductionDescriptionUk;
    private String mainEcoProductionDescriptionEn;
    private String mainEcoProductionPathToBanner;
}