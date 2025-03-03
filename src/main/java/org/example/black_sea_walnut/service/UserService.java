package org.example.black_sea_walnut.service;

import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.stats.UserResponseForStats;
import org.example.black_sea_walnut.dto.user.request.UserFopRequestForView;
import org.example.black_sea_walnut.dto.user.request.UserIndividualRequestForAdd;
import org.example.black_sea_walnut.dto.user.request.UserLegalRequestForView;
import org.example.black_sea_walnut.dto.user.UserResponseForView;
import org.example.black_sea_walnut.dto.user.response.UserFopResponseForAdd;
import org.example.black_sea_walnut.dto.user.response.UserIndividualResponseForAdd;
import org.example.black_sea_walnut.dto.user.response.UserLegalResponseForView;
import org.example.black_sea_walnut.dto.web.security.UserRequestForRegistration;
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

    List<UserResponseForStats> getUsersByDate(LocalDate start, LocalDate end);

    Optional<User> getByEmail(String email);

    boolean isExistUserByEmail(String email);
}