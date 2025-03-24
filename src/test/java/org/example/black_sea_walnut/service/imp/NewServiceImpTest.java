package org.example.black_sea_walnut.service.imp;

import jakarta.persistence.EntityNotFoundException;
import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.admin.new_.NewRequestForAdd;
import org.example.black_sea_walnut.dto.admin.new_.ResponseNewForView;
import org.example.black_sea_walnut.dto.web.NewResponseInWeb;
import org.example.black_sea_walnut.dto.web.ResponseNewForViewInWeb;
import org.example.black_sea_walnut.entity.New;
import org.example.black_sea_walnut.entity.translation.NewTranslation;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.enums.MediaType;
import org.example.black_sea_walnut.repository.NewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NewServiceImpTest {
    @Mock
    private NewRepository newRepository;

    @Mock
    private ImageServiceImp imageServiceImp;

    @InjectMocks
    private NewServiceImp newServiceImp;

    @Captor
    ArgumentCaptor<New> newCaptor;

    NewTranslation nt1;

    @BeforeEach
    void setup() {
        nt1 = new NewTranslation();
        nt1.setLanguageCode(LanguageCode.en);
        nt1.setTitle("translator");
        nt1.setDescription("description");
    }

    @Test
    void testGetAll_withFilters() {
        Pageable pageable = PageRequest.of(0, 10);
        New entity = new New();
        entity.setDateOfPublication(LocalDate.now());
        entity.setTranslations(List.of(nt1));
        Page<New> page = new PageImpl<>(List.of(entity));
        Specification<New> specification = Specification.where(null);
        when(newRepository.findAll(specification, pageable)).thenReturn(page);
        ResponseNewForView dto = ResponseNewForView.builder().build();
        PageResponse<ResponseNewForView> response = newServiceImp.getAll(dto, pageable, LanguageCode.en);
        assertEquals(1, response.getContent().size());
    }

    @Test
    void testGetAllForWeb() {
        Pageable pageable = PageRequest.of(0, 10);
        New entity = new New();
        entity.setDateOfPublication(LocalDate.now());
        entity.setTranslations(List.of(nt1));
        Page<New> page = new PageImpl<>(List.of(entity));
        when(newRepository.findAll(pageable)).thenReturn(page);

        PageResponse<ResponseNewForViewInWeb> response = newServiceImp.getAll(pageable, LanguageCode.en);
        assertEquals(1, response.getContent().size());
    }

    @Test
    void testGetById_exists() {
        New entity = new New();
        entity.setId(1L);
        when(newRepository.findById(1L)).thenReturn(Optional.of(entity));
        assertEquals(entity, newServiceImp.getById(1L));
    }

    @Test
    void testGetById_notFound() {
        when(newRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> newServiceImp.getById(2L));
    }

    @Test
    void testGetByIdLikeDTO() {
        New entity = new New();
        entity.setId(3L);
        entity.setDateOfPublication(LocalDate.now());
        entity.setTranslations(List.of(nt1));
        when(newRepository.findById(3L)).thenReturn(Optional.of(entity));

        NewRequestForAdd dto = newServiceImp.getByIdLikeDTO(3L);
        assertNotNull(dto);
    }

    @Test
    void testGetByIdInResponseForWeb() {
        New entity = new New();
        entity.setId(4L);
        entity.setTranslations(List.of(nt1));
        entity.setDateOfPublication(LocalDate.now());
        when(newRepository.findById(4L)).thenReturn(Optional.of(entity));
        NewResponseInWeb response = newServiceImp.getByIdInResponseForWeb(4L, LanguageCode.en);
        assertNotNull(response);
    }

    @Test
    void testGetAllBySizeAmongLast() {
        New n = new New();
        n.setTranslations(List.of(nt1));
        n.setDateOfPublication(LocalDate.now());
        when(newRepository.getNewsThreeLast(3, 10L)).thenReturn(List.of(n));
        List<NewResponseInWeb> result = newServiceImp.getAllBySizeAmongLast(3, LanguageCode.en, 10L);
        assertEquals(1, result.size());
    }

    @Test
    void testGetAll() {
        when(newRepository.findAll()).thenReturn(List.of(new New()));
        List<New> result = newServiceImp.getAll();
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllInResponseForAdd() {
        New aNew = new New();
        aNew.setDateOfPublication(LocalDate.now());
        aNew.setTranslations(List.of(nt1));
        when(newRepository.findAll()).thenReturn(List.of(aNew));
        List<NewRequestForAdd> result = newServiceImp.getAllInResponseForAdd();
        assertEquals(1, result.size());
    }

    @Test
    void testSave_create() {
        New newEntity = new New();
        when(newRepository.save(any(New.class))).thenAnswer(inv -> inv.getArgument(0));
        New saved = newServiceImp.save(newEntity);
        assertNotNull(saved);
    }

    @Test
    void testSave_update_existing() {
        New existing = new New();
        existing.setId(5L);
        existing.setTranslations(new ArrayList<>());

        New input = new New();
        input.setId(5L);
        input.setTranslations(List.of());

        when(newRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(newRepository.save(any(New.class))).thenAnswer(inv -> inv.getArgument(0));

        New saved = newServiceImp.save(input);
        assertEquals(5L, saved.getId());
    }

    @Test
    void testSaveLikeDto() {
        NewRequestForAdd dto = NewRequestForAdd.builder().build();
        dto.setDateOfPublication("25.12.2003");
        New newEntity = new New();
        newEntity.setDateOfPublication(LocalDate.now());
        newEntity.setTranslations(List.of(nt1));
        when(newRepository.save(any(New.class))).thenReturn(newEntity);
        New result = newServiceImp.saveLikeDto(dto);
        assertNotNull(result);
    }

    @Test
    void testSaveImage_withFileAndPath() throws IOException {
        NewRequestForAdd dto = NewRequestForAdd.builder().build();
        dto.setId(10L);
        dto.setMediaType(MediaType.image);
        dto.setPathToImage("");
        dto.setFile(mock(org.springframework.web.multipart.MultipartFile.class));
        dto.setDateOfPublication("25.12.2003");

        New entity = new New();
        entity.setId(10L);
        entity.setPathToMedia("/old/image.jpg");
        entity.setDateOfPublication(LocalDate.now());
        entity.setTranslations(new ArrayList<>(List.of(nt1)));

        when(newRepository.findById(10L)).thenReturn(Optional.of(entity));
        when(imageServiceImp.generateFileName(any())).thenReturn("generated.jpg");
        when(newRepository.save(any(New.class))).thenReturn(entity);

        doNothing().when(imageServiceImp).save(any(), any());
        doNothing().when(imageServiceImp).deleteByPath(any());

        New result = newServiceImp.saveImage(dto);
        assertEquals(10L, result.getId());
    }

    @Test
    void testSaveImage_withFileAndPathNotEmpty() throws IOException {
        NewRequestForAdd dto = NewRequestForAdd.builder().build();
        dto.setId(10L);
        dto.setMediaType(MediaType.image);
        dto.setPathToImage("/path/to/image");
        dto.setFile(mock(org.springframework.web.multipart.MultipartFile.class));
        dto.setDateOfPublication("25.12.2003");

        New entity = new New();
        entity.setId(10L);
        entity.setPathToMedia("/old/image.jpg");
        entity.setDateOfPublication(LocalDate.now());
        entity.setTranslations(new ArrayList<>(List.of(nt1)));

        when(newRepository.findById(10L)).thenReturn(Optional.of(entity));
        when(imageServiceImp.generateFileName(any())).thenReturn("generated.jpg");
        when(newRepository.save(any(New.class))).thenReturn(entity);

        doNothing().when(imageServiceImp).save(any(), any());

        New result = newServiceImp.saveImage(dto);
        assertEquals(10L, result.getId());
    }

    @Test
    void testSaveImage_WhereIdIsNull() throws IOException {
        NewRequestForAdd dto = NewRequestForAdd.builder().build();
        dto.setFile(null);
        dto.setMediaType(MediaType.image);
        dto.setPathToImage("");
        dto.setDateOfPublication("25.12.2003");

        New entity = new New();
        entity.setId(10L);
        entity.setPathToMedia("/old/image.jpg");
        entity.setDateOfPublication(LocalDate.now());
        entity.setTranslations(new ArrayList<>(List.of(nt1)));

        when(newRepository.save(any(New.class))).thenReturn(entity);

        doNothing().when(imageServiceImp).save(any(), any());

        New result = newServiceImp.saveImage(dto);
        assertEquals(10L, result.getId());
    }

    @Test
    void testDeleteById() {
        doNothing().when(newRepository).deleteById(1L);
        newServiceImp.deleteById(1L);
        verify(newRepository, times(1)).deleteById(1L);
    }
}