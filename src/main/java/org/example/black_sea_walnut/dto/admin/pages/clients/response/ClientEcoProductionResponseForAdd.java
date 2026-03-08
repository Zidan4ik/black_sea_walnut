package org.example.black_sea_walnut.dto.admin.pages.clients.response;

import lombok.Builder;
import lombok.Getter;
import org.example.black_sea_walnut.service.history.HistoryResponse;

@Builder
@Getter
public class ClientEcoProductionResponseForAdd implements HistoryResponse {
    private Long clientsEcoProductionId;
    private Boolean clientsEcoProductionIsActive;
    private String clientsEcoProductionTitleUk;
    private String clientsEcoProductionTitleEn;
    private String clientsEcoProductionDescriptionUk;
    private String clientsEcoProductionDescriptionEn;
    private String clientsEcoProductionPathToBanner;
}
