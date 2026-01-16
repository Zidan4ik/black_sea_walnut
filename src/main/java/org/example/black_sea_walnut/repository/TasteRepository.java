package org.example.black_sea_walnut.repository;

import org.example.black_sea_walnut.entity.Taste;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.springframework.data.jpa.repository.JpaRepository;
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

    default Map<Long, List<Taste>> findAllGroupedByCommonId(){
        return findAll().stream()
                .collect(Collectors.groupingBy(Taste::getCommonId));
    }
}