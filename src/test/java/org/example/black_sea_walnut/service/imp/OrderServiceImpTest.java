package org.example.black_sea_walnut.service.imp;

import jakarta.persistence.EntityNotFoundException;
import org.example.black_sea_walnut.dto.admin.order.OrderResponseForStatsProducts;
import org.example.black_sea_walnut.dto.admin.order.ResponseOrderDetailForView;
import org.example.black_sea_walnut.dto.admin.order.ResponseOrderForAdd;
import org.example.black_sea_walnut.dto.web.checkout.CheckoutUser;
import org.example.black_sea_walnut.entity.OrderDetail;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.admin.order.ResponseOrderForView;
import org.example.black_sea_walnut.dto.web.OrderResponseForAccount;
import org.example.black_sea_walnut.entity.Order;
import org.example.black_sea_walnut.entity.User;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.mapper.OrderDetailMapper;
import org.example.black_sea_walnut.mapper.OrderMapper;
import org.example.black_sea_walnut.repository.OrderDetailsRepository;
import org.example.black_sea_walnut.repository.OrderRepository;
import org.example.black_sea_walnut.service.TransactionsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImpTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper mapper;

    @Mock
    private OrderDetailMapper orderDetailMapper;

    @Mock
    private OrderDetailsRepository orderDetailsRepository;

    @Mock
    private TransactionsService transactionsService;

    @InjectMocks
    private OrderServiceImp orderService;


    @Test
    void testGetAllOrders() {
        Order order = new Order();
        when(orderRepository.findAll()).thenReturn(List.of(order));

        List<Order> result = orderService.getAll();

        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testGetAllByUser() {
        User user = new User();
        user.setId(1L);

        Order order = new Order();
        when(orderRepository.findAllByUser(user)).thenReturn(List.of(order));
        when(mapper.toResponseForUserOrderView(order)).thenReturn(any());

        List<?> result = orderService.getAllByUser(user);

        assertEquals(1, result.size());
        verify(orderRepository).findAllByUser(user);
    }

    @Test
    void testGetAllPageable() {
        Pageable pageable = PageRequest.of(0, 10);
        Order order = new Order();
        Page<Order> page = new PageImpl<>(List.of(order));
        Specification<Order> specification = Specification.where(null);
        when(orderRepository.findAll(specification, pageable)).thenReturn(page);
        when(mapper.toResponseForAccount(any(Order.class))).thenReturn(mock(OrderResponseForAccount.class));

        PageResponse<OrderResponseForAccount> response = orderService.getAll(pageable, LanguageCode.uk);

        assertEquals(1, response.getContent().size());
        verify(orderRepository).findAll(specification, pageable);
    }

    @Test
    void testDeleteById() {
        Long id = 1L;
        doNothing().when(orderRepository).deleteById(id);

        orderService.deleteById(id);

        verify(orderRepository, times(1)).deleteById(id);
    }

    @Test
    void testGetByUserIdAndPersonalId_found() {
        Order order = new Order();
        order.setId(1L);
        order.setPersonalId(2L);
        order.setOrderDetails(List.of(new OrderDetail()));
        User user = new User();
        user.setId(1L);
        order.setUser(user);

        order.setOrderDetails(List.of(new OrderDetail()));
        when(orderRepository.findOrderByUserAndPersonalId(1L, 2L)).thenReturn(Optional.of(order));

        List<ResponseOrderDetailForView> result = orderService.getByUserIdAndPersonalId(1L, 2L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetByUserIdAndPersonalId_notFound() {
        when(orderRepository.findOrderByUserAndPersonalId(1L, 2L)).thenReturn(Optional.empty());

        List<ResponseOrderDetailForView> result = orderService.getByUserIdAndPersonalId(1L, 2L);

        assertNull(result);
    }

    @Test
    void testGetAll_withFilterAndPagination() {
        Page<Order> page = new PageImpl<>(List.of(new Order()));
        Pageable pageable = PageRequest.of(0, 10);
        when(orderRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);
        ResponseOrderForView dto = ResponseOrderForView.builder().build();
        when(mapper.toDTOView(any(Order.class))).thenReturn(dto);

        PageResponse<ResponseOrderForView> result = orderService.getAll(dto, pageable, LanguageCode.en);

        assertEquals(1, result.getContent().size());
    }

    @Test
    void testGetAll_byUser() {
        User user = new User();
        user.setId(1L);
        Page<Order> page = new PageImpl<>(List.of(new Order()));
        Pageable pageable = PageRequest.of(0, 10);
        when(orderRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);
        when(mapper.toResponseForAccount(any())).thenReturn(OrderResponseForAccount.builder().build());

        PageResponse<OrderResponseForAccount> result = orderService.getAll(user, pageable, LanguageCode.en);

        assertEquals(1, result.getContent().size());
    }

    @Test
    void testGetByIdInDTOAdd() {
        Order order = new Order();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(mapper.toDTOAdd(order)).thenReturn(ResponseOrderForAdd.builder().build());

        ResponseOrderForAdd result = orderService.getByIdInDTOAdd(1L);

        assertNotNull(result);
    }

    @Test
    void testGetById_found() {
        Order order = new Order();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order result = orderService.getById(1L);

        assertEquals(order, result);
    }

    @Test
    void testGetById_notFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> orderService.getById(1L));
        assertEquals("Order was not found!", ex.getMessage());
    }

    @Test
    void testSave() {
        Order order = new Order();
        orderService.save(order);
        verify(orderRepository).save(order);
    }

    @Test
    void testSaveAfterPayment() {
        CheckoutUser dto = new CheckoutUser();
        User user = new User();
        user.setId(1L);
        dto.setUser(user);
        Order order = new Order();

        when(mapper.toEntityAfterCheckout(dto)).thenReturn(order);

        orderService.saveAfterPayment(dto);
        verify(orderRepository).save(order);
    }

    @Test
    void testGetOrdersCountByDate() {
        LocalDate start = LocalDate.now().minusMonths(1);
        LocalDate end = LocalDate.now();
        when(orderRepository.countOrdersByMonth(start, end)).thenReturn(List.of(Map.of("month", "Jan", "count", 5)));

        List<Map<String, Object>> result = orderService.getOrdersCountByDate(start, end);

        assertEquals(1, result.size());
    }

    @Test
    void testGetTopProductBySalesInMonth() {
        LocalDate start = LocalDate.now().minusMonths(1);
        LocalDate end = LocalDate.now();
        Object[] raw = new Object[] { "Product1", BigDecimal.TEN };
        when(orderDetailsRepository.findSummaWithDiscountByProductAndDateRange(eq(start), eq(end), any(Pageable.class)))
                .thenReturn(new ArrayList<>());
        when(mapper.toResponseForStatsProduct(any())).thenReturn(List.of(OrderResponseForStatsProducts.builder().build()));

        List<OrderResponseForStatsProducts> result = orderService.getTopProductBySalesInMonth(start, end, 5);

        assertEquals(1, result.size());
    }
}