package org.example.black_sea_walnut.repository;

import lombok.NonNull;
import org.example.black_sea_walnut.entity.New;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewRepository extends JpaRepository<New, Long>, JpaSpecificationExecutor<New> {
    @NonNull
    Page<New> findAll(Specification<New> specification, @NonNull Pageable pageable);

    @Query("SELECT n FROM New n ORDER BY n.id DESC LIMIT 3")
    List<New> getNewsThreeLast();
}
