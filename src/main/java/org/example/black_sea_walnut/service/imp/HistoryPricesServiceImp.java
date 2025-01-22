package org.example.black_sea_walnut.service.imp;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.HistoryRequestPricesForProduct;
import org.example.black_sea_walnut.dto.historyPrice.HistoryResponsePricesForProduct;
import org.example.black_sea_walnut.entity.HistoryPrices;
import org.example.black_sea_walnut.mapper.HistoryPricesMapper;
import org.example.black_sea_walnut.repository.HistoryPricesRepository;
import org.example.black_sea_walnut.repository.ProductRepository;
import org.example.black_sea_walnut.service.HistoryPricesService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HistoryPricesServiceImp implements HistoryPricesService {
    private final HistoryPricesRepository historyPricesRepository;
    private final HistoryPricesMapper mapper;

    @Override
    public HistoryPrices getLatestPriceByProductId(Long productId) {
        return historyPricesRepository.findTopByProductInOrderByValidFromDesc(productId);
    }

    @Override
    public HistoryResponsePricesForProduct getLatestPriceByProductIdInDtoForProduct(Long productId) {
        return mapper.toDTOForView(getLatestPriceByProductId(productId));
    }

    @Override
    public HistoryPrices save(HistoryPrices entity) {
        return historyPricesRepository.save(entity);
    }

    @Override
    public HistoryPrices save(HistoryRequestPricesForProduct dto) {
        return save(mapper.toEntityForAdd(dto));
    }

    @Override
    public void deleteAllByProduct(Long productId) {
        historyPricesRepository.deleteAllByProduct(productId);
    }
}
