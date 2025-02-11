package org.example.black_sea_walnut.repository;

import lombok.NonNull;
import org.example.black_sea_walnut.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    @NonNull
    Page<User> findAll(Specification<User> specification, @NonNull Pageable pageable);
    @Query("""
            SELECT u.registerType, COUNT(u.registerType) 
            FROM User u
            WHERE u.dateRegistered BETWEEN :start AND :end
            GROUP BY u.registerType
            """)
    List<Object[]> getUsersBetweenStartDayAndEndDay(@Param("start") LocalDate start,
                                                    @Param("end") LocalDate end);
}
