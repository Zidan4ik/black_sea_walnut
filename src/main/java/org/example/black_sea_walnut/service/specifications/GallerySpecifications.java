package org.example.black_sea_walnut.service.specifications;

import jakarta.persistence.criteria.Join;
import lombok.experimental.UtilityClass;
import org.example.black_sea_walnut.entity.Call;
import org.example.black_sea_walnut.entity.Gallery;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class GallerySpecifications {
    public static Specification<Gallery> getSpecification(LanguageCode code, Boolean isActive) {
        Specification<Gallery> specification = Specification.where(null);

        if (isActive != null) {
            specification = specification.and(((root, query, cb) -> cb.equal(root.get("isActive"),isActive)));
        }

        specification = specification.and((root, query, cb) -> {
            Join<Object, Object> translations = root.join("translations");
            return cb.equal(translations.get("languageCode"), code);
        });

        return specification;
    }
}
