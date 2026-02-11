package org.example.black_sea_walnut.repository;

import lombok.NonNull;
import org.example.black_sea_walnut.entity.Gallery;
import org.example.black_sea_walnut.entity.Manager;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GalleryRepository extends JpaRepository<Gallery, Long>, JpaSpecificationExecutor<Gallery> {
    @Query("SELECT g FROM Gallery g JOIN g.translations t WHERE t.languageCode=:languageCode")
    List<Gallery> findAllByLanguageCode(@Param("languageCode") LanguageCode languageCode);

    List<Gallery> findAllByTranslations_LanguageCodeAndIsActive(LanguageCode languageCode, boolean isActive);

    @NonNull
    Page<Gallery> findAll(Specification<Gallery> specification, @NonNull Pageable pageable);

}
