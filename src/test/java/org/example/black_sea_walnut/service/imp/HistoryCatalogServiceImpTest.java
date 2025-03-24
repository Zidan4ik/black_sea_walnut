package org.example.black_sea_walnut.service.imp;

import lombok.SneakyThrows;
import org.example.black_sea_walnut.dto.admin.historyMedia.HistoryMediaRequestForAdd;
import org.example.black_sea_walnut.entity.Banner;
import org.example.black_sea_walnut.enums.MediaType;
import org.mockito.Mock;

import org.example.black_sea_walnut.dto.admin.pages.catalog.request.BannerBlockRequestForAdd;
import org.example.black_sea_walnut.dto.admin.pages.catalog.request.EcologicallyBlockRequestForAdd;
import org.example.black_sea_walnut.dto.admin.pages.catalog.response.BannerBlockResponseForAdd;
import org.example.black_sea_walnut.dto.admin.pages.catalog.response.EcologicallyBlockResponseForAdd;
import org.example.black_sea_walnut.entity.History;
import org.example.black_sea_walnut.entity.HistoryMedia;
import org.example.black_sea_walnut.enums.PageType;
import org.example.black_sea_walnut.mapper.pages.HistoryCatalogMapper;
import org.example.black_sea_walnut.service.HistoryService;
import org.example.black_sea_walnut.service.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistoryCatalogServiceImpTest {
    @Mock
    private HistoryService historyService;

    @Mock
    private HistoryCatalogMapper catalogMapper;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private HistoryCatalogServiceImp historyCatalogService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(historyCatalogService, "contextPath", "/uploads");
    }

    @Test
    void testGetByPageTypeInResponseBannerBlock() {
        PageType type = PageType.catalog_banner;
        History history = new History();
        BannerBlockResponseForAdd response = BannerBlockResponseForAdd.builder().build();
        when(historyService.getByPageType(type)).thenReturn(history);
        when(catalogMapper.toResponseBannerBlockForAdd(history)).thenReturn(response);
        BannerBlockResponseForAdd result = historyCatalogService.getByPageTypeInResponseBannerBlock(type);
        assertNotNull(result);
        verify(historyService).getByPageType(type);
        verify(catalogMapper).toResponseBannerBlockForAdd(history);
    }

    @Test
    void testGetByPageTypeInResponseEcologicallyBlock() {
        PageType type = PageType.catalog_ecologically_pure_walnut;
        History history = new History();
        EcologicallyBlockResponseForAdd response = EcologicallyBlockResponseForAdd.builder().build();
        when(historyService.getByPageType(type)).thenReturn(history);
        when(catalogMapper.toResponseEcologicallyBlockForAdd(history)).thenReturn(response);
        EcologicallyBlockResponseForAdd result = historyCatalogService.getByPageTypeInResponseEcologicallyBlock(type);
        assertNotNull(result);
        verify(historyService).getByPageType(type);
        verify(catalogMapper).toResponseEcologicallyBlockForAdd(history);
    }

    @Test
    void testSaveHistoryBannerBlock() {
        BannerBlockRequestForAdd dto = new BannerBlockRequestForAdd();
        dto.setCatalogBannerId(1L);
        dto.setCatalogBannerPathToImage("");
        dto.setCatalogBannerFile(new MockMultipartFile("file", "image.jpeg", "image/jpeg", new byte[]{1, 2, 3, 4}));
        History history = new History();
        history.setBanner(new Banner());

        when(historyService.getById(1L)).thenReturn(history);
        when(catalogMapper.toEntityFromRequestBannerBlock(dto)).thenReturn(history);
        when(historyService.save(history)).thenReturn(history);

        History result = historyCatalogService.saveHistoryBannerBlock(dto);

        assertNotNull(result);
        verify(historyService).getById(1L);
        verify(imageService).save(any(), any());
        verify(historyService).save(history);
    }

    @Test
    void testSaveHistoryBannerBlock_WhereIdIsNull() {
        BannerBlockRequestForAdd dto = new BannerBlockRequestForAdd();
        History history = new History();
        when(catalogMapper.toEntityFromRequestBannerBlock(dto)).thenReturn(history);
        when(historyService.save(history)).thenReturn(history);
        History result = historyCatalogService.saveHistoryBannerBlock(dto);
        assertNotNull(result);
        verify(imageService).save(any(), any());
        verify(historyService).save(history);
    }

    @Test
    void testSaveHistoryBannerBlock_whenIdAreNull() {
        BannerBlockRequestForAdd dto = new BannerBlockRequestForAdd();
        History history = new History();
        when(catalogMapper.toEntityFromRequestBannerBlock(dto)).thenReturn(history);
        when(historyService.save(history)).thenReturn(history);
        History result = historyCatalogService.saveHistoryBannerBlock(dto);
        assertNotNull(result);
        verify(imageService).save(any(), any());
        verify(historyService).save(history);
    }

    @Test
    void testSaveHistoryBannerBlock_WhenBannerIsEmpty() {
        BannerBlockRequestForAdd dto = new BannerBlockRequestForAdd();
        dto.setCatalogBannerId(1L);
        dto.setCatalogBannerPathToImage("");
        dto.setCatalogBannerFile(new MockMultipartFile("file", "image.jpeg", "image/jpeg", new byte[]{1, 2, 3, 4}));
        History history = new History();
        when(historyService.getById(1L)).thenReturn(history);
        when(catalogMapper.toEntityFromRequestBannerBlock(dto)).thenReturn(history);
        when(historyService.save(history)).thenReturn(history);
        History result = historyCatalogService.saveHistoryBannerBlock(dto);
        assertNotNull(result);
        verify(historyService).getById(1L);
        verify(imageService).save(any(), any());
        verify(historyService).save(history);
    }

    @Test
    void testSaveHistoryBannerBlock_WhenPathToImageIsNoEmpty() {
        BannerBlockRequestForAdd dto = new BannerBlockRequestForAdd();
        dto.setCatalogBannerId(1L);
        dto.setCatalogBannerPathToImage("/path/image");
        History history = new History();
        when(historyService.getById(1L)).thenReturn(history);
        when(catalogMapper.toEntityFromRequestBannerBlock(dto)).thenReturn(history);
        when(historyService.save(history)).thenReturn(history);
        History result = historyCatalogService.saveHistoryBannerBlock(dto);
        assertNotNull(result);
        verify(historyService).getById(1L);
        verify(imageService).save(any(), any());
        verify(historyService).save(history);
    }

    @Test
    void testSaveHistoryBannerBlock_FilesAndPathsAreNull() {
        BannerBlockRequestForAdd dto = new BannerBlockRequestForAdd();
        dto.setCatalogBannerId(1L);
        dto.setCatalogBannerFile(null);
        dto.setCatalogBannerPathToImage("");
        History history = new History();
        when(historyService.getById(1L)).thenReturn(history);
        when(catalogMapper.toEntityFromRequestBannerBlock(dto)).thenReturn(history);
        when(historyService.save(history)).thenReturn(history);
        History result = historyCatalogService.saveHistoryBannerBlock(dto);
        assertNotNull(result);
        verify(historyService).getById(1L);
        verify(imageService).save(any(), any());
        verify(historyService).save(history);
    }

    @Test
    void save_ShouldHandleExceptionAndThrowRuntimeException() {
        BannerBlockRequestForAdd dto = new BannerBlockRequestForAdd();
        dto.setCatalogBannerId(1L);
        dto.setMediaType(MediaType.image);
        dto.setCatalogBannerPathToImage("");
        doThrow(new RuntimeException("Database error")).when(historyService).getById(1L);
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> historyCatalogService.saveHistoryBannerBlock(dto));
    }

    @Test
    void saveHistoryEcologicallyBlock_ShouldHandleRuntimeException() {
        EcologicallyBlockRequestForAdd dto = new EcologicallyBlockRequestForAdd();
        dto.setCatalogEcologicallyId(1L);
        doThrow(new RuntimeException("Database error")).when(historyService).getById(1L);
        assertThrows(RuntimeException.class,
                () -> historyCatalogService.saveHistoryEcologicallyBlock(dto));
    }

    @Test
    void saveHistoryEcologicallyBlock_ShouldSaveSuccessfully_WhenValidDtoProvided() {
        EcologicallyBlockRequestForAdd dto = new EcologicallyBlockRequestForAdd();
        dto.setCatalogEcologicallyId(1L);
        HistoryMediaRequestForAdd e1 = new HistoryMediaRequestForAdd();
        e1.setId(1L);
        e1.setPathToImage("");
        e1.setFileImage(new MockMultipartFile("file", "image.jpeg", "image/jpeg", new byte[]{1, 2, 3, 4}));
        dto.setCatalogEcologicallyFiles(List.of(e1));

        History history = new History();
        HistoryMedia media = new HistoryMedia();
        media.setId(1L);
        media.setPathToImage("/old/path/image.jpg");
        history.setHistoryMedia(List.of(media));

        when(historyService.getById(1L)).thenReturn(history);
        when(catalogMapper.toEntityFromRequestEcologicallyBlock(dto)).thenReturn(history);
        when(historyService.save(history)).thenReturn(history);

        History result = historyCatalogService.saveHistoryEcologicallyBlock(dto);

        assertNotNull(result);
        verify(historyService).getById(1L);
        verify(historyService).save(history);
    }

    @Test
    void saveHistoryEcologicallyBlock_ShouldSaveSuccessfully_WhenIdIsNull() {
        EcologicallyBlockRequestForAdd dto = new EcologicallyBlockRequestForAdd();
        HistoryMediaRequestForAdd e1 = new HistoryMediaRequestForAdd();
        History history = new History();
        when(catalogMapper.toEntityFromRequestEcologicallyBlock(dto)).thenReturn(history);
        when(historyService.save(history)).thenReturn(history);
        History result = historyCatalogService.saveHistoryEcologicallyBlock(dto);
        assertNotNull(result);
        verify(historyService).save(history);
    }

    @SneakyThrows
    @Test
    void saveHistoryEcologicallyBlock_WhereFileImageIsNotEmpty() {
        EcologicallyBlockRequestForAdd dto = new EcologicallyBlockRequestForAdd();
        dto.setCatalogEcologicallyId(1L);
        HistoryMediaRequestForAdd e1 = new HistoryMediaRequestForAdd();
        e1.setPathToImage("/path/to/image");
        dto.setCatalogEcologicallyFiles(List.of(e1));

        History history = new History();
        HistoryMedia media = new HistoryMedia();
        media.setId(1L);
        media.setPathToImage("/old/path/image.jpg");
        history.setHistoryMedia(List.of(media));

        when(historyService.getById(1L)).thenReturn(history);
        when(catalogMapper.toEntityFromRequestEcologicallyBlock(dto)).thenReturn(history);
        when(historyService.save(history)).thenReturn(history);

        History result = historyCatalogService.saveHistoryEcologicallyBlock(dto);

        assertNotNull(result);
        verify(imageService).deleteByPath("/old/path/image.jpg");
        verify(historyService).save(history);
    }

    @SneakyThrows
    @Test
    void saveHistoryEcologicallyBlock_WherePathInDTOIsNotEmpty_HaveNoMatcherId() {
        EcologicallyBlockRequestForAdd dto = new EcologicallyBlockRequestForAdd();
        dto.setCatalogEcologicallyId(1L);
        HistoryMediaRequestForAdd e1 = new HistoryMediaRequestForAdd();
        e1.setId(2L);
        e1.setPathToImage("");
        dto.setCatalogEcologicallyFiles(List.of(e1));

        History history = new History();
        HistoryMedia media = new HistoryMedia();
        media.setId(1L);
        media.setPathToImage("/old/path/image.jpg");
        history.setHistoryMedia(List.of(media));

        when(historyService.getById(1L)).thenReturn(history);
        when(catalogMapper.toEntityFromRequestEcologicallyBlock(dto)).thenReturn(history);
        when(historyService.save(history)).thenReturn(history);

        History result = historyCatalogService.saveHistoryEcologicallyBlock(dto);

        assertNotNull(result);
        verify(imageService).deleteByPath("/old/path/image.jpg");
        verify(historyService).save(history);
    }

    @Test
    void saveHistoryEcologicallyBlock_ShouldHandleNewMediaFiles() {
        EcologicallyBlockRequestForAdd dto = new EcologicallyBlockRequestForAdd();
        dto.setCatalogEcologicallyId(1L);

        MockMultipartFile newFile = new MockMultipartFile("file", "new_image.jpg", "image/jpeg", new byte[]{1, 2, 3});
        HistoryMediaRequestForAdd newMediaDto = new HistoryMediaRequestForAdd();
        newMediaDto.setId(2L);
        newMediaDto.setPathToImage("/path/to/image");
        newMediaDto.setFileImage(newFile);
        dto.setCatalogEcologicallyFiles(List.of(newMediaDto));

        History history = new History();
        when(historyService.getById(1L)).thenReturn(history);
        when(imageService.generateFileName(newFile)).thenReturn("generated_image.jpg");
        when(catalogMapper.toEntityFromRequestEcologicallyBlock(dto)).thenReturn(history);
        when(historyService.save(history)).thenReturn(history);

        History result = historyCatalogService.saveHistoryEcologicallyBlock(dto);
        assertNotNull(result);
        verify(historyService).save(history);
    }

    @Test
    void saveHistoryEcologicallyBlock_ShouldSkipProcessing_WhenNoFilesInDto() {
        EcologicallyBlockRequestForAdd dto = new EcologicallyBlockRequestForAdd();
        dto.setCatalogEcologicallyId(1L);
        dto.setCatalogEcologicallyFiles(null);

        History history = new History();
        when(historyService.getById(1L)).thenReturn(history);
        when(catalogMapper.toEntityFromRequestEcologicallyBlock(dto)).thenReturn(history);
        when(historyService.save(history)).thenReturn(history);

        History result = historyCatalogService.saveHistoryEcologicallyBlock(dto);

        assertNotNull(result);
        verify(historyService).getById(1L);
        verifyNoInteractions(imageService);
        verify(historyService).save(history);
    }

    @Test
    void saveHistoryEcologicallyBlock_ShouldThrowException_WhenHistoryServiceFails() {
        EcologicallyBlockRequestForAdd dto = new EcologicallyBlockRequestForAdd();
        dto.setCatalogEcologicallyId(1L);

        when(historyService.getById(1L)).thenThrow(new RuntimeException("Database error"));

        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> historyCatalogService.saveHistoryEcologicallyBlock(dto));

        assertEquals("Database error", thrown.getMessage());
    }
}