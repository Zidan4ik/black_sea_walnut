package org.example.black_sea_walnut.service.imp;

import org.example.black_sea_walnut.dto.web.BasketResponseForCart;
import org.example.black_sea_walnut.entity.Basket;
import org.example.black_sea_walnut.entity.Discount;
import org.example.black_sea_walnut.entity.Product;
import org.example.black_sea_walnut.entity.User;
import org.example.black_sea_walnut.entity.translation.ProductTranslation;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.exception.InsufficientStockException;
import org.example.black_sea_walnut.mapper.BasketMapper;
import org.example.black_sea_walnut.repository.BasketRepository;
import org.example.black_sea_walnut.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BasketServiceImpTest {

    @Mock
    private BasketRepository basketRepository;

    @Mock
    private BasketMapper basketMapper;

    @Mock
    private ProductService productService;

    @InjectMocks
    private BasketServiceImp basketService;

    private Basket basket;
    private Product product;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        product = new Product();
        product.setId(1L);
        product.setTotalCount(10L);
        product.setPriceByUnit("10");

        basket = new Basket();
        basket.setId(1L);
        basket.setUser(user);
        basket.setProducts(Collections.singletonList(product));
        basket.setCount(2);
    }

    @Test
    void getAll_ReturnsList() {
        when(basketRepository.findAll()).thenReturn(Collections.singletonList(basket));
        List<Basket> result = basketService.getAll();
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(basketRepository, times(1)).findAll();
    }

    @Test
    void getAllInResponseForCart_ReturnsList() {
        when(basketRepository.getAllByUser(user)).thenReturn(Collections.singletonList(basket));
        List<BasketResponseForCart> result = basketService.getAllInResponseForCart(user,LanguageCode.en);
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(basketRepository, times(1)).getAllByUser(user);
    }

    @Test
    void getById_ReturnsBasket() {
        when(basketRepository.findById(1L)).thenReturn(Optional.of(basket));
        Basket result = basketService.getById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(basketRepository, times(1)).findById(1L);
    }

    @Test
    void getById_ThrowsException_WhenNotFound() {
        when(basketRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> basketService.getById(2L));
    }

    @Test
    void decreaseAmountById_DeletesBasket_WhenCountIsOne() {
        basket.setCount(1);
        when(basketRepository.findById(1L)).thenReturn(Optional.of(basket));
        basketService.decreaseAmountById(1L);
        verify(basketRepository, times(1)).deleteById(1L);
    }

    @Test
    void decreaseAmountById_WithoutDiscount_WhenCountIsMoreThanOne() {
        basket.setCount(2);
        Discount dis = new Discount();
        dis.setValue(1);
        product.setDiscounts(Set.of(dis));
        when(basketRepository.findById(1L)).thenReturn(Optional.of(basket));
        basketService.decreaseAmountById(1L);
    }


    @Test
    void decreaseAmountById_DeletesBasket_WhenCountIsMoreThanOne() {
        basket.setCount(2);
        when(basketRepository.findById(1L)).thenReturn(Optional.of(basket));
        basketService.decreaseAmountById(1L);
    }

    @Test
    void increaseAmountById_IncreasesCount_WhenStockAvailable() {
        when(basketRepository.findById(1L)).thenReturn(Optional.of(basket));
        basketService.increaseAmountById(1L);
        assertEquals(3, basket.getCount());
        verify(productService, times(1)).decreaseCountItems(1L);
    }

    @Test
    void increaseAmountById_ThrowsException_WhenNoStock() {
        product.setTotalCount(0L);
        when(basketRepository.findById(1L)).thenReturn(Optional.of(basket));
        assertThrows(InsufficientStockException.class, () -> basketService.increaseAmountById(1L));
    }

    @Test
    void deleteById_CallsRepository() {
        basketService.deleteById(1L);
        verify(basketRepository, times(1)).deleteById(1L);
    }

    @Test
    void saveCountProduct_ShouldUpdateCount_WhenValidValueProvided() {
        when(basketRepository.findById(1L)).thenReturn(Optional.of(basket));
        when(productService.getById(1L)).thenReturn(product);
        basketService.saveCountProduct(1L, 5);
        assertEquals(5, basket.getCount(), "Basket count should be updated");
        assertEquals(7, product.getTotalCount(), "Total count should be reduced");
        verify(basketRepository, times(1)).save(any(Basket.class));
    }

    @Test
    void saveCountProduct_ShouldThrowException_WhenNegativeValueProvided() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                basketService.saveCountProduct(1L, -1));
        assertEquals("The value must not be negative!", exception.getMessage());
    }

    @Test
    void saveCountProduct_ShouldThrowException_WhenInsufficientStock() {
        when(basketRepository.findById(1L)).thenReturn(Optional.of(basket));
        product.setTotalCount(3L);
        InsufficientStockException exception = assertThrows(InsufficientStockException.class, () ->
                basketService.saveCountProduct(1L, 6));
        assertTrue(exception.getMessage().contains("Only: left in stock"));
    }

    @Test
    void saveCountProduct_ShouldDeleteBasket_WhenValueIsZero() {
        when(basketRepository.findById(1L)).thenReturn(Optional.of(basket));
        when(productService.getById(1L)).thenReturn(product);
        basketService.saveCountProduct(1L, 0);
        verify(basketRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteByUser_WithoutReturn() {
        basketService.deleteByUser(user);
        verify(basketRepository, times(1)).deleteByUser(user);
    }

    @Test
    void buyProduct_ShouldCreateNewBasket_WhenBasketDoesNotExist() {
        when(productService.getById(1L)).thenReturn(product);
        when(basketRepository.getBasketByUserAndArticleId(user, 1L)).thenReturn(null);

        ProductTranslation translation = new ProductTranslation();
        translation.setLanguageCode(LanguageCode.uk);
        translation.setName("Test Product");
        product.setProductTranslations(List.of(translation));
        product.setMass(5);

        basketService.buyProduct(1L, user);

        verify(basketRepository, times(1)).save(any(Basket.class));
        verify(productService, times(1)).decreaseCountItems(1L);
    }

    @Test
    void buyProduct_ShouldUpdateBasket_WhenBasketExists() {
        when(productService.getById(1L)).thenReturn(product);
        when(basketRepository.getBasketByUserAndArticleId(user, 1L)).thenReturn(basket);

        ProductTranslation translation = new ProductTranslation();
        translation.setLanguageCode(LanguageCode.uk);
        translation.setName("Test Product");
        product.setProductTranslations(List.of(translation));

        basketService.buyProduct(1L, user);

        assertEquals(3, basket.getCount());
        verify(basketRepository, times(1)).save(basket);
        verify(productService, times(1)).decreaseCountItems(1L);
    }

    @Test
    void buyProduct_ShouldThrowException_WhenInsufficientStock() {
        product.setTotalCount(0L);
        when(productService.getById(1L)).thenReturn(product);

        InsufficientStockException exception = assertThrows(InsufficientStockException.class, () ->
                basketService.buyProduct(1L, user)
        );

        assertEquals("No more products!", exception.getMessage());
    }

    @Test
    void buyProduct_ShouldThrowException_WhenTranslationNotFound() {
        product.setProductTranslations(Collections.emptyList());
        when(productService.getById(1L)).thenReturn(product);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                basketService.buyProduct(1L, user)
        );

        assertEquals("Translation not found", exception.getMessage());
    }

    @Test
    void buyProduct_ShouldDoNothing_WhenProductIdIsNull() {
        basketService.buyProduct(null, user);

        verify(productService, never()).getById(anyLong());
        verify(basketRepository, never()).save(any(Basket.class));
    }
}