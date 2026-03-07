package org.example.black_sea_walnut.service.user;

import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.admin.stats.UserResponseForStats;
import org.example.black_sea_walnut.dto.admin.user.UserResponseForView;
import org.example.black_sea_walnut.dto.admin.user.response.UserFopResponseForAdd;
import org.example.black_sea_walnut.dto.admin.user.response.UserIndividualResponseForAdd;
import org.example.black_sea_walnut.dto.admin.user.response.UserLegalResponseForView;
import org.example.black_sea_walnut.entity.User;
import org.example.black_sea_walnut.mapper.UserMapper;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserResponseForView> getAllInResponseForView();

    PageResponse<UserResponseForView> getAll(UserResponseForView response, Pageable pageable);

    User getById(Long id);

    UserFopResponseForAdd getByIdForFopResponse(Long id);

    UserIndividualResponseForAdd getByIdForIndividualResponse(Long id);

    UserLegalResponseForView getByIdForLegalResponse(Long id);

    User save(User entity);

    User save(Saveable<User, UserMapper> dto);

    Optional<User> getByEmail(String email);

    boolean isExistUserByEmail(String email);

    User findUserByPasswordToken(String passwordResetToken);

    List<User> getAll();

    void deleteUserById(Long id);

    List<UserResponseForStats> getUsersByDate(LocalDate start, LocalDate end);

    void deleteTokenByToken(String token);
}