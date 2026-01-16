package org.example.black_sea_walnut.service.specifications;
import org.example.black_sea_walnut.dto.admin.user.UserResponseForView;
import org.example.black_sea_walnut.entity.User;
import org.example.black_sea_walnut.enums.RegisterType;
import org.example.black_sea_walnut.enums.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

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
class UserSpecificationTest {
    @Mock
    private Root<User> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private Path<Object> idPath, fullNamePath, emailPath, phonePath, datePath, registerTypePath, statusPath;

    @Mock
    private Predicate mockPredicate;

    private UserResponseForView dto;

    @BeforeEach
    void setUp() {
        dto = UserResponseForView.builder().build();
    }

    @Test
    void testGetSpecification_WithIdFilter() {
        dto.setId(123L);
        Specification<User> specification = UserSpecification.getSpecification(dto);

        when(root.get("id")).thenReturn(idPath);
        when(criteriaBuilder.equal(idPath, 123L)).thenReturn(mockPredicate);

        Predicate result = specification.toPredicate(root, query, criteriaBuilder);
        assertNotNull(result);
        verify(criteriaBuilder).equal(idPath, 123L);
    }

    @Test
    void testGetSpecification_WithFullNameFilter() {
        dto.setFullName("John Doe");
        Specification<User> specification = UserSpecification.getSpecification(dto);
        when(root.get("fullName")).thenReturn(fullNamePath);
        Predicate result = specification.toPredicate(root, query, criteriaBuilder);
    }

    @Test
    void testGetSpecification_WithEmailFilter() {
        dto.setEmail("john@example.com");
        Specification<User> specification = UserSpecification.getSpecification(dto);
        when(root.get("email")).thenReturn(emailPath);
        Predicate result = specification.toPredicate(root, query, criteriaBuilder);
    }

    @Test
    void testGetSpecification_WithPhoneFilter() {
        dto.setPhone("123456789");
        Specification<User> specification = UserSpecification.getSpecification(dto);
        when(root.get("phone")).thenReturn(phonePath);
        Predicate result = specification.toPredicate(root, query, criteriaBuilder);
    }

    @Test
    void testGetSpecification_WithoutFilters() {
        dto.setFullName("");
        dto.setEmail("");
        dto.setPhone("");
        dto.setDate("");
        dto.setUserStatus("");
        dto.setRegistrationType("");
        Specification<User> specification = UserSpecification.getSpecification(dto);
       specification.toPredicate(root, query, criteriaBuilder);
    }


    @Test
    void testGetSpecification_WithDateOfRegistrationFilter() {
        String dateString = "23.03.2025";
        dto.setDate(dateString);
        LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        LocalDateTime startDay = date.atStartOfDay();
        LocalDateTime endDay = date.atTime(LocalTime.MAX);
        when(root.get("dateRegistered")).thenReturn(datePath);
        Specification<User> specification = UserSpecification.getSpecification(dto);
        Predicate result = specification.toPredicate(root, query, criteriaBuilder);
    }

    @Test
    void testGetSpecification_WithUserStatusFilter() {
        dto.setUserStatus("isActive");
        UserStatus userStatus = UserStatus.isActive;

        when(root.get("status")).thenReturn(statusPath);
        when(criteriaBuilder.equal(statusPath, userStatus)).thenReturn(mockPredicate);

        Specification<User> specification = UserSpecification.getSpecification(dto);
        Predicate result = specification.toPredicate(root, query, criteriaBuilder);

        assertNotNull(result);
        verify(criteriaBuilder).equal(statusPath, userStatus);
    }

    @Test
    void testGetSpecification_WithRegistryTypeFilter() {
        dto.setRegistrationType("fop");
        RegisterType registerType = RegisterType.fop;

        when(root.get("registerType")).thenReturn(registerTypePath);
        when(criteriaBuilder.equal(registerTypePath, registerType)).thenReturn(mockPredicate);

        Specification<User> specification = UserSpecification.getSpecification(dto);
        Predicate result = specification.toPredicate(root, query, criteriaBuilder);

        assertNotNull(result);
        verify(criteriaBuilder).equal(registerTypePath, registerType);
    }

    @Test
    void testGetSpecification_WithMultipleFilters() {
        dto.setId(1L);
        dto.setEmail("john@example.com");
        dto.setUserStatus("isActive");
        Specification<User> specification = UserSpecification.getSpecification(dto);
        when(root.get("id")).thenReturn(idPath);
        when(root.get("email")).thenReturn(emailPath);
        when(root.get("status")).thenReturn(statusPath);
        when(criteriaBuilder.equal(idPath, 1L)).thenReturn(mockPredicate);
        when(criteriaBuilder.equal(statusPath, UserStatus.isActive)).thenReturn(mockPredicate);
        Predicate result = specification.toPredicate(root, query, criteriaBuilder);
        verify(criteriaBuilder).equal(idPath, 1L);
        verify(criteriaBuilder).equal(statusPath, UserStatus.isActive);
    }
}