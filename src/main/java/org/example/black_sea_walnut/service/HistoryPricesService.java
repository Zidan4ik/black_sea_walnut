package org.example.black_sea_walnut.service;

import org.example.black_sea_walnut.dto.HistoryRequestPricesForProduct;
import org.example.black_sea_walnut.dto.historyPrice.HistoryResponsePricesForProduct;
import org.example.black_sea_walnut.entity.HistoryPrices;

public interface HistoryPricesService {
    HistoryPrices getLatestPriceByProductId(Long productId);
    HistoryResponsePricesForProduct getLatestPriceByProductIdInDtoForProduct(Long productId);
}
