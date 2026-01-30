package org.example.black_sea_walnut.service.specifications;

import jakarta.persistence.criteria.*;
import org.example.black_sea_walnut.dto.admin.order.ResponseOrderForView;
import org.example.black_sea_walnut.entity.Order;
import org.example.black_sea_walnut.entity.User;
import org.example.black_sea_walnut.enums.DeliveryStatus;
import org.example.black_sea_walnut.enums.OrderStatus;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class OrderSpecificationTest {
    @Test
    void testGetSpecificationWithAllFields() {
        ResponseOrderForView response = ResponseOrderForView.builder()
                .id(1L)
                .fullName("John Doe")
                .email("john@example.com")
                .totalPrice(5)
                .totalCount(100)
                .statusDelivery(DeliveryStatus.await)
                .statusOrder(OrderStatus.new_)
                .isPay(true)
                .date("01.03.2024").build();

        Specification<Order> specification = OrderSpecification.getSpecification(response);
        assertNotNull(specification);

        // Створюємо моки для Predicate, CriteriaBuilder, Root і т.д.
        Root<Order> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);

        when(cb.equal(any(), any())).thenReturn(mock(Predicate.class));
        when(cb.like(any(Expression.class), anyString())).thenReturn(mock(Predicate.class));
        specification.toPredicate(root, query, cb);
    }

    @Test
    void testGetSpecificationWithOnlyId() {
        ResponseOrderForView response = ResponseOrderForView.builder()
                .id(10L)
                .build();

        Specification<Order> specification = OrderSpecification.getSpecification(response);
        assertNotNull(specification);

        Root<Order> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);

        Path<Long> path = mock(Path.class);
        when(cb.equal(path, 10L)).thenReturn(mock(Predicate.class));

        specification.toPredicate(root, query, cb);
    }

    @Test
    void testGetSpecificationWithDate() {
        ResponseOrderForView response = ResponseOrderForView.builder()
                .date("15.01.2024")
                .fullName("")
                .email("")
                .date("")
                .build();

        Specification<Order> specification = OrderSpecification.getSpecification(response);
        assertNotNull(specification);

        Root<Order> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);

        Path<LocalDate> path = mock(Path.class);
        LocalDate expectedDate = LocalDate.parse("15.01.2024", DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        when(cb.equal(path, expectedDate)).thenReturn(mock(Predicate.class));

        specification.toPredicate(root, query, cb);
    }

    @Test
    void testHasUserSpecification() {
        User user = new User();
        user.setId(5L);
        Specification<Order> specification = OrderSpecification.hasUser(user);
        assertNotNull(specification);
        Root<Order> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Path<User> userPath = mock(Path.class);
        Predicate expectedPredicate = mock(Predicate.class);
        when(cb.equal(userPath, user)).thenReturn(expectedPredicate);
        Predicate predicate = specification.toPredicate(root, query, cb);
        verify(root).get("user");
    }

}