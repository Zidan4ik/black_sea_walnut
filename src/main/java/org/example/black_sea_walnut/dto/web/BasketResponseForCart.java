package org.example.black_sea_walnut.dto.web;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BasketResponseForCart {
    private Long id;
    private String name;
    private Integer mass;
    private Integer amount;
    private int price;
}
