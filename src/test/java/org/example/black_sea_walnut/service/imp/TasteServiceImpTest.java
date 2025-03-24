package org.example.black_sea_walnut.service.imp;

import jakarta.persistence.EntityNotFoundException;
import org.example.black_sea_walnut.dto.admin.taste.TasteRequestForAdd;
import org.example.black_sea_walnut.dto.admin.taste.TasteResponseForAdd;
import org.example.black_sea_walnut.dto.admin.taste.TasteResponseForView;
import org.example.black_sea_walnut.entity.Taste;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.mapper.TasteMapper;
import org.example.black_sea_walnut.repository.TasteRepository;
import org.junit.jupiter.api.BeforeEach;
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
class TasteServiceImpTest {

    @Mock
    private TasteRepository tasteRepository;

    @Mock
    private TasteMapper tasteMapper;

    @InjectMocks
    private TasteServiceImp tasteService;

    private Taste taste;
    private TasteResponseForView tasteResponseForView;
    private TasteRequestForAdd tasteRequestForAdd;

    @BeforeEach
    void setUp() {
        taste = new Taste();
        taste.setId(1L);
        taste.setName("Vanilla");
        taste.setLanguageCode(LanguageCode.en);

        tasteResponseForView = TasteResponseForView.builder()
                .id(1L)
                .name("Vanilla")
                .build();

        tasteRequestForAdd = TasteRequestForAdd.builder()
                .nameUk("Ванілка")
                .nameEn("Vanilla")
                .build();
    }

    @Test
    void testGetAllByLanguageCodeInDTO() {
        when(tasteRepository.findAll()).thenReturn(List.of(taste));

        List<Taste> result = tasteService.getAllByLanguageCodeInDTO();

        assertEquals(1, result.size());
        assertEquals(taste, result.get(0));
        verify(tasteRepository, times(1)).findAll();
    }

    @Test
    void testGetAllByLanguageCodeInDTO_WithLanguageCode() {
        when(tasteRepository.findAllByLanguageCode(LanguageCode.en)).thenReturn(Set.of(taste));
//        when(tasteMapper.toDTOForView(taste)).thenReturn(tasteResponseForView);

        Set<TasteResponseForView> result = tasteService.getAllByLanguageCodeInDTO(LanguageCode.en);

        assertEquals(1, result.size());
//        assertTrue(result.contains(tasteResponseForView));
        verify(tasteRepository, times(1)).findAllByLanguageCode(LanguageCode.en);
    }

    @Test
    void testGetSentence() {
        Set<TasteResponseForView> tastes = Set.of(tasteResponseForView);

        String sentence = tasteService.getSentence(tastes);

        assertEquals("Vanilla", sentence);
    }

    @Test
    void testGetAllByCommonId() {
        when(tasteRepository.findAllByCommonId(1L)).thenReturn(Set.of(taste));

        Set<Taste> result = tasteService.getAllByCommonId(1L);

        assertEquals(1, result.size());
        assertTrue(result.contains(taste));
        verify(tasteRepository, times(1)).findAllByCommonId(1L);
    }

    @Test
    void testSave_TasteEntity() {
        when(tasteRepository.save(taste)).thenReturn(taste);

        Taste savedTaste = tasteService.save(taste);

        assertNotNull(savedTaste);
        assertEquals(taste, savedTaste);
        verify(tasteRepository, times(1)).save(taste);
    }

    @Test
    void testSave_TasteRequestForAdd() {
        tasteService.save(tasteRequestForAdd);
    }

    @Test
    void testSave_TasteRequestForAdd_WhenListIsNull() {
        tasteService.save(TasteRequestForAdd.builder().build());

    }

    @Test
    void testIsExistByCommonId() {
        when(tasteRepository.existsByCommonId(1L)).thenReturn(true);

        boolean exists = tasteService.isExistByCommonId(1L);

        assertTrue(exists);
        verify(tasteRepository, times(1)).existsByCommonId(1L);
    }

    @Test
    void testIsExistById() {
        when(tasteRepository.existsById(1L)).thenReturn(true);

        boolean exists = tasteService.isExistById(1L);

        assertTrue(exists);
        verify(tasteRepository, times(1)).existsById(1L);
    }

    @Test
    void testGetById_TasteFound() {
        when(tasteRepository.findById(1L)).thenReturn(Optional.of(taste));

        Taste result = tasteService.getById(1L);

        assertNotNull(result);
        assertEquals(taste, result);
        verify(tasteRepository, times(1)).findById(1L);
    }

    @Test
    void testGetById_TasteNotFound() {
        when(tasteRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> tasteService.getById(1L));

        assertEquals("Taste with id: 1 was not found!", exception.getMessage());
        verify(tasteRepository, times(1)).findById(1L);
    }

    @Test
    void testGetByIdInResponseForAdd() {
        when(tasteRepository.findAllByCommonId(1L)).thenReturn(Set.of(taste));
        TasteResponseForAdd responseForAdd = TasteResponseForAdd.builder().build();
        TasteResponseForAdd result = tasteService.getByIdInResponseForAdd(1L);
        verify(tasteRepository, times(1)).findAllByCommonId(1L);
    }

    @Test
    void testDeleteByCommonId() {
        doNothing().when(tasteRepository).deleteAllByCommonId(1L);

        tasteService.deleteByCommonId(1L);

        verify(tasteRepository, times(1)).deleteAllByCommonId(1L);
    }
}