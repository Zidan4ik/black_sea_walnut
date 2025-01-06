package org.example.black_sea_walnut.mapper;

import org.example.black_sea_walnut.dto.HistoryRequestPricesForProduct;
import org.example.black_sea_walnut.dto.historyPrice.HistoryResponsePricesForProduct;
import org.example.black_sea_walnut.entity.HistoryPrices;
import org.example.black_sea_walnut.entity.Product;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class HistoryPricesMapper {
    public HistoryResponsePricesForProduct toDTOForView(HistoryPrices entity) {
        return HistoryResponsePricesForProduct
                .builder()
                .id(entity.getId())
                .newPrice(String.valueOf(entity.getNewPrice()))
                .oldPrice(String.valueOf(entity.getOldPrice()))
                .build();
    }

    public HistoryPrices toEntityForAdd(HistoryRequestPricesForProduct dto) {
        HistoryPrices entity = new HistoryPrices();
        entity.setId(dto.getId());
        entity.setOldPrice(dto.getOldPrice().intValue());
        entity.setNewPrice(dto.getNewPrice().intValue());
        entity.setValidFrom(LocalDateTime.now());
        entity.setValidTo(LocalDateTime.now().plusDays(30));
        entity.setProduct(Product.builder().id(dto.getProductId()).build());
        return entity;
    }
}
