package org.example.black_sea_walnut.service.imp;

import org.example.black_sea_walnut.dto.admin.pages.clients.request.*;
import org.example.black_sea_walnut.dto.admin.pages.clients.response.*;
import org.example.black_sea_walnut.entity.Banner;
import org.example.black_sea_walnut.entity.History;
import org.example.black_sea_walnut.enums.MediaType;
import org.example.black_sea_walnut.enums.PageType;
import org.example.black_sea_walnut.mapper.pages.HistoryClientsMapper;
import org.example.black_sea_walnut.service.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistoryClientServiceImpTest {
    @Mock
    private HistoryService historyService;
    @Mock
    private HistoryClientsMapper clientsMapper;
    @Mock
    private ClientCategoryService clientCategoryService;
    @Mock
    private ImageService imageService;

    @InjectMocks
    private HistoryClientServiceImp historyClientService;


    @Test
    void getByPageTypeInResponseBannerBlock_ShouldReturnResponse() {
        PageType type = PageType.clients_banner;
        History history = new History();
        ClientBannerResponseForAdd response = ClientBannerResponseForAdd.builder().build();
        when(historyService.getByPageType(type)).thenReturn(history);
        when(clientsMapper.toResponseBannerBlockForAdd(history)).thenReturn(response);
        ClientBannerResponseForAdd result = historyClientService.getByPageTypeInResponseBannerBlock(type);
        assertNotNull(result);
        verify(historyService).getByPageType(type);
        verify(clientsMapper).toResponseBannerBlockForAdd(history);
    }

    @Test
    void getByPageTypeInResponseEcoProductionBlock_ShouldReturnResponse() {
        PageType type = PageType.clients_eco_production;
        History history = new History();
        ClientEcoProductionResponseForAdd response = ClientEcoProductionResponseForAdd.builder().build();
        when(historyService.getByPageType(type)).thenReturn(history);
        when(clientsMapper.toResponseEcoProductionBlockForAdd(history)).thenReturn(response);
        ClientEcoProductionResponseForAdd result = historyClientService.getByPageTypeInResponseEcoProductionBlock(type);
        assertNotNull(result);
        verify(historyService).getByPageType(type);
        verify(clientsMapper).toResponseEcoProductionBlockForAdd(history);
    }

    @Test
    void getAllInResponseCategoryBlock_ShouldReturnList() {
        List<ClientCategoryResponseForAdd> responseList = List.of(ClientCategoryResponseForAdd.builder().build());
        when(clientCategoryService.getAllInResponse()).thenReturn(responseList);
        List<ClientCategoryResponseForAdd> result = historyClientService.getAllInResponseCategoryBlock();
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(clientCategoryService).getAllInResponse();
    }

    @Test
    void saveHistoryBannerBlock_ShouldSaveHistorySuccessfully() {
        ClientBannerRequestForAdd dto = new ClientBannerRequestForAdd();
        dto.setClientsBannerId(1L);
        dto.setClientsBannerPathToBanner("");
        dto.setClientsBannerFile(new MockMultipartFile("file", "image.jpeg", "image/jpeg", new byte[]{1, 2, 3, 4}));
        History history = new History();
        history.setBanner(new Banner());
        when(historyService.getById(1L)).thenReturn(history);
        when(clientsMapper.toEntityFromRequestBannerBlock(dto)).thenReturn(history);
        when(historyService.save(history)).thenReturn(history);
        History result = historyClientService.saveHistoryBannerBlock(dto);
        assertNotNull(result);
        verify(imageService).save(any(), anyString());
        verify(historyService).save(history);
    }

    @Test
    void saveHistoryBannerBlock_ShouldSaveHistorySuccessfully_WhenNoHaveBannerInDto() {
        ClientBannerRequestForAdd dto = new ClientBannerRequestForAdd();
        dto.setClientsBannerId(1L);
        dto.setClientsBannerPathToBanner("");
        History history = new History();
        when(historyService.getById(1L)).thenReturn(history);
        when(clientsMapper.toEntityFromRequestBannerBlock(dto)).thenReturn(history);
        when(historyService.save(history)).thenReturn(history);
        History result = historyClientService.saveHistoryBannerBlock(dto);
        assertNotNull(result);
        verify(imageService).save(any(), anyString());
        verify(historyService).save(history);
    }

    @Test
    void saveHistoryBannerBlock_ShouldSaveHistorySuccessfully_WhenHavePathToImage() {
        ClientBannerRequestForAdd dto = new ClientBannerRequestForAdd();
        dto.setClientsBannerId(1L);
        dto.setClientsBannerPathToBanner("/path/to/image");
        dto.setClientsBannerFile(new MockMultipartFile("file", "image.jpeg", "image/jpeg", new byte[]{1, 2, 3, 4}));
        History history = new History();
        when(historyService.getById(1L)).thenReturn(history);
        when(clientsMapper.toEntityFromRequestBannerBlock(dto)).thenReturn(history);
        when(historyService.save(history)).thenReturn(history);
        History result = historyClientService.saveHistoryBannerBlock(dto);
        assertNotNull(result);
        verify(imageService).save(any(), anyString());
        verify(historyService).save(history);
    }

    @Test
    void saveHistoryBannerBlock_ShouldSaveHistorySuccessfully_WhenIdIsNull() {
        ClientBannerRequestForAdd dto = new ClientBannerRequestForAdd();
        History history = new History();
        when(clientsMapper.toEntityFromRequestBannerBlock(dto)).thenReturn(history);
        when(historyService.save(history)).thenReturn(history);
        History result = historyClientService.saveHistoryBannerBlock(dto);
        assertNotNull(result);
        verify(historyService).save(history);
    }


    @Test
    void saveHistoryCategoryBlock_ShouldCallSaveForEachCategory() {
        ClientCategoryRequestForAdd category1 = new ClientCategoryRequestForAdd();
        ClientCategoryRequestForAdd category2 = new ClientCategoryRequestForAdd();
        List<ClientCategoryRequestForAdd> dtoList = List.of(category1, category2);
        historyClientService.saveHistoryCategoryBlock(dtoList);
    }

    @Test
    void saveHistoryCategoryBlock_WhenListIsNull() {
        historyClientService.saveHistoryCategoryBlock(null);
    }

    @Test
    void saveHistoryEcoProductionBlock_ShouldSaveHistorySuccessfully_WhenFileWasDeleted() {
        ClientEcoProductionRequestForAdd dto = new ClientEcoProductionRequestForAdd();
        dto.setClientsEcoProductionId(1L);
        dto.setClientsEcoProductionPathToBanner("");
        dto.setClientsEcoProductionFile(new MockMultipartFile("file", "image.jpeg", "image/jpeg", new byte[]{1, 2, 3, 4}));
        History history = new History();
        history.setBanner(new Banner());
        when(historyService.getById(1L)).thenReturn(history);
        when(clientsMapper.toEntityFromRequestClientEcoProductionBlock(dto)).thenReturn(history);
        when(historyService.save(history)).thenReturn(history);
        History result = historyClientService.saveHistoryEcoProductionBlock(dto);
        assertNotNull(result);
        verify(imageService).save(any(), anyString());
        verify(historyService).save(history);
    }

    @Test
    void saveHistoryEcoProductionBlock_ShouldSaveHistorySuccessfully() {
        ClientEcoProductionRequestForAdd dto = new ClientEcoProductionRequestForAdd();
        dto.setClientsEcoProductionId(1L);
        dto.setClientsEcoProductionPathToBanner("/path/to/image");
        History history = new History();
        history.setBanner(new Banner());
        when(historyService.getById(1L)).thenReturn(history);
        when(clientsMapper.toEntityFromRequestClientEcoProductionBlock(dto)).thenReturn(history);
        when(historyService.save(history)).thenReturn(history);
        History result = historyClientService.saveHistoryEcoProductionBlock(dto);
        assertNotNull(result);
        verify(imageService).save(any(), anyString());
        verify(historyService).save(history);
    }

    @Test
    void saveHistoryEcoProductionBlock_ShouldSaveHistorySuccessfully_WhenNoBannerAndFile() {
        ClientEcoProductionRequestForAdd dto = new ClientEcoProductionRequestForAdd();
        dto.setClientsEcoProductionId(1L);
        dto.setClientsEcoProductionPathToBanner("");
        History history = new History();
        when(historyService.getById(1L)).thenReturn(history);
        when(clientsMapper.toEntityFromRequestClientEcoProductionBlock(dto)).thenReturn(history);
        when(historyService.save(history)).thenReturn(history);
        History result = historyClientService.saveHistoryEcoProductionBlock(dto);
        assertNotNull(result);
        verify(imageService).save(any(), anyString());
        verify(historyService).save(history);
    }

    @Test
    void saveHistoryEcoProductionBlock_ShouldSaveHistorySuccessfully_WhenIdIsNull() {
        ClientEcoProductionRequestForAdd dto = new ClientEcoProductionRequestForAdd();
        History history = new History();
        when(clientsMapper.toEntityFromRequestClientEcoProductionBlock(dto)).thenReturn(history);
        when(historyService.save(history)).thenReturn(history);
        History result = historyClientService.saveHistoryEcoProductionBlock(dto);
        assertNotNull(result);
        verify(historyService).save(history);
    }

    @Test
    void deleteById_ShouldCallDeleteMethod() {
        Long id = 1L;
        historyClientService.deleteById(id);
        verify(clientCategoryService).deleteById(id);
    }

    @Test
    void save_ShouldHandleExceptionAndThrowRuntimeException() {
        ClientBannerRequestForAdd dto = new ClientBannerRequestForAdd();
        dto.setClientsBannerId(1L);
        dto.setMediaType(MediaType.image);
        dto.setClientsBannerPathToBanner("");
        doThrow(new RuntimeException("Database error")).when(historyService).getById(1L);
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> historyClientService.saveHistoryBannerBlock(dto));
    }
    @Test
    void saveHistoryEcoProductionBlock_ShouldHandleExceptionAndThrowRuntimeException() {
        ClientEcoProductionRequestForAdd dto = new ClientEcoProductionRequestForAdd();
        dto.setClientsEcoProductionId(1L);
        dto.setMediaType(MediaType.image);
        dto.setClientsEcoProductionPathToBanner("");
        doThrow(new RuntimeException("Database error")).when(historyService).getById(1L);
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> historyClientService.saveHistoryEcoProductionBlock(dto));
    }
}