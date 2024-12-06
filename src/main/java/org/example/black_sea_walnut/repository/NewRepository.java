package org.example.black_sea_walnut.repository;

import lombok.NonNull;
import org.example.black_sea_walnut.entity.New;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface NewRepository extends JpaRepository<New, Long>, JpaSpecificationExecutor<New> {
    @NonNull
    Page<New> findAll(Specification<New> specification, @NonNull Pageable pageable);
}
