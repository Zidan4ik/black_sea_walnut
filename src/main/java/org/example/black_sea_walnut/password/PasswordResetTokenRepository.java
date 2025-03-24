package org.example.black_sea_walnut.password;

import org.example.black_sea_walnut.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String theToken);

    boolean existsByUser(User user);

    void deleteByUser(User user);
}
