package org.example.black_sea_walnut.repository;

import lombok.NonNull;
import org.example.black_sea_walnut.entity.Product;
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
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    @NonNull
    Page<Product> findAll(@NonNull Specification<Product> specification, @NonNull Pageable pageable);

    boolean existsByArticleId(Long id);

    boolean existsById(@NonNull Long id);

    @Query(value = "SELECT * FROM products ORDER BY RAND() LIMIT :size",nativeQuery = true)
    List<Product> findRandomProducts(@Param("size") int size);

    @Query("SELECT p.mass FROM Product p GROUP BY p.mass")
    List<Integer> getAllMasses();
}
