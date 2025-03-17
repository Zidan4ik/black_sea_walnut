package org.example.black_sea_walnut.dto.web;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BasketResponseForCart {
    private Long id;
    private Long articleProduct;
    private String name;
    private Integer mass;
    private Integer amount;
    private int price;
}
