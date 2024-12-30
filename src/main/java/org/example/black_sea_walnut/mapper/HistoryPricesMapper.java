package org.example.black_sea_walnut.mapper;

import org.example.black_sea_walnut.dto.ResponseHistoryPricesForProduct;
import org.example.black_sea_walnut.entity.HistoryPrices;

public class HistoryPricesMapper {
    public ResponseHistoryPricesForProduct toDTOForView(HistoryPrices entity) {
        return ResponseHistoryPricesForProduct
                .builder()
                .id(entity.getId())
                .newPrice(String.valueOf(entity.getNewPrice()))
                .oldPrice(String.valueOf(entity.getOldPrice()))
                .build();
    }
}
