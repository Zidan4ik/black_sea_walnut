package org.example.black_sea_walnut.dto.discount;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DiscountResponseForAdd {
    private Long discountIdUk;
    private Long discountIdEn;
    private Long discountCommonId;
    private String nameUk2;
    private String nameEn2;
    private Long value;
}