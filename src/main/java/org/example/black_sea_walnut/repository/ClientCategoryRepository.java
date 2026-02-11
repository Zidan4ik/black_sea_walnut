package org.example.black_sea_walnut.repository;

import org.example.black_sea_walnut.entity.ClientCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientCategoryRepository extends JpaRepository<ClientCategory, Long> {
    List<ClientCategory> getAllByIsActive(boolean isActive);
}
