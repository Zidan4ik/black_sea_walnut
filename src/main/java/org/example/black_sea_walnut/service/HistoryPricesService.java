package org.example.black_sea_walnut.service;

import org.example.black_sea_walnut.dto.HistoryRequestPricesForProduct;
import org.example.black_sea_walnut.dto.historyPrice.HistoryResponsePricesForProduct;
import org.example.black_sea_walnut.dto.product.ProductRequestForAdd;
import org.example.black_sea_walnut.entity.HistoryPrices;
import org.example.black_sea_walnut.entity.Product;

public interface HistoryPricesService {
    HistoryPrices getLatestPriceByProductId(Long productId);
    HistoryResponsePricesForProduct getLatestPriceByProductIdInDtoForProduct(Long productId);
    HistoryPrices save(HistoryPrices entity);
    HistoryPrices save(HistoryRequestPricesForProduct dto);
    void deleteAllByProduct(Long productId);
}
