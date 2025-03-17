package org.example.black_sea_walnut.service;

import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.admin.stats.UserResponseForStats;
import org.example.black_sea_walnut.dto.admin.user.request.UserFopRequestForView;
import org.example.black_sea_walnut.dto.admin.user.request.UserIndividualRequestForAdd;
import org.example.black_sea_walnut.dto.admin.user.request.UserLegalRequestForView;
import org.example.black_sea_walnut.dto.admin.user.UserResponseForView;
import org.example.black_sea_walnut.dto.admin.user.response.UserFopResponseForAdd;
import org.example.black_sea_walnut.dto.admin.user.response.UserIndividualResponseForAdd;
import org.example.black_sea_walnut.dto.admin.user.response.UserLegalResponseForView;
import org.example.black_sea_walnut.dto.web.security.UserRequestForRegistration;
import org.example.black_sea_walnut.dto.web.user.AddressDtoIndividual;
import org.example.black_sea_walnut.dto.web.user.AddressDtoLegal;
import org.example.black_sea_walnut.dto.web.user.UserDtoIndividual;
import org.example.black_sea_walnut.dto.web.user.UserDtoLegal;
import org.example.black_sea_walnut.entity.User;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAll();

    List<UserResponseForView> getAllInResponseForView();

    PageResponse<UserResponseForView> getAll(UserResponseForView response, Pageable pageable);

    User getById(Long id);

    UserFopResponseForAdd getByIdForFopResponse(Long id);

    UserIndividualResponseForAdd getByIdForIndividualResponse(Long id);

    UserLegalResponseForView getByIdForLegalResponse(Long id);

    User save(User entity);

    User save(UserFopRequestForView dto);

    User save(UserIndividualRequestForAdd dto);

    User save(UserLegalRequestForView dto);

    User save(UserRequestForRegistration dto);

    void save(UserDtoLegal dto);

    void save(UserDtoIndividual dto);

    void save(AddressDtoIndividual dto);

    void save(AddressDtoLegal dto);

    List<UserResponseForStats> getUsersByDate(LocalDate start, LocalDate end);

    Optional<User> getByEmail(String email);

    boolean isExistUserByEmail(String email);

    void createPasswordResetTokenForUser(User user, String passwordToken);

    void saveUserVerificationToken(User theUser, String token);

    String validatePasswordResetToken(String passwordResetToken);

    User findUserByPasswordToken(String passwordResetToken);

    void resetUserPassword(User user, String newPassword);

    void deleteTokenByToken(String token);
}