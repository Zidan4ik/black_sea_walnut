package org.example.black_sea_walnut.service.specifications;

import org.example.black_sea_walnut.dto.admin.product.ProductResponseForViewInProducts;
import org.example.black_sea_walnut.entity.HistoryPrices;
import org.example.black_sea_walnut.entity.Product;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.*;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ProductSpecificationTest {
    private ProductResponseForViewInProducts productDTO;
    private Root<Product> root;
    private CriteriaQuery<?> query;
    private CriteriaBuilder criteriaBuilder;
    private Join<Object, Object> tasteTable;
    private Join<Object, Object> discountTable;
    private Join<Object, Object> translations;

    @BeforeEach
    void setUp() {
        productDTO = ProductResponseForViewInProducts.builder().build();
        root = mock(Root.class);
        query = mock(CriteriaQuery.class);
        criteriaBuilder = mock(CriteriaBuilder.class);
        tasteTable = mock(Join.class);
        discountTable = mock(Join.class);
        translations = mock(Join.class);

        when(root.join("tastes", JoinType.LEFT)).thenReturn(tasteTable);
        when(root.join("discounts", JoinType.LEFT)).thenReturn(discountTable);
        when(root.join("productTranslations")).thenReturn(translations);
    }

    @Test
    void shouldReturnSpecificationWhenIdIsProvided() {
        productDTO.setId(1L);
        Specification<Product> specification = ProductSpecification.getSpecification(productDTO, LanguageCode.en);
        assertThat(specification).isNotNull();
    }

    @Test
    void shouldReturnSpecificationWhenNameIsProvided() {
        productDTO.setName("Walnut");
        Specification<Product> specification = ProductSpecification.getSpecification(productDTO, LanguageCode.en);
        assertThat(specification).isNotNull();
    }

    @Test
    void shouldReturnSpecificationWhenPriceByUnitIsProvided() {
        productDTO.setPriceByUnit("100");
        Specification<Product> specification = ProductSpecification.getSpecification(productDTO, LanguageCode.en);
        assertThat(specification).isNotNull();
    }

    @Test
    void shouldReturnSpecificationWhenTasteIsProvided() {
        productDTO.setTaste("Sweet");
        Specification<Product> specification = ProductSpecification.getSpecification(productDTO, LanguageCode.en);
        assertThat(specification).isNotNull();
    }

    @Test
    void shouldReturnSpecificationWhenDiscountIsProvided() {
        productDTO.setDiscount("10%");
        Specification<Product> specification = ProductSpecification.getSpecification(productDTO, LanguageCode.en);
        assertThat(specification).isNotNull();
    }

    @Test
    void shouldReturnDefaultSpecificationWhenNoCriteriaProvided() {
        productDTO.setName("");
        productDTO.setPriceByUnit("");
        Specification<Product> specification = ProductSpecification.getSpecification(productDTO, LanguageCode.en);
        assertThat(specification).isNotNull();
    }

    @Test
    void shouldReturnPredicateWhenTasteIsNotEmpty() {
        String taste = "Sweet";
        LanguageCode code = LanguageCode.en;

        when(criteriaBuilder.equal(tasteTable.get("languageCode"), code)).thenReturn(mock(Predicate.class));
        when(criteriaBuilder.equal(tasteTable.get("name"), taste)).thenReturn(mock(Predicate.class));
        when(criteriaBuilder.and(any(), any())).thenReturn(mock(Predicate.class));

        Specification<Product> spec = ProductSpecification.hasTaste(taste, code);
        Predicate predicate = spec.toPredicate(root, query, criteriaBuilder);

        assertThat(predicate).isNotNull();
    }

    @Test
    void shouldReturnPredicateWhenTasteIsEmpty() {
        String taste = "";
        LanguageCode code = LanguageCode.en;

        when(criteriaBuilder.asc(root.get("id"))).thenReturn(mock(Order.class));
        when(criteriaBuilder.isNull(tasteTable.get("languageCode"))).thenReturn(mock(Predicate.class));
        when(criteriaBuilder.equal(tasteTable.get("languageCode"), code)).thenReturn(mock(Predicate.class));
        when(criteriaBuilder.or(any(), any())).thenReturn(mock(Predicate.class));

        Specification<Product> spec = ProductSpecification.hasTaste(taste, code);
        Predicate predicate = spec.toPredicate(root, query, criteriaBuilder);

        assertThat(predicate).isNotNull();
        verify(query).orderBy(any(Order.class));
    }

    @Test
    void shouldReturnPredicateWhenDiscountIsNotEmpty() {
        String discount = "20%";
        LanguageCode code = LanguageCode.en;

        when(criteriaBuilder.equal(discountTable.get("languageCode"), code)).thenReturn(mock(Predicate.class));
        when(criteriaBuilder.equal(discountTable.get("name"), discount)).thenReturn(mock(Predicate.class));
        when(criteriaBuilder.and(any(), any())).thenReturn(mock(Predicate.class));

        Specification<Product> spec = ProductSpecification.hasDiscount(discount, code);
        Predicate predicate = spec.toPredicate(root, query, criteriaBuilder);

        assertThat(predicate).isNotNull();
    }

    @Test
    void shouldReturnPredicateWhenDiscountIsEmpty() {
        String discount = "";
        LanguageCode code = LanguageCode.en;

        when(criteriaBuilder.asc(root.get("id"))).thenReturn(mock(Order.class));
        when(criteriaBuilder.isNull(discountTable.get("languageCode"))).thenReturn(mock(Predicate.class));
        when(criteriaBuilder.equal(discountTable.get("languageCode"), code)).thenReturn(mock(Predicate.class));
        when(criteriaBuilder.or(any(), any())).thenReturn(mock(Predicate.class));

        Specification<Product> spec = ProductSpecification.hasDiscount(discount, code);
        Predicate predicate = spec.toPredicate(root, query, criteriaBuilder);

        assertThat(predicate).isNotNull();
        verify(query).orderBy(any(Order.class));
    }

    @Test
    void shouldReturnPredicateWhenIdIsProvided() {
        Long id = 1L;
        Specification<Product> spec = ProductSpecification.hasId(id);
        Predicate predicate = spec.toPredicate(root, query, criteriaBuilder);
    }

    @Test
    void shouldReturnPredicateWhenNameIsProvided() {
        String name = "Walnut";
        LanguageCode code = LanguageCode.en;

        when(criteriaBuilder.equal(translations.get("languageCode"), code)).thenReturn(mock(Predicate.class));
        when(criteriaBuilder.like(translations.get("name"), "%" + name + "%")).thenReturn(mock(Predicate.class));
        when(criteriaBuilder.and(any(), any())).thenReturn(mock(Predicate.class));

        Specification<Product> spec = ProductSpecification.likeName(name, code);
        Predicate predicate = spec.toPredicate(root, query, criteriaBuilder);

        assertThat(predicate).isNotNull();
    }

    @Test
    void shouldReturnPredicateWhenPriceByUnitIsProvided() {
        int priceByUnit = 100;
        Subquery<LocalDateTime> maxValidFromSubquery = mock(Subquery.class);
        Subquery<Integer> currentPriceSubquery = mock(Subquery.class);
        Root<HistoryPrices> historyRoot = mock(Root.class);
        Path<LocalDateTime> validFromPath = mock(Path.class);
        Path<LocalDateTime> maxValidFrom = mock(Path.class);
        Path<Integer> currentPricePath = mock(Path.class);
        when(query.subquery(LocalDateTime.class)).thenReturn(maxValidFromSubquery);
        when(maxValidFromSubquery.from(HistoryPrices.class)).thenReturn(historyRoot);

        doReturn(validFromPath).when(historyRoot).get("validFrom");
        doReturn(maxValidFrom).when(criteriaBuilder).greatest(validFromPath);
        doReturn(maxValidFromSubquery).when(maxValidFromSubquery).select(maxValidFrom);
        doReturn(maxValidFromSubquery).when(maxValidFromSubquery).where(any(Predicate.class));

        when(query.subquery(Integer.class)).thenReturn(currentPriceSubquery);
        when(currentPriceSubquery.from(HistoryPrices.class)).thenReturn(historyRoot);

        doReturn(currentPricePath).when(historyRoot).get("currentPrice");
        doReturn(currentPriceSubquery).when(currentPriceSubquery).select(any(Expression.class));
        doReturn(currentPriceSubquery).when(currentPriceSubquery).where(any(Predicate.class));

        Specification<Product> spec = ProductSpecification.equalPriceByUnit(priceByUnit);
        Predicate predicate = spec.toPredicate(root, query, criteriaBuilder);
    }
}