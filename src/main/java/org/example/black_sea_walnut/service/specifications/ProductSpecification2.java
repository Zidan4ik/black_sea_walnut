package org.example.black_sea_walnut.service.specifications;

import jakarta.persistence.criteria.*;
import lombok.experimental.UtilityClass;
import org.example.black_sea_walnut.dto.admin.product.ProductResponseForShopPage;
import org.example.black_sea_walnut.entity.Product;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

@UtilityClass
public class ProductSpecification2 {
    public static Specification<Product> getSpecification(ProductResponseForShopPage entity, LanguageCode languageCode) {
        Specification<Product> specification = Specification.where(null);

        if (entity.getTasteFilter() != null) {
            specification = specification.and(hasTaste(entity.getTasteFilter(), languageCode));
        }
        if (entity.getMassFilter() != null && !entity.getMassFilter().isBlank()) {
            specification = specification.and(hasMass(entity.getMassFilter()));
        }
        return specification;
    }

    static Specification<Product> hasTaste(String taste, LanguageCode code) {
        return (root, query, criteriaBuilder) -> {
            Join<Object, Object> tasteTable = root.join("tastes", JoinType.LEFT);

            if (!Objects.equals(taste, "")) {
                return criteriaBuilder.and(
                        criteriaBuilder.equal(tasteTable.get("languageCode"), code),
                        criteriaBuilder.equal(tasteTable.get("name"), taste)
                );
            } else {
                query.orderBy(criteriaBuilder.asc(root.get("id")));
                return criteriaBuilder.or(
                        criteriaBuilder.isNull(tasteTable.get("languageCode")),
                        criteriaBuilder.equal(tasteTable.get("languageCode"), code)
                );
            }
        };
    }

    static Specification<Product> hasMass(String mass) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("mass"), mass));
    }
}
