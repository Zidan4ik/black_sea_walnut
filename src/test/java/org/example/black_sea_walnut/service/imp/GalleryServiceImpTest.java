package org.example.black_sea_walnut.service.imp;

import jakarta.persistence.EntityNotFoundException;
import lombok.SneakyThrows;
import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.admin.gallery.GalleryRequestForAdd;
import org.example.black_sea_walnut.dto.admin.gallery.GalleryResponseForAdd;
import org.example.black_sea_walnut.entity.Gallery;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.enums.MediaType;
import org.example.black_sea_walnut.mapper.GalleryMapper;
import org.example.black_sea_walnut.repository.GalleryRepository;
import org.example.black_sea_walnut.service.ImageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GalleryServiceImpTest {
    @Mock
    private GalleryRepository galleryRepository;

    @Mock
    private ImageService imageService;

    @Mock
    private GalleryMapper galleryMapper;

    @InjectMocks
    private GalleryServiceImp galleryService;

    @Test
    void testSaveGallery() {
        Gallery gallery = new Gallery();
        when(galleryRepository.save(any(Gallery.class))).thenReturn(gallery);
        Gallery savedGallery = galleryService.save(gallery);
        assertNotNull(savedGallery);
        verify(galleryRepository, times(1)).save(gallery);
    }

    @Test
    void testGetAllGalleries() {
        List<Gallery> galleries = List.of(new Gallery(), new Gallery());
        when(galleryRepository.findAll()).thenReturn(galleries);
        List<Gallery> result = galleryService.getAll();
        assertEquals(2, result.size());
        verify(galleryRepository, times(1)).findAll();
    }

    @Test
    void testGetAllByLanguageCode() {
        LanguageCode code = LanguageCode.en;
        List<Gallery> galleries = List.of(new Gallery(), new Gallery());
        when(galleryRepository.findAllByLanguageCode(code)).thenReturn(galleries);
        List<Gallery> result = galleryService.getAllByLanguageCode(code);
        assertEquals(2, result.size());
        verify(galleryRepository, times(1)).findAllByLanguageCode(code);
    }

    @Test
    void testGetById_GalleryExists() {
        Long id = 1L;
        Gallery gallery = new Gallery();
        when(galleryRepository.findById(id)).thenReturn(Optional.of(gallery));
        Gallery result = galleryService.getById(id);
        assertNotNull(result);
        verify(galleryRepository, times(1)).findById(id);
    }

    @Test
    void testGetById_GalleryNotFound() {
        Long id = 1L;
        when(galleryRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> galleryService.getById(id));
        verify(galleryRepository, times(1)).findById(id);
    }

    @SneakyThrows
    @Test
    void testDeleteById() {
        Long id = 1L;
        Gallery gallery = new Gallery();
        when(galleryRepository.findById(id)).thenReturn(Optional.of(gallery));
        doNothing().when(galleryRepository).deleteById(id);
        galleryService.deleteById(id);
        verify(galleryRepository, times(1)).deleteById(id);
    }

    @SneakyThrows
    @Test
    void testDeleteById_whereIdIsExist() {
        Long id = 1L;
        Gallery gallery = new Gallery();
        gallery.setPathToMedia("media.jpeg");
        when(galleryRepository.findById(id)).thenReturn(Optional.of(gallery));
        doNothing().when(galleryRepository).deleteById(id);
        galleryService.deleteById(id);
        verify(galleryRepository, times(1)).deleteById(id);
    }

    @Test
    void testGetAllInResponseByLanguageCode() {
        LanguageCode code = LanguageCode.en;
        List<Gallery> galleries = List.of(new Gallery(), new Gallery());
        when(galleryRepository.findAllByLanguageCode(code)).thenReturn(galleries);
        List<GalleryResponseForAdd> result = galleryService.getAllInResponseByLanguageCode(code);
        assertEquals(2, result.size());
        verify(galleryRepository, times(1)).findAllByLanguageCode(code);
    }

    @Test
    void testGetAllInResponseByLanguageCode_WithPagination() {
        LanguageCode code = LanguageCode.en;
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());

        Gallery gallery1 = new Gallery();
        Gallery gallery2 = new Gallery();
        List<Gallery> galleries = List.of(gallery1, gallery2);
        Page<Gallery> page = new PageImpl<>(galleries, pageable, galleries.size());

        GalleryResponseForAdd response1 = GalleryResponseForAdd.builder().build();
        GalleryResponseForAdd response2 = GalleryResponseForAdd.builder().build();

        when(galleryRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);
        when(galleryMapper.toResponseForAdd(gallery1)).thenReturn(response1);
        when(galleryMapper.toResponseForAdd(gallery2)).thenReturn(response2);

        ArgumentCaptor<Specification<Gallery>> specCaptor = ArgumentCaptor.forClass(Specification.class);

        PageResponse<GalleryResponseForAdd> result = galleryService.getAllInResponseByLanguageCode(pageable, code);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(0, result.getMetadata().getPage());
        assertEquals(10, result.getMetadata().getSize());
        assertEquals(2, result.getMetadata().getTotalElements());

        verify(galleryRepository, times(1)).findAll(specCaptor.capture(), eq(pageable));
        verify(galleryMapper, times(1)).toResponseForAdd(gallery1);
        verify(galleryMapper, times(1)).toResponseForAdd(gallery2);

        Specification<Gallery> capturedSpec = specCaptor.getValue();
        assertNotNull(capturedSpec);

        verifyNoMoreInteractions(galleryRepository, galleryMapper);
    }

    @SneakyThrows
    @Test
    void testSaveGallery_WithNewFile() {
        GalleryRequestForAdd dto = GalleryRequestForAdd.builder()
                .id(1L)
                .file(new MockMultipartFile("file", "image.jpeg", "image/jpeg", new byte[]{1, 2, 3, 4}))
                .pathToMediaFile("files")
                .mediaType(MediaType.image)
                .build();

        Gallery mappedGallery = new Gallery();
        mappedGallery.setPathToMedia("gallery/image/sample.jpg");

        when(galleryRepository.findById(1L)).thenReturn(Optional.of(mappedGallery));
        when(galleryMapper.toEntityForRequestAdd(dto)).thenReturn(mappedGallery);
        when(galleryRepository.save(mappedGallery)).thenReturn(mappedGallery);
        doNothing().when(imageService).save(any(), any());

        Gallery savedGallery = galleryService.save(dto);

        assertNotNull(savedGallery);
        assertEquals("gallery/image/sample.jpg", savedGallery.getPathToMedia());

        verify(imageService, times(1)).save(any(), eq("gallery/image/sample.jpg"));
        verify(galleryRepository, times(1)).save(mappedGallery);
    }

    @SneakyThrows
    @Test
    void testSaveGallery_WithoutId() {
        GalleryRequestForAdd dto = GalleryRequestForAdd.builder()
                .id(null)
                .file(new MockMultipartFile("file", "image.jpeg", "image/jpeg", new byte[]{1, 2, 3, 4}))
                .pathToMediaFile("files")
                .mediaType(MediaType.image)
                .build();

        Gallery mappedGallery = new Gallery();
        mappedGallery.setPathToMedia("gallery/image/sample.jpg");

        when(galleryMapper.toEntityForRequestAdd(dto)).thenReturn(mappedGallery);
        when(galleryRepository.save(mappedGallery)).thenReturn(mappedGallery);
        doNothing().when(imageService).save(any(), any());

        Gallery savedGallery = galleryService.save(dto);

        assertNotNull(savedGallery);
        assertEquals("gallery/image/sample.jpg", savedGallery.getPathToMedia());

        verify(imageService, times(1)).save(any(), eq("gallery/image/sample.jpg"));
        verify(galleryRepository, times(1)).save(mappedGallery);
    }

    @SneakyThrows
    @Test
    void testSaveGallery_WithExistingGallery_AndEmptyMedia() {
        GalleryRequestForAdd dto = GalleryRequestForAdd.builder().id(1L).pathToMediaFile("").build();
        Gallery existingGallery = new Gallery();
        existingGallery.setPathToMedia("old_image.jpg");
        when(galleryMapper.toEntityForRequestAdd(dto)).thenReturn(existingGallery);
        when(galleryRepository.findById(1L)).thenReturn(Optional.of(existingGallery));
        when(galleryRepository.save(existingGallery)).thenReturn(existingGallery);
        doNothing().when(imageService).deleteByPath("old_image.jpg");
        Gallery savedGallery = galleryService.save(dto);
        assertNotNull(savedGallery);
        verify(imageService, times(1)).deleteByPath("old_image.jpg");
        verify(galleryRepository, times(1)).save(existingGallery);
    }

    @Test
    void save_ShouldHandleExceptionAndThrowRuntimeException() {
        GalleryRequestForAdd dto = GalleryRequestForAdd.builder().id(1L).mediaType(MediaType.image)
                .pathToMediaFile("").build();
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> galleryService.save(dto));
        assertEquals("Gallery with id: 1 was not found!", thrown.getMessage());
    }
}