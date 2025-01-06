package org.example.black_sea_walnut.dto.discount;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ResponseDiscountForView {
    private Long id;
    private String name;
    private Long value;
}
