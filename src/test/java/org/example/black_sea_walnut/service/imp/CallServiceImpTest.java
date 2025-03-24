package org.example.black_sea_walnut.service.imp;

import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.admin.calls.CallResponseForView;
import org.example.black_sea_walnut.entity.Call;
import org.example.black_sea_walnut.mapper.CallMapper;
import org.example.black_sea_walnut.repository.CallRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CallServiceImpTest {
    @Mock
    private CallRepository callRepository;

    @Mock
    private CallMapper callMapper;

    @InjectMocks
    private CallServiceImp callService;

    private Call call;
    private CallResponseForView callResponseForView;

    @BeforeEach
    void setUp() {
        call = new Call();
        callResponseForView = CallResponseForView.builder().build();
    }

    @Test
    void getAll_ReturnsAllCalls() {
        when(callRepository.findAll()).thenReturn(Collections.singletonList(call));
        List<Call> result = callService.getAll();
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(callRepository, times(1)).findAll();
    }

    @Test
    void getAll_WithFilters_ReturnsPaginatedResponse() {
        Specification<Call> specification = Specification.where(null);
        Pageable pageable = mock(Pageable.class);
        Page<Call> callPage = new PageImpl<>(Collections.singletonList(call));
        when(callRepository.findAll(specification, pageable)).thenReturn(callPage);
        when(callMapper.toResponseForView(any(Call.class))).thenReturn(callResponseForView);
        PageResponse<CallResponseForView> response = callService.getAll(callResponseForView, pageable);
        assertNotNull(response);
        assertEquals(1, response.getMetadata().getTotalElements());
        verify(callRepository, times(1)).findAll(specification, pageable);
    }

    @Test
    void deleteById_DeletesCall() {
        Long id = 1L;
        callService.deleteById(id);
        verify(callRepository, times(1)).deleteById(id);
    }

    @Test
    void save_SavesCall() {
        when(callMapper.toEntityForSaveCall(any(CallResponseForView.class))).thenReturn(call);
        callService.save(callResponseForView);
        verify(callRepository, times(1)).save(any(Call.class));
    }
}