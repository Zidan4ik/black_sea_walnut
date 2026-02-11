package org.example.black_sea_walnut.service.specifications;


import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.black_sea_walnut.entity.Gallery;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

import static org.mockito.Mockito.*;

class GallerySpecificationsTest {
    @Test
    void testGetSpecification() {
        LanguageCode code = LanguageCode.uk;

        Root<Gallery> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Join<Object, Object> join = mock(Join.class);
        Path<LanguageCode> path = mock(Path.class);
        Predicate predicate = mock(Predicate.class);

        when(root.join("translations")).thenReturn(join);
        when(cb.equal(path, code)).thenReturn(predicate);

        Specification<Gallery> spec = GallerySpecifications.getSpecification(code,null);
        spec.toPredicate(root, query, cb);

        verify(root).join("translations");
        verify(join).get("languageCode");
    }
}