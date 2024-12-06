package org.example.black_sea_walnut.service.specifications;

import lombok.experimental.UtilityClass;
import org.example.black_sea_walnut.dto.ResponseNewForView;
import org.example.black_sea_walnut.entity.New;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class NewSpecification {
    public static Specification<New> getSpecification(ResponseNewForView entity) {
        Specification<New> specification = Specification.where(null);
        if (entity.getId() != null) {
            specification = specification.and(hasId(entity.getId()));
        }
        if (entity.getTitle() != null && !entity.getTitle().isBlank()) {
            specification = specification.and(likeTitle(entity.getTitle()));
        }
        if (entity.getDate() != null && !entity.getDate().isBlank()) {
            LocalDate date = LocalDate.parse(entity.getDate(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            specification = specification.and(hasDate(date));
        }
        return specification;
    }

    private static Specification<New> hasId(Long id) {
        if (id == null) return null;
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("id"), id));
    }

    private static Specification<New> likeTitle(String title) {
        if (title == null || title.isBlank()) return null;
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("title"), "%" + title + "%"));
    }

    private static Specification<New> hasDate(LocalDate date) {
        if (date == null) return null;
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("time_updated"), "%" + date + "%"));
    }
}
