package org.example.black_sea_walnut.service.specifications;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.criteria.*;
import org.example.black_sea_walnut.dto.admin.product.ProductResponseForShopPage;
import org.example.black_sea_walnut.entity.Product;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.jpa.domain.Specification;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ProductSpecification2Test {
    @Mock
    private Root<Product> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private Join<Object, Object> tasteTable;

    @Mock
    private Path<Object> idPath, tasteNamePath, tasteLanguageCodePath, massPath;

    private ProductResponseForShopPage entity;
    private LanguageCode languageCode;

    @BeforeEach
    void setUp() {
        entity = ProductResponseForShopPage.builder().build();
        languageCode = LanguageCode.en;
    }

    @Test
    void testGetSpecification_WithTasteAndMassFilter() {
        entity.setTasteFilter("Sweet");
        entity.setMassFilter("500g");

        Specification<Product> specification = ProductSpecification2.getSpecification(entity, languageCode);

        Predicate mockPredicate = mock(Predicate.class);
        when(criteriaBuilder.and(any(), any())).thenReturn(mockPredicate);
        when(criteriaBuilder.equal(any(), any())).thenReturn(mockPredicate);
        when(root.get("mass")).thenReturn(massPath);

        // Додати мок для join, щоб уникнути NullPointerException
        when(root.join("tastes", JoinType.LEFT)).thenReturn(tasteTable);
        when(tasteTable.get("name")).thenReturn(tasteNamePath);
        when(tasteTable.get("languageCode")).thenReturn(tasteLanguageCodePath);

        Predicate resultPredicate = specification.toPredicate(root, query, criteriaBuilder);

        assertNotNull(resultPredicate);
        verify(criteriaBuilder).equal(massPath, "500g");
        verify(criteriaBuilder).equal(tasteNamePath, "Sweet");
        verify(criteriaBuilder).equal(tasteLanguageCodePath, languageCode);
    }

    @Test
    void testGetSpecification_WithOnlyTasteFilter() {
        entity.setTasteFilter("Sweet");
        entity.setMassFilter(null);
        Specification<Product> specification = ProductSpecification2.getSpecification(entity, languageCode);
        Predicate mockPredicate = mock(Predicate.class);
        when(root.join("tastes", JoinType.LEFT)).thenReturn(tasteTable);
        when(tasteTable.get("name")).thenReturn(tasteNamePath);
        when(tasteTable.get("languageCode")).thenReturn(tasteLanguageCodePath);
        Predicate resultPredicate = specification.toPredicate(root, query, criteriaBuilder);
        verify(criteriaBuilder).equal(tasteLanguageCodePath, languageCode);
        verify(criteriaBuilder).equal(tasteNamePath, "Sweet");
    }

    @Test
    void testHasTaste_WithEmptyTaste() {
        Specification<Product> specification = ProductSpecification2.getSpecification(ProductResponseForShopPage.builder()
        .massFilter("").build(), languageCode);
        specification.toPredicate(root, query, criteriaBuilder);
    }

    @Test
    void testHasMass() {
        Specification<Product> massSpecification = ProductSpecification2.hasMass("500g");
        when(root.get("mass")).thenReturn(massPath);
        Predicate massPredicate = massSpecification.toPredicate(root, query, criteriaBuilder);
        verify(criteriaBuilder).equal(massPath, "500g");
    }

    @Test
    void testGetSpecification_WithNullFilters() {
        entity.setTasteFilter(null);
        entity.setMassFilter(null);
        Specification<Product> specification = ProductSpecification2.getSpecification(entity, languageCode);
        assertNull(specification.toPredicate(root, query, criteriaBuilder));
    }

}