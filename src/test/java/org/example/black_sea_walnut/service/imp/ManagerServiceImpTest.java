package org.example.black_sea_walnut.service.imp;

import jakarta.persistence.EntityNotFoundException;
import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.admin.manager.ManagerDTO;
import org.example.black_sea_walnut.dto.admin.manager.ManagerResponseForView;
import org.example.black_sea_walnut.entity.Manager;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.mapper.ManagerMapper;
import org.example.black_sea_walnut.repository.ManagerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ManagerServiceImpTest {

    @Mock
    private ManagerRepository managerRepository;

    @Mock
    private ManagerMapper managerMapper;

    @InjectMocks
    private ManagerServiceImp managerService;

    private Manager manager;
    private ManagerDTO managerDTO;
    private ManagerResponseForView responseView;

    @BeforeEach
    void setUp() {
        manager = new Manager();
        manager.setId(1L);

        managerDTO = ManagerDTO.builder().build();
        responseView = ManagerResponseForView.builder().build();
    }

    @Test
    void testSaveEntity() {
        when(managerRepository.save(manager)).thenReturn(manager);
        Manager saved = managerService.save(manager);
        assertEquals(manager, saved);
        verify(managerRepository).save(manager);
    }

    @Test
    void testSaveDTO() {
        when(managerMapper.toEntityFromRequest(managerDTO)).thenReturn(manager);
        when(managerRepository.save(manager)).thenReturn(manager);

        Manager result = managerService.save(managerDTO);
        assertEquals(manager, result);
        verify(managerMapper).toEntityFromRequest(managerDTO);
        verify(managerRepository).save(manager);
    }

    @Test
    void testGetById_Found() {
        when(managerRepository.findById(1L)).thenReturn(Optional.of(manager));
        Manager result = managerService.getById(1L);
        assertEquals(manager, result);
    }

    @Test
    void testGetById_NotFound() {
        when(managerRepository.findById(1L)).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> managerService.getById(1L));
        assertEquals("Manager with id:1 was not found!", exception.getMessage());
    }

    @Test
    void testGetByIdInResponseForAdd() {
        when(managerRepository.findById(1L)).thenReturn(Optional.of(manager));
        when(managerMapper.toResponseForDTO(manager)).thenReturn(managerDTO);

        ManagerDTO result = managerService.getByIdInResponseForAdd(1L);
        assertEquals(managerDTO, result);
        verify(managerMapper).toResponseForDTO(manager);
    }

    @Test
    void testDeleteById() {
        doNothing().when(managerRepository).deleteById(1L);
        managerService.deleteById(1L);
        verify(managerRepository).deleteById(1L);
    }

    @Test
    void testGetAll() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<Manager> page = new PageImpl<>(List.of(manager), pageable, 1);
        LanguageCode lang = LanguageCode.uk;
        Specification<Manager> specification = Specification.where(null);
        when(managerRepository.findAll(specification, pageable)).thenReturn(page);
        when(managerMapper.toResponseForView(manager, lang)).thenReturn(responseView);

        PageResponse<ManagerResponseForView> response = managerService.getAll(responseView, pageable, lang);

        assertEquals(1, response.getContent().size());
        assertEquals(1, response.getMetadata().getTotalElements());
        assertEquals(1, response.getMetadata().getTotalPages());
        verify(managerRepository).findAll(specification, pageable);
        verify(managerMapper).toResponseForView(manager, lang);
    }
}