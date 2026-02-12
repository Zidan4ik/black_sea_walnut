package org.example.black_sea_walnut.service.specifications;

import jakarta.persistence.criteria.Join;
import lombok.experimental.UtilityClass;
import org.example.black_sea_walnut.dto.admin.new_.ResponseNewForView;
import org.example.black_sea_walnut.entity.New;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class NewSpecification {
    public static Specification<New> getSpecification(ResponseNewForView entity, LanguageCode code) {
        Specification<New> specification = Specification.where(null);
        if (entity.getId() != null) {
            specification = specification.and(hasId(entity.getId()));
        }
        if (entity.getTitle() != null && !entity.getTitle().isBlank()) {
            specification = specification.and(likeTitle(entity.getTitle(), code));
        }
        if (entity.getDate() != null && !entity.getDate().isBlank()) {
            LocalDate date = LocalDate.parse(entity.getDate(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            specification = specification.and(hasDate(date));
        }
        if (entity.getIsActive() != null) {
            specification = specification.and(isActive(entity.getIsActive()));
        }
        return specification;
    }

    private static Specification<New> hasId(Long id) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("id"), id));
    }

    private static Specification<New> isActive(boolean isActive) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isActive"), isActive));
    }

    private static Specification<New> likeTitle(String title, LanguageCode code) {
        return (root, query, criteriaBuilder) -> {
            Join<Object, Object> translation = root.join("translations");
            return criteriaBuilder.and(
                    criteriaBuilder.equal(translation.get("languageCode"), code),
                    criteriaBuilder.like(translation.get("title"), "%" + title + "%")
            );
        };
    }

    private static Specification<New> hasDate(LocalDate date) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("dateOfPublication"), date));
    }
}
