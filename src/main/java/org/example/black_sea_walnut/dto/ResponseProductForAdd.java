package org.example.black_sea_walnut.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
public class ResponseProductForAdd {
    private Long id;
    private Long articleId;
    private Boolean isActive;
    private String amount;
    private String nameUk;
    private String nameEn;
    private String energyMass;
    private String mass;
    private String conditionExploitationUk;
    private String conditionExploitationEn;
    private String descriptionProductUk;
    private String descriptionProductEn;
    private String descriptionPackingUk;
    private String descriptionPackingEn;
    private String descriptionPaymentUk;
    private String descriptionPaymentEn;
    private String descriptionDeliveryUk;
    private String descriptionDeliveryEn;
    @Setter
    private ResponseHistoryPricesForProduct prices;
    @Setter
    private ResponseTastesForProduct taste;
    @Setter
    private ResponseDiscountsForProduct discount;
}
