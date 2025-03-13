package org.example.black_sea_walnut.password;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenService {
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public void createResetPasswordTokenForUser(User user, String passwordToken) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(passwordToken, user);
        passwordResetTokenRepository.save(passwordResetToken);
    }

    public String validatePasswordResetToken(String theToken) {
        PasswordResetToken token = passwordResetTokenRepository.findByToken(theToken);
        if (token == null) {
            return "Invalid password reset token";
        }
        Calendar calendar = Calendar.getInstance();
        if ((token.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0) {
            return "Link already expired, resend link";
        }
        return "valid";
    }

    public Optional<User> findUserByPasswordToken(String passwordToken) {
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(passwordToken).getUser());
    }

    @Transactional
    public void deleteTokenByToken(String thetoken) {
        PasswordResetToken token = passwordResetTokenRepository.findByToken(thetoken);
        passwordResetTokenRepository.deleteById(token.getToken_id());
    }

    @Transactional
    public void deleteTokenByUser(User user){
        passwordResetTokenRepository.deleteByUser(user);
    }

    public boolean isExistTokenByUser(User user){
        return passwordResetTokenRepository.existsByUser(user);
    }
}
