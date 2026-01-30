package org.example.black_sea_walnut.service.specifications;

import jakarta.persistence.criteria.*;
import org.example.black_sea_walnut.dto.admin.nut.NutResponseForView;
import org.example.black_sea_walnut.entity.Nut;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class NutSpecificationTest {
    private Root<Nut> root;
    private CriteriaQuery<?> query;
    private CriteriaBuilder criteriaBuilder;

    @BeforeEach
    void setUp() {
        root = mock(Root.class);
        query = mock(CriteriaQuery.class);
        criteriaBuilder = mock(CriteriaBuilder.class);
    }

    @Test
    void testGetSpecification_withAllFields() {
        NutResponseForView dto = NutResponseForView.builder()
                .id(1L)
                .title("Test Title")
                .date("15.03.2024")
                .build();
        Join<Object, Object> join = mock(Join.class);
        when(root.join("translations")).thenReturn(join);
        when(criteriaBuilder.equal(any(), any())).thenReturn(mock(Predicate.class));
        when(criteriaBuilder.like(any(), (Expression<String>) any())).thenReturn(mock(Predicate.class));
        when(criteriaBuilder.and(any(), any())).thenReturn(mock(Predicate.class));
        Specification<Nut> spec = NutSpecification.getSpecification(dto, LanguageCode.uk);
        Predicate predicate = spec.toPredicate(root, query, criteriaBuilder);
        assertNotNull(predicate);
        verify(root, times(1)).get("id");
        verify(root, times(1)).join("translations");
        verify(root, times(1)).get("date");
    }

    @Test
    void testGetSpecification_withOnlyTitle() {

        NutResponseForView dto = NutResponseForView.builder().build();
        Join<Object, Object> join = mock(Join.class);
        when(root.join("translations")).thenReturn(join);
        when(criteriaBuilder.equal(any(), any())).thenReturn(mock(Predicate.class));
        when(criteriaBuilder.like(any(), (Expression<String>) any())).thenReturn(mock(Predicate.class));
        when(criteriaBuilder.and(any(), any())).thenReturn(mock(Predicate.class));
        Specification<Nut> spec = NutSpecification.getSpecification(dto, LanguageCode.uk);
        Predicate predicate = spec.toPredicate(root, query, criteriaBuilder);
    }

    @Test
    void testGetSpecification_withOnlyDate() {
        NutResponseForView dto = NutResponseForView.builder().build();
        Specification<Nut> spec = NutSpecification.getSpecification(dto, LanguageCode.uk);
        Predicate predicate = spec.toPredicate(root, query, criteriaBuilder);
    }

    @Test
    void testGetSpecification_withNoFields() {
        NutResponseForView dto = NutResponseForView.builder()
                .title("")
                .date("")
                .build();
        Specification<Nut> spec = NutSpecification.getSpecification(dto, LanguageCode.en);
        Predicate predicate = spec.toPredicate(root, query, criteriaBuilder);
        assertNotNull(spec);
    }
}