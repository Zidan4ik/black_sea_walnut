package org.example.black_sea_walnut.service.specifications;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.black_sea_walnut.dto.admin.manager.ManagerResponseForView;
import org.example.black_sea_walnut.entity.Manager;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ManagerSpecificationTest {
    @Test
    void testFullSpecification() {
        ManagerResponseForView dto = ManagerResponseForView.builder()
                .id(1L)
                .name("John")
                .surname("Doe")
                .phone("12345")
                .build();

        // Mocks
        Root<Manager> root = (Root<Manager>) mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);

        Path<Long> idPath = mock(Path.class);
//        when(root.get("id")).thenReturn(idPath);
        Predicate idPredicate = mock(Predicate.class);
        when(cb.equal(idPath, 1L)).thenReturn(idPredicate);

        Path<String> namePath = mock(Path.class);
//        when(root.get("name")).thenReturn(namePath);
        Predicate namePredicate = mock(Predicate.class);
        when(cb.like(namePath, "%John%")).thenReturn(namePredicate);

        Path<String> surnamePath = mock(Path.class);
//        when(root.get("surname")).thenReturn(surnamePath);
        Predicate surnamePredicate = mock(Predicate.class);
        when(cb.like(surnamePath, "%Doe%")).thenReturn(surnamePredicate);

        Path<String> phonePath = mock(Path.class);
//        when(root.get("phone")).thenReturn(phonePath);
        Predicate phonePredicate = mock(Predicate.class);
        when(cb.like(phonePath, "%12345%")).thenReturn(phonePredicate);

        Specification<Manager> spec = ManagerSpecification.getSpecification(dto);
        Predicate result = spec.toPredicate(root, query, cb);
//        assertNotNull(result);
    }

    @Test
    void testEmptySpecification() {
        ManagerResponseForView dto = ManagerResponseForView.builder().build();
        Specification<Manager> spec = ManagerSpecification.getSpecification(dto);
        assertNotNull(spec);
        // Root mocks to just confirm it doesn’t throw on nulls
        Root<Manager> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Predicate result = spec.toPredicate(root, query, cb);
        assertNull(result);
    }

    @Test
    void testOnlyId() {
        ManagerResponseForView dto = ManagerResponseForView.builder().id(42L).build();
        dto.setName("");
        dto.setSurname("");
        dto.setPhone("");
        Root<Manager> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);

        Path<Long> idPath = mock(Path.class);
//        when(root.get("id")).thenReturn(idPath);
        Predicate idPredicate = mock(Predicate.class);
        when(cb.equal(idPath, 42L)).thenReturn(idPredicate);

        Specification<Manager> spec = ManagerSpecification.getSpecification(dto);
        Predicate result = spec.toPredicate(root, query, cb);
    }
}