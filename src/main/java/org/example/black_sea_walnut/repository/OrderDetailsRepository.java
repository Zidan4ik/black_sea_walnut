package org.example.black_sea_walnut.repository;

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
        SELECT p.articleId, MIN(pt.name), SUM(od.count), SUM(od.summaWithDiscount * od.count)
        FROM OrderDetail od
        JOIN od.order o
        JOIN od.products p
        JOIN ProductTranslation pt ON pt.product.articleId = p.articleId
        WHERE o.dateOfOrdering BETWEEN :startDate AND :endDate
        AND pt.languageCode = :languageCode
        GROUP BY p.articleId
        ORDER BY SUM(od.summaWithDiscount * od.count) DESC
        """)
    List<Object[]> findSummaWithDiscountByProductAndDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("languageCode") LanguageCode languageCode,
            Pageable pageable
    );
}
