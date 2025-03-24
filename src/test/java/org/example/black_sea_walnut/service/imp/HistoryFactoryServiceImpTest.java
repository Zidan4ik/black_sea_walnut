package org.example.black_sea_walnut.service.imp;

import lombok.SneakyThrows;
import org.example.black_sea_walnut.dto.admin.historyMedia.HistoryMediaRequestForAdd;
import org.example.black_sea_walnut.entity.Banner;
import org.example.black_sea_walnut.entity.HistoryMedia;
import org.example.black_sea_walnut.enums.MediaType;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import org.example.black_sea_walnut.dto.admin.pages.factory.request.BannerBlockRequestForAdd;
import org.example.black_sea_walnut.dto.admin.pages.factory.request.BlockRequestForAdd;
import org.example.black_sea_walnut.dto.admin.pages.factory.response.FactoryBannerBlockResponseForAdd;
import org.example.black_sea_walnut.dto.admin.pages.factory.response.BlockResponseForAdd;
import org.example.black_sea_walnut.entity.History;
import org.example.black_sea_walnut.enums.PageType;
import org.example.black_sea_walnut.mapper.pages.HistoryFactoryMapper;
import org.example.black_sea_walnut.service.HistoryService;
import org.example.black_sea_walnut.service.ImageService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistoryFactoryServiceImpTest {
    @Mock
    private HistoryService historyService;
    @Mock
    private HistoryFactoryMapper factoryMapper;
    @Mock
    private ImageService imageService;

    @InjectMocks
    private HistoryFactoryServiceImp historyFactoryService;

    @Test
    void getByPageTypeInResponseBannerBlock_ShouldReturnResponse() {
        PageType type = PageType.factory_banner;
        History history = new History();
        FactoryBannerBlockResponseForAdd response = FactoryBannerBlockResponseForAdd.builder().build();

        when(historyService.getByPageType(type)).thenReturn(history);
        when(factoryMapper.toResponseBannerBlockForAdd(history)).thenReturn(response);

        FactoryBannerBlockResponseForAdd result = historyFactoryService.getByPageTypeInResponseBannerBlock(type);

        assertNotNull(result);
        verify(historyService).getByPageType(type);
        verify(factoryMapper).toResponseBannerBlockForAdd(history);
    }

    @Test
    void getByPageTypeInResponseBlock_ShouldReturnResponse() {
        PageType type = PageType.factory_block2;
        History history = new History();
        BlockResponseForAdd response = BlockResponseForAdd.builder().build();

        when(historyService.getByPageType(type)).thenReturn(history);
        when(factoryMapper.toResponseBlockForAdd(history)).thenReturn(response);

        BlockResponseForAdd result = historyFactoryService.getByPageTypeInResponseBlock(type);

        assertNotNull(result);
        verify(historyService).getByPageType(type);
        verify(factoryMapper).toResponseBlockForAdd(history);
    }

    @Test
    void saveHistoryBannerBlock_ShouldSaveSuccessfully() {
        BannerBlockRequestForAdd dto = new BannerBlockRequestForAdd();
        dto.setFactoryBannerId(1L);
        dto.setFactoryBannerPathToBanner("");
        MockMultipartFile file = new MockMultipartFile("file", "banner.jpg", "image/jpeg", new byte[10]);
        dto.setFactoryBannerFile(file);

        History history = new History();
        history.setBanner(new Banner());
        when(historyService.getById(1L)).thenReturn(history);
        when(imageService.generateFileName(any())).thenReturn("generated.jpg");
        when(factoryMapper.toEntityFromRequestBannerBlock(dto)).thenReturn(history);
        when(historyService.save(history)).thenReturn(history);

        History result = historyFactoryService.saveHistoryBannerBlock(dto);

        assertNotNull(result);
        verify(imageService).save(any(), anyString());
        verify(historyService).save(history);
    }

    @Test
    void saveHistoryBannerBlock_ShouldSaveSuccessfully_WhenBannerIsNullAndFile() {
        BannerBlockRequestForAdd dto = new BannerBlockRequestForAdd();
        dto.setFactoryBannerId(1L);
        dto.setFactoryBannerPathToBanner("");

        History history = new History();
        when(historyService.getById(1L)).thenReturn(history);
        when(factoryMapper.toEntityFromRequestBannerBlock(dto)).thenReturn(history);
        when(historyService.save(history)).thenReturn(history);

        History result = historyFactoryService.saveHistoryBannerBlock(dto);

        assertNotNull(result);
        verify(imageService).save(any(), anyString());
        verify(historyService).save(history);
    }

    @Test
    void saveHistoryBannerBlock_ShouldSaveSuccessfully_WhenPathIsNotEmpty() {
        BannerBlockRequestForAdd dto = new BannerBlockRequestForAdd();
        dto.setFactoryBannerId(1L);
        dto.setFactoryBannerPathToBanner("/path/to/image");
        History history = new History();
        when(historyService.getById(1L)).thenReturn(history);
//        when(imageService.generateFileName(any())).thenReturn("generated.jpg");
        when(factoryMapper.toEntityFromRequestBannerBlock(dto)).thenReturn(history);
        when(historyService.save(history)).thenReturn(history);
        History result = historyFactoryService.saveHistoryBannerBlock(dto);
        assertNotNull(result);
        verify(imageService).save(any(), anyString());
        verify(historyService).save(history);
    }

    @Test
    void save_ShouldHandleExceptionAndThrowRuntimeException() {
        BannerBlockRequestForAdd dto = new BannerBlockRequestForAdd();
        dto.setFactoryBannerId(1L);
        dto.setMediaType(MediaType.image);
        dto.setFactoryBannerPathToBanner("");
        doThrow(new RuntimeException("Database error")).when(historyService).getById(1L);
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> historyFactoryService.saveHistoryBannerBlock(dto));
    }

    @Test
    void saveHistoryBlock_ShouldHandleExceptionAndThrowRuntimeException() {
        BlockRequestForAdd dto = new BlockRequestForAdd();
        dto.setFactoryBlockId(1L);
        doThrow(new RuntimeException("Database error")).when(historyService).getById(1L);
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> historyFactoryService.saveHistoryBlock(dto));
    }

    @Test
    void saveHistoryBannerBlock_ShouldSaveSuccessfully_WhenIdIsNull() {
        BannerBlockRequestForAdd dto = new BannerBlockRequestForAdd();
        History history = new History();
        when(factoryMapper.toEntityFromRequestBannerBlock(dto)).thenReturn(history);
        when(historyService.save(history)).thenReturn(history);
        History result = historyFactoryService.saveHistoryBannerBlock(dto);
        assertNotNull(result);
        verify(historyService).save(history);
    }

    @Test
    void saveHistoryBlock_ShouldSaveSuccessfully() {
        BlockRequestForAdd dto = new BlockRequestForAdd();
        dto.setFactoryBlockId(1L);
        History history = new History();
        history.setBanner(new Banner());
        when(historyService.getById(1L)).thenReturn(history);
        when(factoryMapper.toEntityFromRequestFactoryBlock(dto)).thenReturn(history);
        when(historyService.save(history)).thenReturn(history);
        History result = historyFactoryService.saveHistoryBlock(dto);
        assertNotNull(result);
        verify(historyService).save(history);
    }

    @SneakyThrows
    @Test
    void saveHistoryBlock_ShouldDeleteUnusedImagesAndSaveSuccessfully() {
        BlockRequestForAdd dto = new BlockRequestForAdd();
        dto.setFactoryBlockId(1L);

        HistoryMediaRequestForAdd historyMediaRequestForAdd = new HistoryMediaRequestForAdd();
        historyMediaRequestForAdd.setId(1L);
        historyMediaRequestForAdd.setPathToImage("/path/to/image");

        dto.setFactoryBlockFiles(List.of(historyMediaRequestForAdd));

        History history = new History();
        history.setId(1L);

        HistoryMedia existingMedia1 = new HistoryMedia();
        existingMedia1.setId(1L);
        existingMedia1.setPathToImage("/path/to/image");

        HistoryMedia existingMedia2 = new HistoryMedia();
        existingMedia2.setId(2L);
        existingMedia2.setPathToImage("/path/to/delete");

        history.setHistoryMedia(List.of(existingMedia1, existingMedia2));

        when(historyService.getById(1L)).thenReturn(history);
        when(factoryMapper.toEntityFromRequestFactoryBlock(dto)).thenReturn(history);
        when(historyService.save(history)).thenReturn(history);

        History result = historyFactoryService.saveHistoryBlock(dto);
        assertNotNull(result);
        verify(historyService).save(history);
        verify(imageService).deleteByPath("/path/to/delete");
        verify(imageService, never()).deleteByPath("/path/to/image");
    }

    @SneakyThrows
    @Test
    void saveHistoryBlock_ShouldDeleteUnusedImagesAndSaveSuccessfully_WhereDtoPathIsEmpty() {
        BlockRequestForAdd dto = new BlockRequestForAdd();
        dto.setFactoryBlockId(1L);

        HistoryMediaRequestForAdd historyMediaRequestForAdd = new HistoryMediaRequestForAdd();
        historyMediaRequestForAdd.setId(1L);
        historyMediaRequestForAdd.setPathToImage("");

        dto.setFactoryBlockFiles(List.of(historyMediaRequestForAdd));

        History history = new History();
        history.setId(1L);

        HistoryMedia existingMedia1 = new HistoryMedia();
        existingMedia1.setId(1L);
        existingMedia1.setPathToImage("/path/to/image");


        history.setHistoryMedia(List.of(existingMedia1));

        when(historyService.getById(1L)).thenReturn(history);
        when(factoryMapper.toEntityFromRequestFactoryBlock(dto)).thenReturn(history);
        when(historyService.save(history)).thenReturn(history);

        History result = historyFactoryService.saveHistoryBlock(dto);
        assertNotNull(result);
        verify(historyService).save(history);
    }

    @SneakyThrows
    @Test
    void saveHistoryBlock_ShouldReturnNullMatchersCollections() {
        BlockRequestForAdd dto = new BlockRequestForAdd();
        dto.setFactoryBlockId(1L);

        HistoryMediaRequestForAdd historyMediaRequestForAdd = new HistoryMediaRequestForAdd();
        historyMediaRequestForAdd.setId(null);
        historyMediaRequestForAdd.setPathToImage("");

        dto.setFactoryBlockFiles(List.of(historyMediaRequestForAdd));

        History history = new History();
        history.setId(1L);

        HistoryMedia e1 = new HistoryMedia();
        e1.setId(1L);
        history.setHistoryMedia(List.of(e1));

        when(historyService.getById(1L)).thenReturn(history);
        when(factoryMapper.toEntityFromRequestFactoryBlock(dto)).thenReturn(history);
        when(historyService.save(history)).thenReturn(history);

        History result = historyFactoryService.saveHistoryBlock(dto);
        assertNotNull(result);
        verify(historyService).save(history);
    }

    @Test
    void saveHistoryBlock_ShouldSaveSuccessfully_WhenBannerAndFileAreNull() {
        BlockRequestForAdd dto = new BlockRequestForAdd();
        dto.setFactoryBlockId(1L);

        HistoryMediaRequestForAdd e1 = new HistoryMediaRequestForAdd();
        e1.setFileImage(new MockMultipartFile("file", "banner.jpg", "image/jpeg", new byte[10]));
        e1.setPathToImage("/path/to/image");

        dto.setFactoryBlockFiles(List.of(e1));
        History history = new History();
        when(historyService.getById(1L)).thenReturn(history);
        when(factoryMapper.toEntityFromRequestFactoryBlock(dto)).thenReturn(history);
        when(historyService.save(history)).thenReturn(history);

        History result = historyFactoryService.saveHistoryBlock(dto);

        assertNotNull(result);
        verify(historyService).save(history);
    }

    @Test
    void saveHistoryBlock_ShouldSaveSuccessfully_WhenIdIsNull() {
        BlockRequestForAdd dto = new BlockRequestForAdd();
        History history = new History();
        when(factoryMapper.toEntityFromRequestFactoryBlock(dto)).thenReturn(history);
        when(historyService.save(history)).thenReturn(history);
        History result = historyFactoryService.saveHistoryBlock(dto);
        assertNotNull(result);
        verify(historyService).save(history);
    }
}