package org.example.black_sea_walnut.dto.admin.discount;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DiscountResponseForView {
    private Long id;
    private String name;
    private Long value;
}
