package org.example.black_sea_walnut.dto.admin.pages.main.response;

import lombok.Builder;
import lombok.Getter;
import org.example.black_sea_walnut.service.history.HistoryResponse;

@Builder
@Getter
public class ProductionResponseForAddInMain implements HistoryResponse {
    private Long mainProductionId;
    private boolean mainProductionIsActive;
    private String mainProductionTitleUk;
    private String mainProductionTitleEn;
    private String mainProductionDescriptionUk;
    private String mainProductionDescriptionEn;
}
