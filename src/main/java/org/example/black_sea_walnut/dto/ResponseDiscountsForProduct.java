package org.example.black_sea_walnut.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ResponseDiscountsForProduct {
    private Long commonId;
    private String nameUk;
    private String nameEn;
    private Integer price;
}
