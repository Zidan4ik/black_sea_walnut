package org.example.black_sea_walnut.service.imp;

import org.example.black_sea_walnut.dto.HistoryRequestPricesForProduct;
import org.example.black_sea_walnut.dto.admin.historyPrice.HistoryResponsePricesForProduct;
import org.example.black_sea_walnut.entity.HistoryPrices;
import org.example.black_sea_walnut.entity.Product;
import org.example.black_sea_walnut.mapper.HistoryPricesMapper;
import org.example.black_sea_walnut.repository.HistoryPricesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistoryPricesServiceImpTest {

    @Mock
    private HistoryPricesRepository historyPricesRepository;

    @Mock
    private HistoryPricesMapper mapper;

    @InjectMocks
    private HistoryPricesServiceImp historyPricesService;

    private HistoryPrices historyPrices;
    private Product product;
    private HistoryRequestPricesForProduct historyRequestPricesForProduct;
    private HistoryResponsePricesForProduct historyResponsePricesForProduct;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);

        historyPrices = new HistoryPrices();
        historyPrices.setId(1L);
        historyPrices.setProduct(product);

        historyRequestPricesForProduct = new HistoryRequestPricesForProduct();

        historyResponsePricesForProduct = HistoryResponsePricesForProduct.builder().build();
    }

    @Test
    void getLatestPriceByProductId_ShouldReturnLatestPrice() {
        when(historyPricesRepository.findTopByProductInOrderByValidFromDesc(1L)).thenReturn(historyPrices);

        HistoryPrices result = historyPricesService.getLatestPriceByProductId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(historyPricesRepository).findTopByProductInOrderByValidFromDesc(1L);
    }

    @Test
    void getLatestPriceByProductIdInDtoForProduct_ShouldReturnDto() {
        when(historyPricesRepository.findTopByProductInOrderByValidFromDesc(1L)).thenReturn(historyPrices);
        when(mapper.toDTOForView(historyPrices)).thenReturn(historyResponsePricesForProduct);

        HistoryResponsePricesForProduct result = historyPricesService.getLatestPriceByProductIdInDtoForProduct(1L);

        assertNotNull(result);
        verify(mapper).toDTOForView(historyPrices);
    }

    @Test
    void getLatestPriceByProductIdInDtoForProduct_ShouldThrowException() {
        Long productId = 1L;
        when(historyPricesRepository.findTopByProductInOrderByValidFromDesc(productId))
                .thenThrow(new RuntimeException("Failed to fetch data from db"));
        Exception exception = assertThrows(RuntimeException.class,
                () -> historyPricesService.getLatestPriceByProductIdInDtoForProduct(productId));
        assertEquals("Failed to fetch data from db", exception.getMessage());
        verify(mapper, never()).toDTOForView(any());
    }

    @Test
    void save_ShouldThrowException() {
        Long productId = 1L;
        when(historyPricesRepository.findTopByProductInOrderByValidFromDesc(productId))
                .thenThrow(new RuntimeException("Failed to fetch data from db"));
        Exception exception = assertThrows(RuntimeException.class,
                () -> historyPricesService.getLatestPriceByProductIdInDtoForProduct(productId));
        assertEquals("Failed to fetch data from db", exception.getMessage());
        verify(mapper, never()).toDTOForView(any());
    }

    @Test
    void saveHistoryRequestPricesForProduct_ShouldThrowException() {
        when(historyPricesService.save(any(HistoryRequestPricesForProduct.class)))
                .thenThrow(new RuntimeException("Failed to fetch data from db"));
        Exception exception = assertThrows(RuntimeException.class,
                () -> historyPricesService.save(any(HistoryRequestPricesForProduct.class)));
    }

    @Test
    void getLastTwoDataByProduct_ShouldThrowException() {
        when(historyPricesRepository.getLastTwoDataProduct(any(Product.class)))
                .thenThrow(new RuntimeException("Failed to fetch data from db"));
        Exception exception = assertThrows(RuntimeException.class,
                () -> historyPricesService.getLastTwoDataByProduct(any(Product.class)));
    }

    @Test
    void deleteAllByProduct_ShouldThrowException() {
        Long productId = 1L;
        doThrow(new RuntimeException("Failed to delete data from db"))
                .when(historyPricesRepository).deleteAllByProduct(productId);
        Exception exception = assertThrows(RuntimeException.class,
                () -> historyPricesService.deleteAllByProduct(productId));
        assertEquals("Failed to delete data from db", exception.getMessage());
        verify(historyPricesRepository, times(1)).deleteAllByProduct(productId);
    }

    @Test
    void save_ShouldSaveAndReturnEntity() {
        when(historyPricesRepository.save(historyPrices)).thenReturn(historyPrices);

        HistoryPrices result = historyPricesService.save(historyPrices);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(historyPricesRepository).save(historyPrices);
    }

    @Test
    void save_ShouldConvertAndSaveEntityFromDto() {
        when(mapper.toEntityForAdd(historyRequestPricesForProduct)).thenReturn(historyPrices);
        when(historyPricesRepository.save(historyPrices)).thenReturn(historyPrices);

        HistoryPrices result = historyPricesService.save(historyRequestPricesForProduct);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(mapper).toEntityForAdd(historyRequestPricesForProduct);
        verify(historyPricesRepository).save(historyPrices);
    }

    @Test
    void deleteAllByProduct_ShouldDeletePrices() {
        doNothing().when(historyPricesRepository).deleteAllByProduct(1L);

        historyPricesService.deleteAllByProduct(1L);

        verify(historyPricesRepository).deleteAllByProduct(1L);
    }

    @Test
    void getLastTwoDataByProduct_ShouldReturnList() {
        when(historyPricesRepository.getLastTwoDataProduct(product)).thenReturn(List.of(historyPrices));

        List<HistoryPrices> result = historyPricesService.getLastTwoDataByProduct(product);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(historyPricesRepository).getLastTwoDataProduct(product);
    }
}