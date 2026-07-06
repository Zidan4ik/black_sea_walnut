package org.example.black_sea_walnut.service.product;

import org.example.black_sea_walnut.dto.admin.historyPrice.HistoryResponsePricesForProduct;

public interface PricedResponse {
    void setPrices(HistoryResponsePricesForProduct prices);
}
