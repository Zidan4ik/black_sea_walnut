package org.example.black_sea_walnut.service.specifications;

import jakarta.persistence.criteria.Join;
import lombok.experimental.UtilityClass;
import org.example.black_sea_walnut.dto.product.ResponseProductForView;
import org.example.black_sea_walnut.entity.Product;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;


@UtilityClass
public class ProductSpecification {
    public static Specification<Product> getSpecification(ResponseProductForView entity, LanguageCode languageCode) {
        Specification<Product> specification = Specification.where(null);
        if (entity.getId() != null) {
            specification = specification.and(hasId(entity.getId()));
        }
        if (entity.getName() != null && !entity.getName().isBlank()) {
            specification = specification.and(likeName(entity.getName(), languageCode));
        }
        if (entity.getPriceByUnit() != null && !entity.getPriceByUnit().isBlank()) {
            specification = specification.and(equalPriceByUnit(entity.getPriceByUnit()));
        }
        if (entity.getTaste() != null) {
            specification = specification.and(hasTaste(entity.getTaste(), languageCode));
        }
        if (entity.getDiscountType() != null) {
            specification = specification.and(hasDiscount(entity.getDiscountType(), languageCode));
        }
        return specification;
    }

    private static Specification<Product> hasTaste(String taste, LanguageCode code) {
        if (!Objects.equals(taste, "")) {
            return (root, query, criteriaBuilder) -> {
                Join<Object, Object> tasteTable = root.join("taste");
                return criteriaBuilder.and(
                        criteriaBuilder.equal(tasteTable.get("languageCode"), code),
                        criteriaBuilder.equal(tasteTable.get("name"), taste)
                );
            };
        } else {
            return (root, query, criteriaBuilder) -> {
                query.orderBy(criteriaBuilder.asc(root.get("id")));
                Join<Object, Object> tasteTable = root.join("taste");
                return criteriaBuilder.and(
                        criteriaBuilder.equal(tasteTable.get("languageCode"), code)
                );
            };
        }
    }

    private static Specification<Product> hasDiscount(String discount, LanguageCode code) {
        if (!Objects.equals(discount, "")) {
            return (root, query, criteriaBuilder) -> {
                Join<Object, Object> tasteTable = root.join("discount");
                return criteriaBuilder.and(
                        criteriaBuilder.equal(tasteTable.get("languageCode"), code),
                        criteriaBuilder.equal(tasteTable.get("name"), discount)
                );
            };
        } else {
            return (root, query, criteriaBuilder) -> {
                query.orderBy(criteriaBuilder.asc(root.get("id")));
                Join<Object, Object> tasteTable = root.join("discount");
                return criteriaBuilder.and(
                        criteriaBuilder.equal(tasteTable.get("languageCode"), code)
                );
            };
        }
    }

    private static Specification<Product> hasId(Long id) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("id"), id));
    }

    private static Specification<Product> likeName(String name, LanguageCode code) { //translation
        return (root, query, criteriaBuilder) -> {
            Join<Object, Object> translations = root.join("productTranslations");
            return criteriaBuilder.and(
                    criteriaBuilder.equal(translations.get("languageCode"), code),
                    criteriaBuilder.like(translations.get("name"), "%" + name + "%")
            );
        };
    }

    private static Specification<Product> equalPriceByUnit(String priceByUnit) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("price"), priceByUnit);
    }

}
