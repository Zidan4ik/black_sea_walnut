package org.example.black_sea_walnut.service.imp;

import jakarta.persistence.EntityNotFoundException;
import org.example.black_sea_walnut.dto.admin.order.ResponseOrderDetailForView;
import org.example.black_sea_walnut.entity.Order;
import org.example.black_sea_walnut.entity.OrderDetail;
import org.example.black_sea_walnut.mapper.OrderDetailMapper;
import org.example.black_sea_walnut.repository.OrderDetailsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderDetailServiceImpTest {
    @Mock
    private OrderDetailsRepository repository;
    @InjectMocks
    private OrderDetailServiceImp service;

    @BeforeEach
    void setUp() {
        repository = mock(OrderDetailsRepository.class);
        service = new OrderDetailServiceImp(repository);
    }

    @Test
    void getAllInResponseForViewInPageByOrder_shouldMapAll() {
        Order order = mock(Order.class);
        when(order.getId()).thenReturn(1L);

        OrderDetail orderDetail1 = new OrderDetail();
        OrderDetail orderDetail2 = new OrderDetail();

        when(repository.getAllByOrder(order)).thenReturn(List.of(orderDetail1, orderDetail2));

        List<ResponseOrderDetailForView> result = service.getAllInResponseForViewInPageByOrder(order);

        assertEquals(2, result.size());
    }

    @Test
    void decreaseAmountById_shouldDeleteWhenCountIsOne() {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setCount(1);

        when(repository.findById(1L)).thenReturn(Optional.of(orderDetail));

        service.decreaseAmountById(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void decreaseAmountById_shouldDecreaseCount() {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setCount(5);

        when(repository.findById(1L)).thenReturn(Optional.of(orderDetail));

        service.decreaseAmountById(1L);

        assertEquals(4, orderDetail.getCount());
        verify(repository, never()).deleteById(any());
    }

    @Test
    void increaseAmountById_shouldIncreaseCount() {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setCount(2);

        when(repository.findById(1L)).thenReturn(Optional.of(orderDetail));

        service.increaseAmountById(1L);

        assertEquals(3, orderDetail.getCount());
    }

    @Test
    void save_shouldCallRepositorySave() {
        OrderDetail detail = new OrderDetail();

        service.save(detail);

        verify(repository).save(detail);
    }

    @Test
    void getById_shouldReturnDetail() {
        OrderDetail detail = new OrderDetail();
        when(repository.findById(1L)).thenReturn(Optional.of(detail));

        OrderDetail result = service.getById(1L);

        assertSame(detail, result);
    }

    @Test
    void getById_shouldThrowExceptionWhenNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> service.getById(1L));
        assertEquals("OrderDetail with id: 1 was not found!", ex.getMessage());
    }

    @Test
    void deleteById_shouldCallRepositoryDelete() {
        service.deleteById(1L);
        verify(repository).deleteById(1L);
    }
}