package org.example.black_sea_walnut.dto.product;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.black_sea_walnut.dto.HistoryRequestPricesForProduct;

@Builder
@Getter
public class ProductResponseForAdd {
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
    private String pathToImage1;
    private String pathToImage2;
    private String pathToImage3;
    private String pathToImage4;
    private String pathToImageDescription;
    private String pathToImagePacking;
    private String pathToImagePayment;
    private String pathToImageDelivery;
    @Setter
    private HistoryRequestPricesForProduct prices;
    @Setter
    private Long tasteId;
    @Setter
    private Long discountId;
}
