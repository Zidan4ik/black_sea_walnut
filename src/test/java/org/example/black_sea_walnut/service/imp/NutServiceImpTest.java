package org.example.black_sea_walnut.service.imp;

import jakarta.persistence.EntityNotFoundException;
import lombok.SneakyThrows;
import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.admin.nut.NutRequestForAdd;
import org.example.black_sea_walnut.dto.admin.nut.NutResponseForAdd;
import org.example.black_sea_walnut.dto.admin.nut.NutResponseForView;
import org.example.black_sea_walnut.entity.Nut;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.mapper.NutMapper;
import org.example.black_sea_walnut.repository.NutRepository;
import org.example.black_sea_walnut.service.ImageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class NutServiceImpTest {
    @Mock
    private NutRepository nutRepository;

    @Mock
    private ImageService imageService;

    @Mock
    private NutMapper mapper;

    @InjectMocks
    private NutServiceImp nutServiceImp;

    @Value("${upload.path:/media}")
    private String contextPath;

    @Test
    void testGetById_whenNutExists() {
        Nut nut = new Nut();
        nut.setId(1L);

        when(nutRepository.findById(1L)).thenReturn(Optional.of(nut));

        Nut result = nutServiceImp.getById(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetById_whenNutDoesNotExist() {
        when(nutRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> nutServiceImp.getById(1L));
    }

    @Test
    void testGetByIdInResponseDtoAdd() {
        Nut nut = new Nut();
        nut.setId(2L);

        NutResponseForAdd dto = NutResponseForAdd.builder().build();

        when(nutRepository.findById(2L)).thenReturn(Optional.of(nut));
        when(mapper.toResponseForAdd(nut)).thenReturn(dto);

        NutResponseForAdd result = nutServiceImp.getByIdInResponseDtoAdd(2L);
        assertEquals(dto, result);
    }

    @Test
    void testSaveEntity() {
        Nut nut = new Nut();
        nut.setId(3L);
        when(nutRepository.save(nut)).thenReturn(nut);
        Nut result = nutServiceImp.save(nut);
        assertEquals(3L, result.getId());
    }

    @Test
    void testSaveEntity_throwsRuntimeExceptionOnGetById() {
        NutRequestForAdd dto = NutRequestForAdd.builder()
                .id(4L)
                .pathToSvg("")
                .pathToImage("")
                .fileImage(mock(MultipartFile.class))
                .fileSvg(mock(MultipartFile.class))
                .build();

        when(nutRepository.findById(4L)).thenThrow(new RuntimeException("Failed"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> nutServiceImp.save(dto));
        assertEquals("Failed", exception.getMessage());
    }

    @Test
    void testSaveEntity_whereIdIsNull() {
        Nut nut = new Nut();
        when(nutRepository.save(nut)).thenReturn(nut);
        Nut result = nutServiceImp.save(nut);
    }

    @SneakyThrows
    @Test
    void testSaveFromDto_withFilesAndOldPaths() {
        NutRequestForAdd dto = NutRequestForAdd.builder()
                .id(4L)
                .pathToSvg("")
                .pathToImage("")
                .fileImage(mock(MultipartFile.class))
                .fileSvg(mock(MultipartFile.class))
                .build();

        Nut existingNut = new Nut();
        existingNut.setPathToImage("/old/image.jpg");
        existingNut.setPathToSvg("/old/image.svg");

        when(nutRepository.findById(4L)).thenReturn(Optional.of(existingNut));
        when(imageService.generateFileName(any())).thenReturn("generated.jpg");
        when(mapper.toEntityFromRequestAdd(any())).thenReturn(new Nut());
        when(nutRepository.save(any())).thenReturn(existingNut);

        Nut result = nutServiceImp.save(dto);

        assertNotNull(result);
        verify(imageService, times(2)).generateFileName(any());
        verify(imageService, times(2)).save(any(), any());
        verify(imageService, times(2)).deleteByPath(any());
    }

    @SneakyThrows
    @Test
    void testSaveFromDto_withNullData() {
        NutRequestForAdd dto = NutRequestForAdd.builder()
                .id(4L)
                .pathToSvg("/path/to/svg")
                .pathToImage("/path/to/image")
                .build();

        Nut existingNut = new Nut();
        existingNut.setPathToImage("/old/image.jpg");
        existingNut.setPathToSvg("/old/image.svg");

        when(nutRepository.findById(4L)).thenReturn(Optional.of(existingNut));
        when(mapper.toEntityFromRequestAdd(any())).thenReturn(new Nut());
        when(nutRepository.save(any())).thenReturn(existingNut);

        Nut result = nutServiceImp.save(dto);
        assertNotNull(result);}

    @SneakyThrows
    @Test
    void testSaveFromDto_withIdNull() {
        NutRequestForAdd dto = NutRequestForAdd.builder()
                .build();

        Nut existingNut = new Nut();
        existingNut.setPathToImage("/old/image.jpg");
        existingNut.setPathToSvg("/old/image.svg");

        when(mapper.toEntityFromRequestAdd(any())).thenReturn(new Nut());
        when(nutRepository.save(any())).thenReturn(existingNut);

        Nut result = nutServiceImp.save(dto);
        assertNotNull(result);}

    @Test
    void testGetAll() {
        List<Nut> nuts = List.of(new Nut(), new Nut());
        when(nutRepository.findAll()).thenReturn(nuts);

        List<Nut> result = nutServiceImp.getAll();
        assertEquals(2, result.size());
    }

    @Test
    void testGetAllInResponseForAdd() {
        Nut nut1 = new Nut();
        Nut nut2 = new Nut();

        NutResponseForAdd dto1 = NutResponseForAdd.builder().build();
        NutResponseForAdd dto2 = NutResponseForAdd.builder().build();

        when(nutRepository.findAll()).thenReturn(List.of(nut1, nut2));
        when(mapper.toResponseForAdd(nut1)).thenReturn(dto1);
        when(mapper.toResponseForAdd(nut2)).thenReturn(dto2);

        List<NutResponseForAdd> result = nutServiceImp.getAllInResponseForAdd();
        assertEquals(2, result.size());
    }

    @Test
    void testGetAll_withPagination() {
        Nut nut = new Nut();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Nut> page = new PageImpl<>(List.of(nut));

        NutResponseForView dto = NutResponseForView.builder().build();

        when(nutRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);
        when(mapper.toResponseForView(nut, LanguageCode.uk)).thenReturn(dto);

        PageResponse<NutResponseForView> result = nutServiceImp.getAll(dto, pageable, LanguageCode.uk);
        assertEquals(1, result.getContent().size());
    }

    @SneakyThrows
    @Test
    void testDeleteById() {
        Nut nut = new Nut();
        nut.setId(5L);
        nut.setPathToImage("/img.png");
        nut.setPathToSvg("/svg.svg");

        when(nutRepository.findById(5L)).thenReturn(Optional.of(nut));
        doNothing().when(imageService).deleteByPath(any());

        nutServiceImp.deleteById(5L);

        verify(imageService, times(2)).deleteByPath(any());
        verify(nutRepository).deleteById(5L);
    }

    @SneakyThrows
    @Test
    void testDeleteById_WherePathsAreEmpty() {
        Nut nut = new Nut();
        nut.setId(5L);

        when(nutRepository.findById(5L)).thenReturn(Optional.of(nut));
//        doNothing().when(imageService).deleteByPath(any());

        nutServiceImp.deleteById(5L);

//        verify(imageService, times(2)).deleteByPath(any());
        verify(nutRepository).deleteById(5L);
    }
}