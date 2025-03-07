package org.example.black_sea_walnut.repository;

import org.example.black_sea_walnut.entity.Order;
import org.example.black_sea_walnut.entity.OrderDetail;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetail, Long> {
    @Query("""
    SELECT p.articleId, 
           MIN(ukTranslation.name) AS nameUk, 
           MIN(enTranslation.name) AS nameEn, 
           SUM(od.count), 
           SUM(od.summaWithDiscount * od.count)
    FROM OrderDetail od
    JOIN od.order o
    JOIN od.products p
    LEFT JOIN ProductTranslation ukTranslation ON ukTranslation.product.articleId = p.articleId 
                                               AND ukTranslation.languageCode = 'uk'
    LEFT JOIN ProductTranslation enTranslation ON enTranslation.product.articleId = p.articleId 
                                               AND enTranslation.languageCode = 'en'
    WHERE o.dateOfOrdering BETWEEN :startDate AND :endDate
    GROUP BY p.articleId
    ORDER BY SUM(od.summaWithDiscount * od.count) DESC
""")
    List<Object[]> findSummaWithDiscountByProductAndDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );

    List<OrderDetail> getAllByOrder(Order order);
}
