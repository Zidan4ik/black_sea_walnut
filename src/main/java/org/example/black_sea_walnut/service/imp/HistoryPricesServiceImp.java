package org.example.black_sea_walnut.service.imp;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.HistoryRequestPricesForProduct;
import org.example.black_sea_walnut.dto.admin.historyPrice.HistoryResponsePricesForProduct;
import org.example.black_sea_walnut.entity.HistoryPrices;
import org.example.black_sea_walnut.entity.Product;
import org.example.black_sea_walnut.mapper.HistoryPricesMapper;
import org.example.black_sea_walnut.repository.HistoryPricesRepository;
import org.example.black_sea_walnut.service.HistoryPricesService;
import org.example.black_sea_walnut.util.LogUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryPricesServiceImp implements HistoryPricesService {
    private final HistoryPricesRepository historyPricesRepository;
    private final HistoryPricesMapper mapper;

    @Override
    public HistoryPrices getLatestPriceByProductId(Long productId) {
        LogUtil.logInfo("Fetching latest price for product ID: " + productId);
        try {
            return historyPricesRepository.findTopByProductInOrderByValidFromDesc(productId);
        } catch (Exception e) {
            LogUtil.logError("Error fetching latest price for product ID: " + productId, e);
            throw e;
        }
    }

    @Override
    public HistoryResponsePricesForProduct getLatestPriceByProductIdInDtoForProduct(Long productId) {
        LogUtil.logInfo("Fetching latest price DTO for product ID: " + productId);
        try {
            return mapper.toDTOForView(getLatestPriceByProductId(productId));
        } catch (Exception e) {
            LogUtil.logError("Error fetching latest price DTO for product ID: " + productId, e);
            throw e;
        }
    }

    @Override
    public HistoryPrices save(HistoryPrices entity) {
        LogUtil.logInfo("Saving history price: " + entity);
        try {
            return historyPricesRepository.save(entity);
        } catch (Exception e) {
            LogUtil.logError("Error saving history price: " + entity, e);
            throw e;
        }
    }

    @Override
    public HistoryPrices save(HistoryRequestPricesForProduct dto) {
        LogUtil.logInfo("Saving history price from DTO: " + dto);
        try {
            return save(mapper.toEntityForAdd(dto));
        } catch (Exception e) {
            LogUtil.logError("Error saving history price from DTO: " + dto, e);
            throw e;
        }
    }

    @Override
    public void deleteAllByProduct(Long productId) {
        LogUtil.logInfo("Deleting all history prices for product ID: " + productId);
        try {
            historyPricesRepository.deleteAllByProduct(productId);
        } catch (Exception e) {
            LogUtil.logError("Error deleting history prices for product ID: " + productId, e);
            throw e;
        }
    }

    @Override
    public List<HistoryPrices> getLastTwoDataByProduct(Product product) {
        LogUtil.logInfo("Fetching last two price records for product: " + product);
        try {
            return historyPricesRepository.getLastTwoDataProduct(product);
        } catch (Exception e) {
            LogUtil.logError("Error fetching last two price records for product: " + product, e);
            throw e;
        }
    }
}
