package org.example.black_sea_walnut.service.specifications;

import jakarta.persistence.criteria.*;
import org.example.black_sea_walnut.dto.admin.new_.ResponseNewForView;
import org.example.black_sea_walnut.entity.New;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NewSpecificationTest {
    @Test
    void testGetSpecification_AllFieldsProvided() {
        ResponseNewForView dto = ResponseNewForView.builder()
                .id(1L)
                .title("Test title")
                .date("20.03.2024")
                .build();

        Root<New> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);

        Path<Long> idPath = mock(Path.class);
        Path<LocalDate> datePath = mock(Path.class);

        Join<Object, Object> join = mock(Join.class);
        Path<String> titlePath = mock(Path.class);
        Path<LanguageCode> langPath = mock(Path.class);

        Predicate idPredicate = mock(Predicate.class);
        Predicate titleLikePredicate = mock(Predicate.class);
        Predicate langEqPredicate = mock(Predicate.class);
        Predicate combinedTitlePredicate = mock(Predicate.class);
        Predicate datePredicate = mock(Predicate.class);
        Predicate finalPredicate = mock(Predicate.class);
        when(cb.equal(idPath, 1L)).thenReturn(idPredicate);
        when(root.join("translations")).thenReturn(join);
        when(cb.equal(langPath, LanguageCode.uk)).thenReturn(langEqPredicate);
        when(cb.like(titlePath, "%Test title%")).thenReturn(titleLikePredicate);
        when(cb.and(langEqPredicate, titleLikePredicate)).thenReturn(combinedTitlePredicate);
        when(cb.equal(datePath, LocalDate.of(2024, 3, 20))).thenReturn(datePredicate);
        Predicate result = NewSpecification.getSpecification(dto, LanguageCode.uk)
                .toPredicate(root, query, cb);
    }

    @Test
    void testGetSpecification_OnlyIdProvided() {
        ResponseNewForView dto = ResponseNewForView.builder()
                .id(10L)
                .build();

        Root<New> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Path<Long> idPath = mock(Path.class);
        Predicate idPredicate = mock(Predicate.class);
        when(cb.equal(idPath, 10L)).thenReturn(idPredicate);
        Predicate result = NewSpecification.getSpecification(dto, LanguageCode.en)
                .toPredicate(root, query, cb);
    }

    @Test
    void testGetSpecification_OnlyTitleProvided() {
        ResponseNewForView dto = ResponseNewForView.builder()
                .title("Новина")
                .build();
        Root<New> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);

        Join<Object, Object> join = mock(Join.class);
        Path<String> titlePath = mock(Path.class);
        Path<LanguageCode> langPath = mock(Path.class);
        Predicate langEq = mock(Predicate.class);
        Predicate titleLike = mock(Predicate.class);
        Predicate combined = mock(Predicate.class);

        when(root.join("translations")).thenReturn(join);
        when(cb.equal(langPath, LanguageCode.uk)).thenReturn(langEq);
        when(cb.like(titlePath, "%Новина%")).thenReturn(titleLike);
        when(cb.and(langEq, titleLike)).thenReturn(combined);

        Predicate result = NewSpecification.getSpecification(dto, LanguageCode.uk)
                .toPredicate(root, query, cb);
    }

    @Test
    void testGetSpecification_OnlyDateProvided() {
        ResponseNewForView dto = ResponseNewForView.builder()
                .date("01.01.2023")
                .build();

        Root<New> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);

        Path<LocalDate> datePath = mock(Path.class);
        Predicate datePredicate = mock(Predicate.class);
        when(cb.equal(datePath, LocalDate.of(2023, 1, 1))).thenReturn(datePredicate);

        Predicate result = NewSpecification.getSpecification(dto, LanguageCode.uk)
                .toPredicate(root, query, cb);
    }

    @Test
    void testGetSpecification_AllFieldsNullOrBlank() {
        ResponseNewForView dto = ResponseNewForView.builder()
                .id(null)
                .title("  ")
                .date("")
                .build();

        Specification<New> spec = NewSpecification.getSpecification(dto, LanguageCode.en);
        assertNull(spec.toPredicate(null, null, null));
    }
}