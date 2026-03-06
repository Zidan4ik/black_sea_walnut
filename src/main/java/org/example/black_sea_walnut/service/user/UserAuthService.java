package org.example.black_sea_walnut.service.user;

import org.example.black_sea_walnut.entity.User;

public interface UserAuthService {
    void resetUserPassword(User user, String newPassword);

    String validatePasswordResetToken(String passwordResetToken);

    void saveUserVerificationToken(User theUser, String token);

    void createPasswordResetTokenForUser(User user, String passwordToken);
}
