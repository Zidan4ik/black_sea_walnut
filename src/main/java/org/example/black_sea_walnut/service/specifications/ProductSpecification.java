package org.example.black_sea_walnut.service.specifications;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import lombok.experimental.UtilityClass;
import org.example.black_sea_walnut.dto.product.ProductResponseForView;
import org.example.black_sea_walnut.entity.Product;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;


@UtilityClass
public class ProductSpecification {
    public static Specification<Product> getSpecification(ProductResponseForView entity, LanguageCode languageCode) {
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
        if (entity.getDiscount() != null) {
            specification = specification.and(hasDiscount(entity.getDiscount(), languageCode));
        }
        return specification;
    }

    private static Specification<Product> hasTaste(String taste, LanguageCode code) {
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

    private static Specification<Product> hasDiscount(String discount, LanguageCode code) {
        return (root, query, criteriaBuilder) -> {
            Join<Object, Object> discountTable = root.join("discounts",JoinType.LEFT);
            if(!Objects.equals(discount,"")){
                return criteriaBuilder.and(
                        criteriaBuilder.equal(discountTable.get("languageCode"),code),
                        criteriaBuilder.equal(discountTable.get("name"),discount)
                );
            }else {
                query.orderBy(criteriaBuilder.asc(root.get("id")));
                return criteriaBuilder.or(
                        criteriaBuilder.isNull(discountTable.get("languageCode")),
                        criteriaBuilder.equal(discountTable.get("languageCode"), code)
                );
            }
        };
    }

    private static Specification<Product> hasId(Long id) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("id"), id));
    }

    private static Specification<Product> likeName(String name, LanguageCode code) {
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
