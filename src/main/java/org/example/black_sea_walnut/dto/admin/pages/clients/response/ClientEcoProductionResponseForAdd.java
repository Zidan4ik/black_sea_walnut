package org.example.black_sea_walnut.dto.admin.pages.clients.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ClientEcoProductionResponseForAdd {
    private Long clientsEcoProductionId;
    private Boolean clientsEcoProductionIsActive;
    private String clientsEcoProductionTitleUk;
    private String clientsEcoProductionTitleEn;
    private String clientsEcoProductionDescriptionUk;
    private String clientsEcoProductionDescriptionEn;
    private String clientsEcoProductionPathToBanner;
}
