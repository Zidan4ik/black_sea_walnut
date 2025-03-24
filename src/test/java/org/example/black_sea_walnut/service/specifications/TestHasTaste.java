package org.example.black_sea_walnut.service.specifications;

import org.example.black_sea_walnut.entity.Product;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.persistence.criteria.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TestHasTaste {
    @Mock
    private Root<Product> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private Join<Object, Object> tasteTable;

    @Mock
    private Path<Object> idPath, tasteNamePath, tasteLanguageCodePath;

    @Mock
    private Predicate predicate1, predicate2, predicate3;

    @Test
    void testHasTaste_WithNonEmptyTaste() {
        String taste = "Sweet";
        LanguageCode languageCode = LanguageCode.en;
        when(root.join("tastes", JoinType.LEFT)).thenReturn(tasteTable);
        when(tasteTable.get("name")).thenReturn(tasteNamePath);
        when(tasteTable.get("languageCode")).thenReturn(tasteLanguageCodePath);
        when(criteriaBuilder.equal(tasteLanguageCodePath, languageCode)).thenReturn(predicate1);
        when(criteriaBuilder.equal(tasteNamePath, taste)).thenReturn(predicate2);
        when(criteriaBuilder.and(predicate1, predicate2)).thenReturn(predicate3);
        Specification<Product> specification = ProductSpecification2.hasTaste(taste, languageCode);
        Predicate resultPredicate = specification.toPredicate(root, query, criteriaBuilder);
        assertNotNull(resultPredicate);
        assertEquals(predicate3, resultPredicate);
        verify(root).join("tastes", JoinType.LEFT);
        verify(criteriaBuilder).equal(tasteLanguageCodePath, languageCode);
        verify(criteriaBuilder).equal(tasteNamePath, taste);
        verify(criteriaBuilder).and(predicate1, predicate2);
    }

    @Test
    void testHasTaste_WithEmptyTaste() {
        String taste = "";
        LanguageCode languageCode = LanguageCode.en;
        when(root.get("id")).thenReturn(idPath);
        when(criteriaBuilder.asc(idPath)).thenReturn(mock(Order.class));
        when(root.join("tastes", JoinType.LEFT)).thenReturn(tasteTable);
        when(tasteTable.get("languageCode")).thenReturn(tasteLanguageCodePath);
        when(criteriaBuilder.isNull(tasteLanguageCodePath)).thenReturn(predicate1);
        when(criteriaBuilder.equal(tasteLanguageCodePath, languageCode)).thenReturn(predicate2);
        when(criteriaBuilder.or(predicate1, predicate2)).thenReturn(predicate3);
        Specification<Product> specification = ProductSpecification2.hasTaste(taste, languageCode);
        Predicate resultPredicate = specification.toPredicate(root, query, criteriaBuilder);
        assertNotNull(resultPredicate);
        assertEquals(predicate3, resultPredicate);
        verify(query).orderBy(any(Order.class));
        verify(criteriaBuilder).isNull(tasteLanguageCodePath);
        verify(criteriaBuilder).equal(tasteLanguageCodePath, languageCode);
        verify(criteriaBuilder).or(predicate1, predicate2);
    }
}
