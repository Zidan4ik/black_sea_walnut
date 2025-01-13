package org.example.black_sea_walnut.dto.discount;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DiscountResponseForAdd {
    private Long id;
    private Long discountId;
    private String discountNameUk;
    private String discountNameEn;
    private Long value;
}