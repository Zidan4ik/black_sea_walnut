package org.example.black_sea_walnut.service.specifications;

import jakarta.persistence.criteria.*;
import lombok.experimental.UtilityClass;
import org.example.black_sea_walnut.dto.product.ProductResponseForShopPage;
import org.example.black_sea_walnut.entity.HistoryPrices;
import org.example.black_sea_walnut.entity.Product;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
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
//        boolean asc = entity.getDirectionCost().equals("ASC");
//        specification = specification.and(orderByCurrentPrice(asc));
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

    private static Specification<Product> hasMass(String mass) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("mass"), mass));
    }

    public static Specification<Product> orderByCurrentPrice(boolean ascending) {
        return (root, query, criteriaBuilder) -> {
            // Підзапит для знаходження останньої (максимальної) дати validFrom
            Subquery<LocalDateTime> subquery = query.subquery(LocalDateTime.class);
            Root<HistoryPrices> subRoot = subquery.from(HistoryPrices.class);

            subquery.select(criteriaBuilder.greatest(subRoot.<LocalDateTime>get("validFrom"))) // ✅ Фікс
                    .where(criteriaBuilder.equal(subRoot.get("product"), root));

            // Приєднуємо історію цін
            Join<Product, HistoryPrices> historyPrices = root.join("historyPrices");

            // Вибираємо тільки запис з останньою датою validFrom
            Predicate lastPricePredicate = criteriaBuilder.equal(historyPrices.get("validFrom"), subquery);
            query.where(lastPricePredicate);

            // Сортуємо за останнім currentPrice
            query.orderBy(ascending ?
                    criteriaBuilder.asc(historyPrices.get("currentPrice")) :
                    criteriaBuilder.desc(historyPrices.get("currentPrice"))
            );

            return criteriaBuilder.conjunction();
        };
    }
}
