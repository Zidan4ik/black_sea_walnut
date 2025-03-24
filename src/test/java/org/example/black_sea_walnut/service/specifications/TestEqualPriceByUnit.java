package org.example.black_sea_walnut.service.specifications;

import org.example.black_sea_walnut.entity.HistoryPrices;
import org.example.black_sea_walnut.entity.Product;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.*;

import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class TestEqualPriceByUnit {

    @Mock
    private Root<Product> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private Subquery<LocalDateTime> maxValidFromSubquery;

    @Mock
    private Subquery<Integer> currentPriceSubquery;

    @Mock
    private Path<Integer> currentPricePath;

    @Mock
    private Expression<LocalDateTime> greatestExpression;

    @Mock
    private Root<HistoryPrices> historyRoot1;

    @Mock
    private Root<HistoryPrices> historyRoot2;

    @Mock
    private Path<LocalDateTime> validFromPath;

    @Mock
    private Predicate predicate1, predicate2;

    private int priceByUnit = 100;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(query.subquery(LocalDateTime.class)).thenReturn(maxValidFromSubquery);
        when(maxValidFromSubquery.from(HistoryPrices.class)).thenReturn(historyRoot1);
        when(historyRoot1.get("validFrom")).thenReturn(any());
        when(criteriaBuilder.greatest(validFromPath)).thenReturn(greatestExpression);
        when(maxValidFromSubquery.select(greatestExpression)).thenReturn(maxValidFromSubquery);
        when(maxValidFromSubquery.where(any(Predicate.class))).thenReturn(maxValidFromSubquery);
        when(query.subquery(Integer.class)).thenReturn(currentPriceSubquery);
        when(currentPriceSubquery.from(HistoryPrices.class)).thenReturn(historyRoot2);
        when(historyRoot2.get("currentPrice")).thenReturn(any());
        when(currentPriceSubquery.select(currentPricePath)).thenReturn(currentPriceSubquery);
        when(currentPriceSubquery.where(any(Predicate.class), any(Predicate.class))).thenReturn(currentPriceSubquery);
        when(criteriaBuilder.equal(any(), any())).thenReturn(predicate1);
    }

    @Test
    void testEqualPriceByUnit() {
        Specification<Product> specification = ProductSpecification.equalPriceByUnit(priceByUnit);
        Predicate resultPredicate = specification.toPredicate(root, query, criteriaBuilder);
        verify(query).subquery(LocalDateTime.class);
        verify(query).subquery(Integer.class);
        verify(maxValidFromSubquery).select(greatestExpression);
        verify(maxValidFromSubquery).where(any(Predicate.class));
        verify(currentPriceSubquery).where(any(Predicate.class), any(Predicate.class));
        verify(criteriaBuilder).equal(currentPriceSubquery, priceByUnit);
    }
}