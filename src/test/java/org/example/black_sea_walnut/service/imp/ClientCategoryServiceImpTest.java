package org.example.black_sea_walnut.service.imp;

import jakarta.persistence.EntityNotFoundException;
import lombok.SneakyThrows;
import org.example.black_sea_walnut.dto.admin.pages.clients.request.ClientCategoryRequestForAdd;
import org.example.black_sea_walnut.dto.admin.pages.clients.response.ClientCategoryResponseForAdd;
import org.example.black_sea_walnut.entity.ClientCategory;
import org.example.black_sea_walnut.mapper.pages.HistoryClientsMapper;
import org.example.black_sea_walnut.repository.ClientCategoryRepository;
import org.example.black_sea_walnut.service.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientCategoryServiceImpTest {
    @Mock
    private ClientCategoryRepository clientCategoryRepository;

    @Mock
    private HistoryClientsMapper clientsMapper;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private ClientCategoryServiceImp clientCategoryService;

    @Value("${upload.path}")
    private String contextPath;

    private ClientCategory clientCategory;

    @BeforeEach
    void setUp() {
        clientCategory = new ClientCategory();
        clientCategory.setId(1L);
        clientCategory.setPathToImage("/test/image.png");
        clientCategory.setPathToSvg("/test/image.svg");
    }

    @Test
    void testSaveEntity() {
        clientCategoryService.save(clientCategory);
        verify(clientCategoryRepository, times(1)).save(clientCategory);
    }

    @Test
    void testSaveDto_whereMultipartFilesAreNotAbsent() throws IOException {
        ClientCategoryRequestForAdd dto = new ClientCategoryRequestForAdd();
        dto.setClientsCategoryId(1L);
        dto.setClientsCategoryFileImage(null);
        dto.setClientsCategoryFileSvg(null);
        dto.setClientsCategoryPathToImage("image1.jpeg");
        dto.setClientsCategoryPathToSvg("svg1.web");

        when(clientCategoryRepository.findById(1L)).thenReturn(Optional.of(clientCategory));
        when(clientsMapper.toEntityFromRequestClientCategoryBlock(dto)).thenReturn(clientCategory);
        clientCategoryService.save(dto);
        verify(clientCategoryRepository, times(1)).save(any(ClientCategory.class));
    }

    @Test
    void testSaveDto_whenMultipartFileArePresent() throws IOException {
        ClientCategoryRequestForAdd dto = new ClientCategoryRequestForAdd();
        dto.setClientsCategoryId(1L);
        dto.setClientsCategoryFileImage(new MockMultipartFile("file","image.jpeg","image/jpeg",new byte[]{1,2,3,4}));
        dto.setClientsCategoryFileSvg(new MockMultipartFile("file","svg.jpeg","image/jpeg",new byte[]{1,2,3,4}));
        dto.setClientsCategoryPathToImage("image1.jpeg");
        dto.setClientsCategoryPathToSvg("svg1.web");
        when(clientCategoryRepository.findById(1L)).thenReturn(Optional.of(clientCategory));
        when(clientsMapper.toEntityFromRequestClientCategoryBlock(dto)).thenReturn(clientCategory);
        clientCategoryService.save(dto);
        verify(clientCategoryRepository, times(1)).save(any(ClientCategory.class));
    }

    @Test
    void testSaveDto_wherePathsAreEmpty() throws IOException {
        ClientCategoryRequestForAdd dto = new ClientCategoryRequestForAdd();
        dto.setClientsCategoryId(1L);
        dto.setClientsCategoryPathToImage("");
        dto.setClientsCategoryPathToSvg("");

        when(clientCategoryRepository.findById(1L)).thenReturn(Optional.of(clientCategory));
        when(clientsMapper.toEntityFromRequestClientCategoryBlock(dto)).thenReturn(clientCategory);
        clientCategoryService.save(dto);
        verify(clientCategoryRepository, times(1)).save(any(ClientCategory.class));
    }

    @Test
    void testSaveDto_whereIdIsNull() throws IOException {
        ClientCategoryRequestForAdd dto = new ClientCategoryRequestForAdd();
        when(clientsMapper.toEntityFromRequestClientCategoryBlock(dto)).thenReturn(clientCategory);
        clientCategoryService.save(dto);
        verify(clientCategoryRepository, times(1)).save(any(ClientCategory.class));
    }

    @Test
    void testGetAll() {
        when(clientCategoryRepository.findAll()).thenReturn(List.of(clientCategory));
        List<ClientCategory> result = clientCategoryService.getAll();
        assertEquals(1, result.size());
        verify(clientCategoryRepository, times(1)).findAll();
    }

    @Test
    void testGetAllInResponse() {
        ClientCategoryResponseForAdd responseDto = ClientCategoryResponseForAdd.builder().build();
        when(clientCategoryRepository.findAll()).thenReturn(List.of(clientCategory));
        when(clientsMapper.toResponseCategoryForAdd(clientCategory)).thenReturn(responseDto);
        List<ClientCategoryResponseForAdd> response = clientCategoryService.getAllInResponse();
        assertEquals(1, response.size());
        verify(clientCategoryRepository, times(1)).findAll();
    }

    @Test
    void testGetById() {
        when(clientCategoryRepository.findById(1L)).thenReturn(Optional.of(clientCategory));
        ClientCategory result = clientCategoryService.getById(1L);
        assertNotNull(result);
        verify(clientCategoryRepository, times(1)).findById(1L);
    }

    @Test
    void testGetById_NotFound() {
        when(clientCategoryRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> clientCategoryService.getById(2L));
    }
//
//    @Test
//    void testDeleteById() {
//        doNothing().when(clientCategoryRepository).deleteById(1L);
//        clientCategoryService.deleteById(1L);
//        verify(clientCategoryRepository, times(1)).deleteById(1L);
//    }

    @SneakyThrows
    @Test
    void deleteById_ClientExists_ShouldDeleteSuccessfully() {
        when(clientCategoryRepository.findById(1L)).thenReturn(Optional.of(clientCategory));
        clientCategoryService.deleteById(1L);
        verify(imageService).deleteByPath("/test/image.png");
        verify(imageService).deleteByPath("/test/image.svg");
        verify(clientCategoryRepository).deleteById(1L);
    }
}