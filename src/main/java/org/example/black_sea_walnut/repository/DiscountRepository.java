package org.example.black_sea_walnut.repository;

import org.example.black_sea_walnut.entity.Discount;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {
    Set<Discount> getAllByLanguageCode(LanguageCode code);

    Set<Discount> getAllByDiscountCommonId(Long id);

    Set<Discount> getAllByDiscountCommonIdAndLanguageCode(Long id, LanguageCode code);

    boolean existsByDiscountCommonId(Long commonId);

    void deleteAllByDiscountCommonId(Long id);

    @Modifying
    @Query(value = "DELETE FROM products_discounts WHERE discount_id IN (SELECT id FROM discounts WHERE discount_common_id = :commonId)", nativeQuery = true)
    void deleteProductLinksByDiscountCommonId(Long commonId);

    default Map<Long, List<Discount>> findAllGroupedByCommonId() {
        return findAll().stream()
                .collect(Collectors.groupingBy(Discount::getDiscountCommonId));
    }
}
