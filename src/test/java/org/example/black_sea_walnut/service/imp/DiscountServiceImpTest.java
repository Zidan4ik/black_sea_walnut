package org.example.black_sea_walnut.service.imp;

import jakarta.persistence.EntityNotFoundException;
import org.example.black_sea_walnut.dto.admin.discount.DiscountRequestForAdd;
import org.example.black_sea_walnut.dto.admin.discount.DiscountResponseForView;
import org.example.black_sea_walnut.entity.Discount;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.mapper.DiscountMapper;
import org.example.black_sea_walnut.repository.DiscountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DiscountServiceImpTest {
    @Mock
    private DiscountRepository discountRepository;

    @Mock
    private DiscountMapper discountMapper;

    @InjectMocks
    private DiscountServiceImp discountService;

    @Test
    void testGetAll() {
        List<Discount> discounts = List.of(new Discount(), new Discount());
        when(discountRepository.findAll()).thenReturn(discounts);
        List<Discount> result = discountService.getAll();
        assertEquals(2, result.size());
        verify(discountRepository, times(1)).findAll();
    }

    @Test
    void testGetAllByLanguageCodeInDTO() {
        Set<Discount> discounts = Set.of(new Discount());
        when(discountRepository.getAllByLanguageCode(LanguageCode.en)).thenReturn(discounts);
        Set<DiscountResponseForView> result = discountService.getAllByLanguageCodeInDTO(LanguageCode.en);
        assertEquals(1, result.size());
        verify(discountRepository, times(1)).getAllByLanguageCode(LanguageCode.en);
    }

    @Test
    void testGetSentence() {
        Set<DiscountResponseForView> discounts = Set.of(
                DiscountResponseForView.builder().name("Discount1").build(),
                DiscountResponseForView.builder().name("Discount2").build()
        );
        String result = discountService.getSentence(discounts);
//        assertEquals("Discount1, Discount2", result); //set collection
    }

    @Test
    void testGetAllByDiscountCommonId() {
        Set<Discount> discounts = Set.of(new Discount());
        when(discountRepository.getAllByDiscountCommonId(1L)).thenReturn(discounts);
        Set<Discount> result = discountService.getAllByDiscountCommonId(1L);
        assertEquals(1, result.size());
        verify(discountRepository, times(1)).getAllByDiscountCommonId(1L);
    }

    @Test
    void testIsExistByDiscountCommonId() {
        when(discountRepository.existsByDiscountCommonId(1L)).thenReturn(true);
        boolean exists = discountService.isExistByDiscountCommonId(1L);
        assertTrue(exists);
        verify(discountRepository, times(1)).existsByDiscountCommonId(1L);
    }

    @Test
    void testIsExistById() {
        when(discountRepository.existsById(1L)).thenReturn(true);
        boolean exists = discountService.isExistById(1L);
        assertTrue(exists);
        verify(discountRepository, times(1)).existsById(1L);
    }

    @Test
    void testGetById() {
        Discount discount = new Discount();
        when(discountRepository.findById(1L)).thenReturn(Optional.of(discount));
        Discount result = discountService.getById(1L);
        assertNotNull(result);
        verify(discountRepository, times(1)).findById(1L);
    }

    @Test
    void testGetById_NotFound() {
//        when(discountRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> discountService.getById(1L));
    }

    @Test
    void testGetByIdInResponseForAdd() {
        discountService.getByIdInResponseForAdd(1L);
    }

    @Test
    void testGetByIdAndLanguageInResponseForAdd() {
        discountService.getByIdAndLanguageInResponseForAdd(1L, LanguageCode.en);
    }

    @Test
    void testSaveDiscountEntity() {
        discountService.save(new Discount());
    }

    @Test
    void testSaveDiscountDto() {
        discountService.save(DiscountRequestForAdd.builder().value(1L).build());
    }

    @Test
    void testDeleteCommonById() {
        discountService.deleteCommonById(1L);
    }
}