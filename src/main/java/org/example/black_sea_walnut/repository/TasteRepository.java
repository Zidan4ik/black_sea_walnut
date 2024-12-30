package org.example.black_sea_walnut.repository;

import org.example.black_sea_walnut.entity.Taste;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface TasteRepository extends JpaRepository<Taste,Long> {
    Set<Taste> findAllByLanguageCode(LanguageCode languageCode);
    List<Taste> findAllByTasteId(Long id);
}
