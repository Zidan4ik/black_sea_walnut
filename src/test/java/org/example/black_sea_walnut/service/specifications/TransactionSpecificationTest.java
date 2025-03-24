package org.example.black_sea_walnut.service.specifications;


import org.example.black_sea_walnut.dto.admin.transaction.ResponseTransactionForView;
import org.example.black_sea_walnut.entity.Transaction;
import org.example.black_sea_walnut.entity.User;
import org.example.black_sea_walnut.enums.PaymentStatus;
import org.example.black_sea_walnut.enums.PaymentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.persistence.criteria.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TransactionSpecificationTest {
    @Mock
    private Root<Transaction> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private Path<Object> idPath, datePath, customerPath, phonePath, emailPath, paymentTypePath, paymentStatusPath;

    @Mock
    private Predicate mockPredicate;

    private ResponseTransactionForView entity;

    @BeforeEach
    void setUp() {
        entity = ResponseTransactionForView.builder().build();
    }

    @Test
    void testGetSpecification_WithIdFilter() {
        entity.setId(123L);
        Specification<Transaction> specification = TransactionSpecification.getSpecification(entity);
        when(root.get("id")).thenReturn(idPath);
        when(criteriaBuilder.equal(idPath, 123L)).thenReturn(mockPredicate);
        Predicate result = specification.toPredicate(root, query, criteriaBuilder);
        assertNotNull(result);
        verify(criteriaBuilder).equal(idPath, 123L);
    }

    @Test
    void testGetSpecification_WithDateFilter() {
        String dateString = "23.03.2025";
        entity.setDate(dateString);
        LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        LocalDateTime startDay = date.atStartOfDay();
        LocalDateTime endDay = date.atTime(LocalTime.MAX);
        when(root.get("date")).thenReturn(datePath);
        Specification<Transaction> specification = TransactionSpecification.getSpecification(entity);
        Predicate result = specification.toPredicate(root, query, criteriaBuilder);
    }

    @Test
    void testGetSpecification_WithCustomerFilter() {
        entity.setCustomer("John Doe");
        when(root.get("customer")).thenReturn(customerPath);
        Specification<Transaction> specification = TransactionSpecification.getSpecification(entity);
        Predicate result = specification.toPredicate(root, query, criteriaBuilder);
    }

    @Test
    void testGetSpecification_WithPhoneFilter() {
        entity.setPhone("123456789");
        when(root.get("phone")).thenReturn(phonePath);
        Specification<Transaction> specification = TransactionSpecification.getSpecification(entity);
        Predicate result = specification.toPredicate(root, query, criteriaBuilder);
    }

    @Test
    void testGetSpecification_WithEmailFilter() {
        entity.setEmail("john@example.com");
        when(root.get("email")).thenReturn(emailPath);
        Specification<Transaction> specification = TransactionSpecification.getSpecification(entity);
        Predicate result = specification.toPredicate(root, query, criteriaBuilder);
    }

    @Test
    void testGetSpecification_WithTypeOfPaymentFilter() {
        entity.setTypeOfPayment("card");
        PaymentType paymentType = PaymentType.valueOf("card");
        when(root.get("paymentType")).thenReturn(paymentTypePath);
        when(criteriaBuilder.equal(paymentTypePath, paymentType)).thenReturn(mockPredicate);

        Specification<Transaction> specification = TransactionSpecification.getSpecification(entity);
        Predicate result = specification.toPredicate(root, query, criteriaBuilder);

        assertNotNull(result);
        verify(criteriaBuilder).equal(paymentTypePath, paymentType);
    }

    @Test
    void testGetSpecification_WithStatusPaymentFilter() {
        entity.setStatusPayment("inProcess");
        PaymentStatus paymentStatus = PaymentStatus.inProcess;
        when(root.get("paymentStatus")).thenReturn(paymentStatusPath);
        when(criteriaBuilder.equal(paymentStatusPath, paymentStatus)).thenReturn(mockPredicate);

        Specification<Transaction> specification = TransactionSpecification.getSpecification(entity);
        Predicate result = specification.toPredicate(root, query, criteriaBuilder);

        assertNotNull(result);
        verify(criteriaBuilder).equal(paymentStatusPath, paymentStatus);
    }

    @Test
    void testGetSpecification_WithoutFilters() {
        entity.setStatusPayment("inProcess");
        entity.setCustomer("");
        entity.setPhone("");
        entity.setEmail("");
        entity.setDate("");
        entity.setStatusPayment("");
        entity.setTypeOfPayment("");
        Specification<Transaction> specification = TransactionSpecification.getSpecification(entity);
        Predicate result = specification.toPredicate(root, query, criteriaBuilder);
    }

    @Test
    void testHasUserFilter() {
        User user = new User();
        when(root.get("user")).thenReturn(idPath);
        when(criteriaBuilder.equal(idPath, user)).thenReturn(mockPredicate);

        Specification<Transaction> specification = TransactionSpecification.hasUser(user);
        Predicate result = specification.toPredicate(root, query, criteriaBuilder);

        assertNotNull(result);
        verify(criteriaBuilder).equal(idPath, user);
    }
}