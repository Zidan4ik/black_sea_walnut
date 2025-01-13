package org.example.black_sea_walnut.repository;

import org.example.black_sea_walnut.entity.Discount;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {
    Set<Discount> getAllByLanguageCode(LanguageCode code);
    Set<Discount> getAllByDiscountId(Long id);
    boolean existsByDiscountId(Long discountId);
}
