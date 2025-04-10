package org.example.black_sea_walnut.repository;

import org.example.black_sea_walnut.entity.History;
import org.example.black_sea_walnut.entity.HistoryPrices;
import org.example.black_sea_walnut.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface HistoryPricesRepository extends JpaRepository<HistoryPrices, Long> {
    @Query("SELECT h FROM HistoryPrices h WHERE h.product.id = :productId ORDER BY h.validFrom DESC LIMIT 1")
    HistoryPrices findTopByProductInOrderByValidFromDesc(@Param("productId") Long id);

    @Transactional
    @Modifying
    @Query("DELETE FROM Product p WHERE p.id = :productId")
    void deleteAllByProduct(@Param("productId") Long productId);

    @Query("SELECT hp FROM HistoryPrices hp WHERE hp.product=:product ORDER BY hp.validTo DESC LIMIT 2")
    List<HistoryPrices> getLastTwoDataProduct(@Param("product") Product product);
}
