package org.example.black_sea_walnut.service.specifications;

import jakarta.persistence.criteria.Join;
import lombok.experimental.UtilityClass;
import org.example.black_sea_walnut.dto.admin.nut.NutResponseForView;
import org.example.black_sea_walnut.entity.Nut;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class NutSpecification {
    public static Specification<Nut> getSpecification(NutResponseForView entity, LanguageCode languageCode) {
        Specification<Nut> specification = Specification.where(null);
        if (entity.getId() != null) {
            specification = specification.and(hasId(entity.getId()));
        }
        if (entity.getTitle() != null && !entity.getTitle().isBlank()) {
            specification = specification.and(likeTitle(entity.getTitle(), languageCode));
        }
        if (entity.getDateOfUpdated() != null && !entity.getDateOfUpdated().isBlank()) {
            LocalDate date = LocalDate.parse(entity.getDateOfUpdated(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            specification = specification.and(hasDate(date));
        }
        return specification;
    }

    private static Specification<Nut> hasId(Long id) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("id"), id));
    }

    private static Specification<Nut> likeTitle(String name, LanguageCode code) {
        return (root, query, criteriaBuilder) -> {
            Join<Object, Object> translations = root.join("translations");
            return criteriaBuilder.and(
                    criteriaBuilder.equal(translations.get("languageCode"), code),
                    criteriaBuilder.like(translations.get("title"), "%" + name + "%")
            );
        };
    }
    private static Specification<Nut> hasDate(LocalDate date) {
        LocalDateTime startDay = date.atStartOfDay();
        LocalDateTime endDay = date.atTime(LocalTime.MAX);
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("date"), startDay, endDay));
    }
}
