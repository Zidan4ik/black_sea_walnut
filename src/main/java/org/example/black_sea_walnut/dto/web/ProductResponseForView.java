package org.example.black_sea_walnut.dto.web;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProductResponseForView {
    private Long id;
    private Long articleId;
    private boolean isActive;
    private Long amount;
    private String name;
    private String energyMass;
    private String mass;
    private String recipe;
    private String conditionExploitation;
    private String descriptionProduct;
    private String descriptionPacking;
    private String descriptionPayment;
    private String descriptionDelivery;
    private String pathToImage1;
    private String pathToImage2;
    private String pathToImage3;
    private String pathToImage4;
    private String pathToImageDescription;
    private String pathToImagePacking;
    private String pathToImagePayment;
    private String pathToImageDelivery;
    private String taste;
    private String discount;
    private String newPrice;
    private String oldPrice;
}
