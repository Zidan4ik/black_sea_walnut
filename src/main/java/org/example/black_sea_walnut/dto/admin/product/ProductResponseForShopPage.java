package org.example.black_sea_walnut.dto.admin.product;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProductResponseForShopPage {
    private String massFilter;
    private String tasteFilter;
    private String directionCost;
}
