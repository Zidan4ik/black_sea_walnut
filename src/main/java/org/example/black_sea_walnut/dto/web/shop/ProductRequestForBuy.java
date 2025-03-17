package org.example.black_sea_walnut.dto.web.shop;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProductRequestForBuy {
    private Long articleId;
    private Integer count;
}
