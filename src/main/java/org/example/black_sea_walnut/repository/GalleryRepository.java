package org.example.black_sea_walnut.repository;

import org.example.black_sea_walnut.entity.Gallery;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GalleryRepository extends JpaRepository<Gallery, Long> {
    @Query("SELECT g FROM Gallery g JOIN g.translations t WHERE t.languageCode=:languageCode")
    List<Gallery> findAllByLanguageCode(@Param("languageCode")LanguageCode languageCode);
}
