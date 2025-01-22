package org.example.black_sea_walnut.repository;

import org.example.black_sea_walnut.entity.History;
import org.example.black_sea_walnut.enums.PageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {
    Optional<History> findByPageType(PageType type);
}
