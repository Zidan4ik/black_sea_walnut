package org.example.black_sea_walnut.service.specifications;

import org.example.black_sea_walnut.dto.admin.calls.CallResponseForView;
import org.example.black_sea_walnut.entity.Call;
import org.example.black_sea_walnut.enums.CallStatus;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class CallSpecificationTest {
    @Test
    void testGetSpecification_withAllFields() {
        // given
        CallResponseForView dto = CallResponseForView.builder().build();
        dto.setId(1L);
        dto.setPhone("123456789");
        dto.setDate("01.01.2023");
        dto.setStatus("new_");

        Specification<Call> spec = CallSpecification.getSpecification(dto);
        assertNotNull(spec);

        // simulate criteria usage
        Root<Call> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);

        Path<Object> phonePath = mock(Path.class);
        Path<Object> idPath = mock(Path.class);
        Path<Object> statusPath = mock(Path.class);
        Path<Object> datePath = mock(Path.class);
        Expression<LocalDate> dateExpression = mock(Expression.class);

        when(root.get("id")).thenReturn(idPath);
        when(root.get("phone")).thenReturn(phonePath);
        when(root.get("status")).thenReturn(statusPath);
        when(root.get("registerCall")).thenReturn(datePath);

        when(cb.equal(idPath, 1L)).thenReturn(mock(Predicate.class));
//        when(cb.like(phonePath, "%123456789%")).thenReturn(mock(Predicate.class));
        when(cb.equal(statusPath, CallStatus.new_)).thenReturn(mock(Predicate.class));
        when(cb.function(eq("DATE"), eq(LocalDate.class), eq(datePath))).thenReturn(dateExpression);
        when(cb.equal(dateExpression, LocalDate.of(2023, 1, 1))).thenReturn(mock(Predicate.class));

        // when
        Predicate predicate = spec.toPredicate(root, query, cb);

        // then
        assertNotNull(predicate);
    }

    @Test
    void testGetSpecification_withNullFields() {
        CallResponseForView dto = CallResponseForView.builder().build();
        dto.setId(null);
        dto.setPhone(null);
        dto.setDate(null);
        dto.setStatus(null);

        Specification<Call> spec = CallSpecification.getSpecification(dto);
        assertNotNull(spec);

        // it should be essentially a no-op spec (no predicates)
        assertNull(spec.toPredicate(null, null, null));
    }

    @Test
    void testHasId() {
        Specification<Call> spec = CallSpecification.getSpecification(
                CallResponseForView.builder().id(10L).build()
        );
        Root root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Path<Object> path = mock(Path.class);
        when(root.get("id")).thenReturn(path);
        when(cb.equal(path, 10L)).thenReturn(mock(Predicate.class));

        Predicate result = spec.toPredicate(root, query, cb);
        assertNotNull(result);
    }

    @Test
    void testLikePhone_blankPhone() {
        CallResponseForView dto = CallResponseForView.builder().build();
        dto.setPhone("  ");
        Specification<Call> spec = CallSpecification.getSpecification(dto);
        assertNotNull(spec);
        assertNull(spec.toPredicate(null, null, null));
    }

    @Test
    void testLikePhone_PhoneAndDateIsBlank() {
        CallResponseForView dto = CallResponseForView.builder().build();
        dto.setStatus("");
        dto.setDate("");
        Specification<Call> spec = CallSpecification.getSpecification(dto);
        assertNotNull(spec);
        assertNull(spec.toPredicate(null, null, null));
    }

    @Test
    void testHasDate() {
        CallResponseForView dto = CallResponseForView.builder().build();
        dto.setDate("15.08.2023");

        Specification<Call> spec = CallSpecification.getSpecification(dto);
        assertNotNull(spec);

        Root<Call> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);

        Path<Object> registerCallPath = mock(Path.class);
        when(root.get("registerCall")).thenReturn(registerCallPath);

        Expression<LocalDate> dateFunction = mock(Expression.class);
        when(cb.function(eq("DATE"), eq(LocalDate.class), eq(registerCallPath))).thenReturn(dateFunction);
        when(cb.equal(dateFunction, LocalDate.of(2023, 8, 15))).thenReturn(mock(Predicate.class));

        Predicate predicate = spec.toPredicate(root, query, cb);
        assertNotNull(predicate);
    }

    @Test
    void testHasStatus() {
        CallResponseForView dto = CallResponseForView.builder().build();
        dto.setStatus("close");

        Specification<Call> spec = CallSpecification.getSpecification(dto);
        assertNotNull(spec);

        Root<Call> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Path<Object> statusPath = mock(Path.class);
        when(root.get("status")).thenReturn(statusPath);
        when(cb.equal(statusPath, CallStatus.close)).thenReturn(mock(Predicate.class));

        Predicate predicate = spec.toPredicate(root, query, cb);
        assertNotNull(predicate);
    }
}