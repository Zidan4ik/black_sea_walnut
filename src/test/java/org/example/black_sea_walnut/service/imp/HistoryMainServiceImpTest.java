package org.example.black_sea_walnut.service.imp;

import lombok.SneakyThrows;
import org.example.black_sea_walnut.dto.admin.historyMedia.HistoryMediaRequestForAdd;
import org.example.black_sea_walnut.dto.admin.pages.main.request.*;
import org.example.black_sea_walnut.dto.admin.pages.main.response.*;
import org.example.black_sea_walnut.entity.Banner;
import org.example.black_sea_walnut.entity.History;
import org.example.black_sea_walnut.entity.HistoryMedia;
import org.example.black_sea_walnut.enums.PageType;
import org.example.black_sea_walnut.mapper.pages.HistoryMainMapper;
import org.example.black_sea_walnut.service.HistoryService;
import org.example.black_sea_walnut.service.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class HistoryMainServiceImpTest {
    @Mock
    private HistoryMainMapper historyMainMapper;

    @Mock
    private HistoryService historyService;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private HistoryMainServiceImp historyMainService;

    private History history;

    @BeforeEach
    void setUp() {
        history = new History();
        history.setId(1L);
        Banner banner = new Banner();
        banner.setPathToMedia("/path/to/image/banner");
        history.setBanner(banner);
    }

    @Test
    void getByPageTypeInResponseMainBlock_ShouldReturnResponse() {
        BlockResponseForAddInMain response = BlockResponseForAddInMain.builder().build();
        when(historyService.getByPageType(PageType.main_banner)).thenReturn(history);
        when(historyMainMapper.toResponseMainBlockForAdd(history)).thenReturn(response);
        BlockResponseForAddInMain result = historyMainService.getByPageTypeInResponseMainBlock(PageType.main_banner);
        assertNotNull(result);
        verify(historyService).getByPageType(PageType.main_banner);
        verify(historyMainMapper).toResponseMainBlockForAdd(history);
    }


    @Test
    void getByPageTypeInResponseMainProduction_ShouldReturnResponse() {
        ProductionResponseForAddInMain response = ProductionResponseForAddInMain.builder().build();
        when(historyService.getByPageType(PageType.main_production)).thenReturn(history);
        when(historyMainMapper.toResponseProductionBlockForAdd(history)).thenReturn(response);
        ProductionResponseForAddInMain result = historyMainService.getByPageTypeInResponseProductionBlock(PageType.main_production);
        assertNotNull(result);
        verify(historyService).getByPageType(PageType.main_production);
        verify(historyMainMapper).toResponseProductionBlockForAdd(history);
    }

    @Test
    void getByPageTypeInResponseMainFactory_ShouldReturnResponse() {
        FactoryBlockResponseForAddInMain response = FactoryBlockResponseForAddInMain.builder().build();
        when(historyService.getByPageType(PageType.main_factory_about)).thenReturn(history);
        when(historyMainMapper.toResponseFactoryBlockForAdd(history)).thenReturn(response);
        FactoryBlockResponseForAddInMain result = historyMainService.getByPageTypeInResponseFactoryBlock(PageType.main_factory_about);
        assertNotNull(result);
        verify(historyService).getByPageType(PageType.main_factory_about);
        verify(historyMainMapper).toResponseFactoryBlockForAdd(history);
    }

    @Test
    void getByPageTypeInResponseMainNumber_ShouldReturnResponse() {
        NumberBlockResponseForAddInMain response = NumberBlockResponseForAddInMain.builder().build();
        when(historyService.getByPageType(PageType.main_numbers)).thenReturn(history);
        when(historyMainMapper.toResponseNumberBlockForAdd(history)).thenReturn(response);
        NumberBlockResponseForAddInMain result = historyMainService.getByPageTypeInResponseNumberBlock(PageType.main_numbers);
        assertNotNull(result);
        verify(historyService).getByPageType(PageType.main_numbers);
        verify(historyMainMapper).toResponseNumberBlockForAdd(history);
    }

    @Test
    void getByPageTypeInResponseMainAim_ShouldReturnResponse() {
        AimBlockResponseForAddInMain response = AimBlockResponseForAddInMain.builder().build();
        when(historyService.getByPageType(PageType.main_aim)).thenReturn(history);
        when(historyMainMapper.toResponseAimBlockForAdd(history)).thenReturn(response);
        AimBlockResponseForAddInMain result = historyMainService.getByPageTypeInResponseAimBlock(PageType.main_aim);
        assertNotNull(result);
        verify(historyService).getByPageType(PageType.main_aim);
        verify(historyMainMapper).toResponseAimBlockForAdd(history);
    }

    @Test
    void getByPageTypeInResponseMainEcoProduction_ShouldReturnResponse() {
        EcoProductionResponseForAddInMain response = EcoProductionResponseForAddInMain.builder().build();
        when(historyService.getByPageType(PageType.main_eco_production)).thenReturn(history);
        when(historyMainMapper.toResponseEcoProductionBLockForAdd(history)).thenReturn(response);
        EcoProductionResponseForAddInMain result = historyMainService.getByPageTypeInResponseEcoProductionBlock(PageType.main_eco_production);
        assertNotNull(result);
        verify(historyService).getByPageType(PageType.main_eco_production);
        verify(historyMainMapper).toResponseEcoProductionBLockForAdd(history);
    }

    @Test
    void saveHistoryMainBlock_ShouldSaveSuccessfully() {
        MainBlockRequestForAdd dto = MainBlockRequestForAdd.builder()
                .mainId(1L)
                .mainPathToBanner("")
                .mainFileBanner(new MockMultipartFile("file", "image.jpeg", "image/jpeg", new byte[]{1, 2, 3, 4}))
                .build();

        when(historyMainMapper.toEntityFromRequestForAdd(dto)).thenReturn(history);
        when(historyService.save(history)).thenReturn(history);
        when(historyService.getById(1L)).thenReturn(history);
        History result = historyMainService.saveHistoryMainBlock(dto);
        assertNotNull(result);
        verify(historyService).save(history);
    }

    @Test
    void saveHistoryMainBlock_ShouldSaveSuccessfully_WhenMainPathIsNotEmpty() {
        MainBlockRequestForAdd dto = MainBlockRequestForAdd.builder()
                .mainId(1L)
                .mainPathToBanner("/path/to/image")
                .mainFileBanner(new MockMultipartFile("file", "image.jpeg", "image/jpeg", new byte[]{1, 2, 3, 4}))
                .build();

        when(historyMainMapper.toEntityFromRequestForAdd(dto)).thenReturn(history);
        when(historyService.save(history)).thenReturn(history);
        when(historyService.getById(1L)).thenReturn(history);
        History result = historyMainService.saveHistoryMainBlock(dto);
        assertNotNull(result);
        verify(historyService).save(history);
    }

    @Test
    void saveHistoryMainBlock_ShouldSaveSuccessfully_WhenFileIsNull() {
        MainBlockRequestForAdd dto = MainBlockRequestForAdd.builder()
                .mainId(1L)
                .mainPathToBanner("/path/to/image")
                .build();

        when(historyMainMapper.toEntityFromRequestForAdd(dto)).thenReturn(history);
        when(historyService.save(history)).thenReturn(history);
        when(historyService.getById(1L)).thenReturn(history);
        History result = historyMainService.saveHistoryMainBlock(dto);
        assertNotNull(result);
        verify(historyService).save(history);
    }

    @Test
    void saveHistoryMainBlock_ShouldSaveSuccessfully_WhenBannerIsNull() {
        MainBlockRequestForAdd dto = MainBlockRequestForAdd.builder()
                .mainId(1L)
                .mainPathToBanner("")
                .mainFileBanner(new MockMultipartFile("file", "image.jpeg", "image/jpeg", new byte[]{1, 2, 3, 4}))
                .build();
        history.setBanner(null);
        when(historyMainMapper.toEntityFromRequestForAdd(dto)).thenReturn(history);
        when(historyService.save(history)).thenReturn(history);
        when(historyService.getById(1L)).thenReturn(history);
        History result = historyMainService.saveHistoryMainBlock(dto);
        assertNotNull(result);
        verify(historyService).save(history);
    }

    @Test
    void saveHistoryMainBlock_ShouldSaveSuccessfully_WhenBannerPathIsEmpty() {
        MainBlockRequestForAdd dto = MainBlockRequestForAdd.builder()
                .mainId(1L)
                .mainPathToBanner("")
                .mainFileBanner(new MockMultipartFile("file", "image.jpeg", "image/jpeg", new byte[]{1, 2, 3, 4}))
                .build();
        Banner banner = new Banner();
        banner.setPathToMedia("");
        history.setBanner(banner);
        when(historyMainMapper.toEntityFromRequestForAdd(dto)).thenReturn(history);
        when(historyService.save(history)).thenReturn(history);
        when(historyService.getById(1L)).thenReturn(history);
        History result = historyMainService.saveHistoryMainBlock(dto);
        assertNotNull(result);
        verify(historyService).save(history);
    }

    @Test
    void saveHistoryMainBlock_ShouldThrowExceptionOnError() {
        MainBlockRequestForAdd dto = MainBlockRequestForAdd.builder().build();
        dto.setMainPathToBanner("image.jpg");
        when(historyMainMapper.toEntityFromRequestForAdd(dto)).thenThrow(new RuntimeException("Error"));
        assertThrows(RuntimeException.class, () -> historyMainService.saveHistoryMainBlock(dto));
    }

    @Test
    void saveHistoryAimBlock_ShouldSaveSuccessfully() {
        AimBlockRequestForAdd dto = new AimBlockRequestForAdd();
        dto.setMainAimId(1L);
        dto.setMainAimPathToBanner("");
        dto.setMainAimFileBanner(new MockMultipartFile("file", "image.jpeg", "image/jpeg", new byte[]{1, 2, 3, 4}));
        when(historyMainMapper.toEntityFromRequestForAdd(dto)).thenReturn(history);
        when(historyService.save(history)).thenReturn(history);
        when(historyService.getById(1L)).thenReturn(history);
        History result = historyMainService.saveHistoryAimBlock(dto);
        assertNotNull(result);
        verify(historyService).save(history);
    }

    @Test
    void saveHistoryAimBlock_ShouldSaveSuccessfully_WhenPathToImageIsNotEmpty() {
        AimBlockRequestForAdd dto = new AimBlockRequestForAdd();
        dto.setMainAimId(1L);
        dto.setMainAimPathToBanner("/path/to/image");
        dto.setMainAimFileBanner(new MockMultipartFile("file", "image.jpeg", "image/jpeg", new byte[]{1, 2, 3, 4}));
        when(historyMainMapper.toEntityFromRequestForAdd(dto)).thenReturn(history);
        when(historyService.save(history)).thenReturn(history);
        when(historyService.getById(1L)).thenReturn(history);
        History result = historyMainService.saveHistoryAimBlock(dto);
        assertNotNull(result);
        verify(historyService).save(history);
    }

    @Test
    void saveHistoryAimBlock_ShouldSaveSuccessfully_WhenBannerIsNull() {
        AimBlockRequestForAdd dto = new AimBlockRequestForAdd();
        dto.setMainAimId(1L);
        dto.setMainAimPathToBanner("");

        history.setBanner(null);
        when(historyMainMapper.toEntityFromRequestForAdd(dto)).thenReturn(history);
        when(historyService.save(history)).thenReturn(history);
        when(historyService.getById(1L)).thenReturn(history);
        History result = historyMainService.saveHistoryAimBlock(dto);
        assertNotNull(result);
        verify(historyService).save(history);
    }

    @Test
    void saveHistoryAimBlock_ShouldSaveSuccessfully_WhenBannerPathIsEmpty() {
        AimBlockRequestForAdd dto = new AimBlockRequestForAdd();
        dto.setMainAimId(1L);
        dto.setMainAimPathToBanner("");
        dto.setMainAimFileBanner(new MockMultipartFile("file", "image.jpeg", "image/jpeg", new byte[]{1, 2, 3, 4}));
        Banner banner = new Banner();
        banner.setPathToMedia("");
        history.setBanner(banner);
        when(historyMainMapper.toEntityFromRequestForAdd(dto)).thenReturn(history);
        when(historyService.save(history)).thenReturn(history);
        when(historyService.getById(1L)).thenReturn(history);
        History result = historyMainService.saveHistoryAimBlock(dto);
        assertNotNull(result);
        verify(historyService).save(history);
    }

    @Test
    void saveHistoryAimBlock_ShouldThrowExceptionOnError() {
        AimBlockRequestForAdd dto = new AimBlockRequestForAdd();
        dto.setMainAimPathToBanner("image.jpg");
        when(historyMainMapper.toEntityFromRequestForAdd(dto)).thenThrow(new RuntimeException("Error"));
        assertThrows(RuntimeException.class, () -> historyMainService.saveHistoryAimBlock(dto));
    }

    @Test
    void saveHistoryEcoProductionBlock_ShouldSaveSuccessfully() {
        EcoProductionRequestForAdd dto = EcoProductionRequestForAdd.builder()
                .mainEcoProductionId(1L)
                .mainEcoProductionPathToBanner("")
                .mainEcoProductionFileBanner(new MockMultipartFile("file", "image.jpeg", "image/jpeg", new byte[]{1, 2, 3, 4}))
                .build();
        when(historyMainMapper.toEntityFromRequestForAdd(dto)).thenReturn(history);
        when(historyService.save(history)).thenReturn(history);
        when(historyService.getById(1L)).thenReturn(history);
        History result = historyMainService.saveHistoryEcoProductionBlock(dto);
        assertNotNull(result);
        verify(historyService).save(history);
    }

    @Test
    void saveHistoryEcoProductionBlock_ShouldSaveSuccessfully_WhenEntityBannerISNUll() {
        EcoProductionRequestForAdd dto = EcoProductionRequestForAdd.builder()
                .mainEcoProductionId(1L)
                .mainEcoProductionPathToBanner("")
                .mainEcoProductionFileBanner(new MockMultipartFile("file", "image.jpeg", "image/jpeg", new byte[]{1, 2, 3, 4}))
                .build();
        history.setBanner(null);
        when(historyMainMapper.toEntityFromRequestForAdd(dto)).thenReturn(history);
        when(historyService.save(history)).thenReturn(history);
        when(historyService.getById(1L)).thenReturn(history);
        History result = historyMainService.saveHistoryEcoProductionBlock(dto);
        assertNotNull(result);
        verify(historyService).save(history);
    }

    @Test
    void saveHistoryEcoProductionBlock_ShouldSaveSuccessfully_WhenEntityBannerPathIsEmpty() {
        EcoProductionRequestForAdd dto = EcoProductionRequestForAdd.builder()
                .mainEcoProductionId(1L)
                .mainEcoProductionPathToBanner("")
                .mainEcoProductionFileBanner(new MockMultipartFile("file", "image.jpeg", "image/jpeg", new byte[]{1, 2, 3, 4}))
                .build();
        Banner banner = new Banner();
        banner.setPathToMedia("");
        history.setBanner(banner);
        when(historyMainMapper.toEntityFromRequestForAdd(dto)).thenReturn(history);
        when(historyService.save(history)).thenReturn(history);
        when(historyService.getById(1L)).thenReturn(history);
        History result = historyMainService.saveHistoryEcoProductionBlock(dto);
        assertNotNull(result);
        verify(historyService).save(history);
    }

    @Test
    void saveHistoryEcoProductionBlock_ShouldSaveSuccessfully_WhenFileBannerIsNull() {
        EcoProductionRequestForAdd dto = EcoProductionRequestForAdd.builder()
                .mainEcoProductionId(1L)
                .mainEcoProductionPathToBanner("")
                .build();
        when(historyMainMapper.toEntityFromRequestForAdd(dto)).thenReturn(history);
        when(historyService.save(history)).thenReturn(history);
        when(historyService.getById(1L)).thenReturn(history);
        History result = historyMainService.saveHistoryEcoProductionBlock(dto);
        assertNotNull(result);
        verify(historyService).save(history);
    }

    @Test
    void saveHistoryEcoProductionBlock_ShouldThrowExceptionOnError() {
        EcoProductionRequestForAdd dto = EcoProductionRequestForAdd.builder().build();
        dto.setMainEcoProductionPathToBanner("image.jpg");
        when(historyMainMapper.toEntityFromRequestForAdd(dto)).thenThrow(new RuntimeException("Error"));
        assertThrows(RuntimeException.class, () -> historyMainService.saveHistoryEcoProductionBlock(dto));
    }

    @Test
    void saveHistoryProductionBlock_ShouldSaveSuccessfully() {
        ProductionBlockRequestForAdd dto = ProductionBlockRequestForAdd.builder().build();
        when(historyMainMapper.toEntityFromRequestForAdd(dto)).thenReturn(history);
        when(historyService.save(history)).thenReturn(history);
        History result = historyMainService.saveHistoryProductionBlock(dto);
        assertNotNull(result);
        verify(historyService).save(history);
    }

    @Test
    void saveHistoryNumberBlock_ShouldSaveSuccessfully() {
        NumberBlockRequestForAdd dto = NumberBlockRequestForAdd.builder().build();
        when(historyMainMapper.toEntityFromRequestForAdd(dto)).thenReturn(history);
        when(historyService.save(history)).thenReturn(history);
        History result = historyMainService.saveHistoryNumberBlock(dto);
        assertNotNull(result);
        verify(historyService).save(history);
    }

    @Test
    void saveHistoryFactoryBlock_ShouldSaveSuccessfully() {
        FactoryBlockRequestForAdd dto = new FactoryBlockRequestForAdd();
        dto.setMainFactoryId(1L);
        HistoryMediaRequestForAdd de1 = new HistoryMediaRequestForAdd();
        de1.setId(1L);
        de1.setFileImage(new MockMultipartFile("file", "image.jpeg", "image/jpeg", new byte[]{1, 2, 3, 4}));
        de1.setPathToImage("/path/to/image1");

        HistoryMediaRequestForAdd de2 = new HistoryMediaRequestForAdd();
        de2.setId(1L);
        de2.setPathToImage("/path/to/image1");
        dto.setFiles(List.of(de1, de2));

        HistoryMedia e1 = new HistoryMedia();
        HistoryMedia e2 = new HistoryMedia();
        e1.setId(1L);
        e1.setPathToImage("/path/to/image1");
        e2.setId(2L);
        e2.setPathToImage("/path/to/image2");
        history.setHistoryMedia(List.of(e1, e2));

        when(historyMainMapper.toEntityFromRequestForAdd(dto)).thenReturn(history);
        when(historyService.save(history)).thenReturn(history);
        when(historyService.getById(1L)).thenReturn(history);

        History result = historyMainService.saveHistoryFactoryBlock(dto);
        assertNotNull(result);
        verify(historyService).save(history);
    }

    @Test
    void saveHistoryFactoryBlock_ShouldSaveSuccessfully_WhenDtoFileAreNull() {
        FactoryBlockRequestForAdd dto = new FactoryBlockRequestForAdd();
        dto.setMainFactoryId(1L);

        when(historyMainMapper.toEntityFromRequestForAdd(dto)).thenReturn(history);
        when(historyService.save(history)).thenReturn(history);
        when(historyService.getById(1L)).thenReturn(history);

        History result = historyMainService.saveHistoryFactoryBlock(dto);
        assertNotNull(result);
        verify(historyService).save(history);
    }

    @Test
    void saveHistoryFactoryBlock_ShouldThrowExceptionOnError() {
        FactoryBlockRequestForAdd dto = new FactoryBlockRequestForAdd();
        when(historyMainMapper.toEntityFromRequestForAdd(dto)).thenThrow(new RuntimeException("Error"));
        assertThrows(RuntimeException.class, () -> historyMainService.saveHistoryFactoryBlock(dto));
    }
}