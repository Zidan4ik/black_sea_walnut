package org.example.black_sea_walnut.dto.admin.pages.main.response;

import lombok.Builder;
import lombok.Getter;
import org.example.black_sea_walnut.service.history.HistoryResponse;

@Builder
@Getter
public class BlockResponseForAddInMain implements HistoryResponse {
    private Long mainId;
    private boolean mainIsActive;
    private String mainTitleUk;
    private String mainTitleEn;
    private String mainDescriptionUk;
    private String mainDescriptionEn;
    private String mainPathToBanner;
}
