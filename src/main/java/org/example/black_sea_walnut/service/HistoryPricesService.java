package org.example.black_sea_walnut.service;

import org.example.black_sea_walnut.dto.ResponseHistoryPricesForProduct;
import org.example.black_sea_walnut.entity.HistoryPrices;

public interface HistoryPricesService {
    HistoryPrices getLatestPriceByProductId(Long productId);
    ResponseHistoryPricesForProduct getLatestPriceByProductIdInDtoForProduct(Long productId);
}
