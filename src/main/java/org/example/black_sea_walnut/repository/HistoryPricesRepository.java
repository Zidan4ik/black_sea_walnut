package org.example.black_sea_walnut.repository;

import org.example.black_sea_walnut.entity.HistoryPrices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryPricesRepository extends JpaRepository<HistoryPrices, Long> {
    @Query("SELECT h FROM HistoryPrices h WHERE h.product.id = :productId ORDER BY h.validFrom DESC")
    HistoryPrices findLatestPriceByProductId(@Param("productId") Long id);
}
