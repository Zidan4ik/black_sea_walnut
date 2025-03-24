package org.example.black_sea_walnut.service.specifications;

import jakarta.persistence.criteria.Join;
import lombok.experimental.UtilityClass;
import org.example.black_sea_walnut.entity.Gallery;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class GallerySpecifications {
    public static Specification<Gallery> getSpecification(LanguageCode code) {
        return Specification.where((root, query, criteriaBuilder) -> {
            Join<Object, Object> translations = root.join("translations");
            return criteriaBuilder.equal(translations.get("languageCode"), code);
        });
    }
}
