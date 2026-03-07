package org.example.black_sea_walnut.service.user;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.admin.stats.UserResponseForStats;
import org.example.black_sea_walnut.dto.admin.user.UserResponseForView;
import org.example.black_sea_walnut.dto.admin.user.response.UserFopResponseForAdd;
import org.example.black_sea_walnut.dto.admin.user.response.UserIndividualResponseForAdd;
import org.example.black_sea_walnut.dto.admin.user.response.UserLegalResponseForView;
import org.example.black_sea_walnut.entity.User;
import org.example.black_sea_walnut.enums.Role;
import org.example.black_sea_walnut.mapper.UserMapper;
import org.example.black_sea_walnut.password.PasswordResetTokenService;
import org.example.black_sea_walnut.password.token.VerificationToken;
import org.example.black_sea_walnut.password.token.VerificationTokenRepository;
import org.example.black_sea_walnut.repository.UserRepository;
import org.example.black_sea_walnut.service.*;
import org.example.black_sea_walnut.service.specifications.UserSpecification;
import org.example.black_sea_walnut.service.file.FileProcessable;
import org.example.black_sea_walnut.service.user.adress.HasAdditionalAddress;
import org.example.black_sea_walnut.service.user.adress.HasCountry;
import org.example.black_sea_walnut.service.user.adress.HasMainAddress;
import org.example.black_sea_walnut.util.DatabaseUtil;
import org.example.black_sea_walnut.util.LogUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService, UserAuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ImageService imageService;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenService passwordResetTokenService;
    private final VerificationTokenRepository tokenRepository;
    private final MessageService messageService;
    private final CityService cityService;
    private final RegionService regionService;
    private final CountryService countryService;
    private final DatabaseUtil databaseUtil;

    @Override
    public List<User> getAll() {
        LogUtil.logInfo("Fetching all users");
        return userRepository.findAll();
    }

    @Override
    public List<UserResponseForView> getAllInResponseForView() {
        LogUtil.logInfo("Fetching all users in dto for View");
        return getAll().stream().map(userMapper::toResponseForView).toList();
    }

    @Override
    public PageResponse<UserResponseForView> getAll(UserResponseForView response, Pageable pageable) {
        Page<User> page = userRepository.findAll(UserSpecification.getSpecification(response), pageable);
        List<UserResponseForView> responseDTOView = page.map(userMapper::toResponseForView).stream().toList();
        return new PageResponse<>(responseDTOView, new PageResponse.Metadata(
                page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages()
        ));
    }

    @Override
    public User getById(Long id) {
        LogUtil.logInfo("Fetching user with id: " + id);
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    LogUtil.logError("User with id: " + id + " was not found!", null);
                    return new EntityNotFoundException(messageService.getMessage("error.notfoundUser") + " Id: " + id);
                });
    }

    @Override
    public UserFopResponseForAdd getByIdForFopResponse(Long id) {
        LogUtil.logInfo("Fetching FOP user response for id: " + id);
        return userMapper.toResponseForUserFopAdd(getById(id));
    }

    @Override
    public UserIndividualResponseForAdd getByIdForIndividualResponse(Long id) {
        LogUtil.logInfo("Fetching Individual user response for id: " + id);
        return userMapper.toResponseForUserIndividualAdd(getById(id));
    }

    @Override
    public UserLegalResponseForView getByIdForLegalResponse(Long id) {
        LogUtil.logInfo("Fetching Legal user response for id: " + id);
        return userMapper.toResponseForUserLegalAdd(getById(id));
    }

    @Override
    public User save(User entity) {
        LogUtil.logInfo("Saving user with email: " + entity.getEmail());
        if (entity.getId() == null && !entity.getPassword().isEmpty()) {
            entity.setPassword(passwordEncoder.encode(entity.getPassword()));
            if (entity.getRole() == null) {
                entity.setRole(Role.USER);
            }
        }
        User savedUser = userRepository.save(entity);
        LogUtil.logInfo("User saved with id: " + savedUser.getId());
        return savedUser;
    }

    @SneakyThrows
    @Override
    public User save(Saveable<User,UserMapper> dto) {
        User userToSave = (dto.getId() != null) ? getById(dto.getId()) : new User();
        handleImageProcessing(dto);
        handleAddressMapping(dto, userToSave);
        handleCountryMapping(dto, userToSave);
        dto.updateEntity(userToSave, userMapper);
        return save(userToSave);
    }

    @Override
    public List<UserResponseForStats> getUsersByDate(LocalDate start, LocalDate end) {
        return userMapper.toResponseUsersForStats(userRepository.getUsersBetweenStartDayAndEndDay(start, end));
    }

    @Override
    public Optional<User> getByEmail(String email) {
        LogUtil.logInfo("Fetching user by email: " + email);
        return userRepository.getByEmail(email);
    }

    @Override
    public boolean isExistUserByEmail(String email) {
        LogUtil.logInfo("Checking if user exists by email: " + email);
        boolean exists = userRepository.existsByEmail(email);
        LogUtil.logInfo("User exists: " + exists);
        return exists;
    }

    @Override
    public void createPasswordResetTokenForUser(User user, String passwordToken) {
        LogUtil.logInfo("Creating password reset token for user: " + user.getEmail());
        passwordResetTokenService.createResetPasswordTokenForUser(user, passwordToken);
        LogUtil.logInfo("Password reset token created successfully for user: " + user.getEmail());
    }

    @Override
    public void saveUserVerificationToken(User theUser, String token) {
        LogUtil.logInfo("Saving verification token for user: " + theUser.getEmail());
        var verificationToken = new VerificationToken(token, theUser);
        tokenRepository.save(verificationToken);
        LogUtil.logInfo("Verification token saved successfully for user: " + theUser.getEmail());
    }

    @Override
    public String validatePasswordResetToken(String passwordResetToken) {
        LogUtil.logInfo("Validating password reset token");
        return passwordResetTokenService.validatePasswordResetToken(passwordResetToken);
    }

    @Override
    public User findUserByPasswordToken(String passwordResetToken) {
        LogUtil.logInfo("Finding user by password reset token");
        return passwordResetTokenService.findUserByPasswordToken(passwordResetToken).orElse(null);
    }

    @Override
    public void resetUserPassword(User user, String newPassword) {
        LogUtil.logInfo("Resetting password for user: " + user.getEmail());
        user.setPassword(passwordEncoder.encode(newPassword));
        save(user);
        LogUtil.logInfo("Password reset successful for user: " + user.getEmail());
    }

    @Override
    @Transactional
    public void deleteTokenByToken(String token) {
        LogUtil.logInfo("Deleting password reset token: " + token);
        passwordResetTokenService.deleteTokenByToken(token);
        LogUtil.logInfo("Token deleted successfully: " + token);
    }

    @Override
    public void deleteUserById(Long id) {
        User user = getById(id);
        if (user == null) {
            throw new EntityNotFoundException(messageService.getMessage("error.notfoundUser"));
        }
        if (user.getRole() == Role.SUPER_ADMIN) {
            throw new SecurityException(messageService.getMessage("error.delete.superAdmin"));
        }
        userRepository.deleteById(id);
    }

    private void handleImageProcessing(Saveable<User,UserMapper> dto) throws IOException {
        if (dto instanceof FileProcessable fileDto && isNewImageProvided(fileDto) &&
        dto instanceof Uploadable sub) {
            if (fileDto.getPathToImage() != null) {
                imageService.deleteByPath(fileDto.getPathToImage());
            }
            String newPath = imageService.generatePath(fileDto.getFileImage(),sub);
            fileDto.setPathToImage(newPath);
            imageService.save(fileDto.getFileImage(), newPath);
        }
    }

    private void handleAddressMapping(Saveable<User,UserMapper> dto, User entity) {
        if (dto instanceof HasMainAddress addr) {
            entity.setCity(databaseUtil.findOrThrow(addr.getCityForDeliveryId(), cityService::getById, "City"));
            entity.setRegion(databaseUtil.findOrThrow(addr.getRegionForDeliveryId(), regionService::getById, "Region"));
            entity.setAddress(addr.getAddress());
        }
        if (dto instanceof HasAdditionalAddress addr) {
            entity.setCityAdditional(databaseUtil.findOrThrow(addr.getCityAdditionallyId(), cityService::getById, "City"));
            entity.setRegionAdditional(databaseUtil.findOrThrow(addr.getRegionAdditionallyId(), regionService::getById, "Region"));
            entity.setAddressAdditional(addr.getAddressAdditionally());
        }
    }

    private void handleCountryMapping(Saveable<User,UserMapper> dto, User entity) {
        if (dto instanceof HasCountry countryDto) {
            entity.setCountry(databaseUtil.findOrThrow(countryDto.getIdCountry(), countryService::getById, "Country"));
            if (countryDto.getIdCountryLegal() != null) {
                entity.setCountryAdditional(databaseUtil.findOrThrow(countryDto.getIdCountryLegal(), countryService::getById, "Country"));
            }
        }
    }

    private boolean isNewImageProvided(FileProcessable dto) {
        return dto.getFileImage() != null && !dto.getFileImage().isEmpty();
    }
}
