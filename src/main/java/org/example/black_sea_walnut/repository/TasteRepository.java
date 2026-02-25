package org.example.black_sea_walnut.repository;

import org.example.black_sea_walnut.entity.Taste;
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
public interface TasteRepository extends JpaRepository<Taste, Long> {
    Set<Taste> findAllByLanguageCode(LanguageCode languageCode);

    Set<Taste> findAllByCommonId(Long id);

    boolean existsByCommonId(Long commonId);

    void deleteAllByCommonId(Long id);

    @Modifying
    @Query(value = "DELETE FROM products_tastes WHERE taste_id IN (SELECT id FROM tastes WHERE common_id = :tasteId)", nativeQuery = true)
    void deleteProductLinksByTasteCommonId(Long tasteId);

    default Map<Long, List<Taste>> findAllGroupedByCommonId(){
        return findAll().stream()
                .collect(Collectors.groupingBy(Taste::getCommonId));
    }
}