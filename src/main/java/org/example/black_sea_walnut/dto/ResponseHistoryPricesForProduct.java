package org.example.black_sea_walnut.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ResponseHistoryPricesForProduct {
    private Long id;
    private String newPrice;
    private String oldPrice;
}
