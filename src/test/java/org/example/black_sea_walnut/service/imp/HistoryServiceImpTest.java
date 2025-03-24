package org.example.black_sea_walnut.service.imp;

import jakarta.persistence.EntityNotFoundException;
import org.example.black_sea_walnut.entity.History;
import org.example.black_sea_walnut.enums.PageType;
import org.example.black_sea_walnut.repository.HistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistoryServiceImpTest {
    @Mock
    private HistoryRepository historyRepository;

    @InjectMocks
    private HistoryServiceImp historyService;

    private History history;

    @BeforeEach
    void setUp() {
        history = new History();
        history.setId(1L);
        history.setPageType(PageType.main_aim);
    }

    @Test
    void getById_ShouldReturnHistory() {
        when(historyRepository.findById(1L)).thenReturn(Optional.of(history));

        History result = historyService.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(historyRepository, times(1)).findById(1L);
    }

    @Test
    void getById_ShouldThrowEntityNotFoundException() {
        when(historyRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> historyService.getById(1L));

        assertEquals("History with id: 1 was not found!", exception.getMessage());
        verify(historyRepository, times(1)).findById(1L);
    }

    @Test
    void getByPageType_ShouldReturnHistory() {
        when(historyRepository.findByPageType(PageType.main_aim)).thenReturn(Optional.of(history));

        History result = historyService.getByPageType(PageType.main_aim);

        assertNotNull(result);
        assertEquals(PageType.main_aim, result.getPageType());
        verify(historyRepository, times(1)).findByPageType(PageType.main_aim);
    }

    @Test
    void getByPageType_ShouldThrowEntityNotFoundException() {
        when(historyRepository.findByPageType(PageType.main_aim)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> historyService.getByPageType(PageType.main_aim));

        assertEquals("History with type: main_aim was not found!", exception.getMessage());
        verify(historyRepository, times(1)).findByPageType(PageType.main_aim);
    }

    @Test
    void save_ShouldReturnSavedHistory() {
        when(historyRepository.save(history)).thenReturn(history);

        History result = historyService.save(history);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(historyRepository, times(1)).save(history);
    }

    @Test
    void getAll_ShouldReturnListOfHistories() {
        List<History> historyList = List.of(history);
        when(historyRepository.findAll()).thenReturn(historyList);

        List<History> result = historyService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(historyRepository, times(1)).findAll();
    }
}