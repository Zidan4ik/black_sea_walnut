package org.example.black_sea_walnut.repository;

import lombok.NonNull;
import org.example.black_sea_walnut.entity.Order;
import org.example.black_sea_walnut.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    @NonNull
    Page<Order> findAll(Specification<Order> specification, @NonNull Pageable pageable);

    List<Order> findAllByUser(User user);

    @Query("SELECT new map(o.dateOfOrdering as date, COUNT(o) as count) " +
            "FROM Order o WHERE o.dateOfOrdering BETWEEN :startDate AND :endDate " +
            "GROUP BY o.dateOfOrdering ORDER BY o.dateOfOrdering")
    List<Map<String, Object>> countOrdersByMonth(@Param("startDate") LocalDate startDate,
                                                 @Param("endDate") LocalDate endDate);

    @Query("SELECT o FROM Order AS o WHERE o.user.id=:userId AND o.personalId=:personalId")
    Optional<Order> findOrderByUserAndPersonalId(@Param("userId") Long userId, @Param("personalId") Long personalId);
}