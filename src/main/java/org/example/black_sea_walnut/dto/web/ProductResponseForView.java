package org.example.black_sea_walnut.dto.web;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProductResponseForView {
    private Long id;
    private Long articleId;
    private String name;
    private String taste;
    private int mass;
    private int priceOld;
    private int priceNew;
    private String discount;
    private String pathToImage1;
    private String pathToImage2;
    private String pathToImage3;
    private String pathToImage4;
}
