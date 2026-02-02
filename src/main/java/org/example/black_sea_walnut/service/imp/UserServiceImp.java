package org.example.black_sea_walnut.service.imp;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.admin.stats.UserResponseForStats;
import org.example.black_sea_walnut.dto.admin.user.UserResponseForView;
import org.example.black_sea_walnut.dto.admin.user.request.UserFopRequestForView;
import org.example.black_sea_walnut.dto.admin.user.request.UserIndividualRequestForAdd;
import org.example.black_sea_walnut.dto.admin.user.request.UserLegalRequestForView;
import org.example.black_sea_walnut.dto.admin.user.response.UserFopResponseForAdd;
import org.example.black_sea_walnut.dto.admin.user.response.UserIndividualResponseForAdd;
import org.example.black_sea_walnut.dto.admin.user.response.UserLegalResponseForView;
import org.example.black_sea_walnut.dto.web.security.UserRequestForRegistration;
import org.example.black_sea_walnut.dto.web.user.AddressDtoIndividual;
import org.example.black_sea_walnut.dto.web.user.AddressDtoLegal;
import org.example.black_sea_walnut.dto.web.user.UserDtoIndividual;
import org.example.black_sea_walnut.dto.web.user.UserDtoLegal;
import org.example.black_sea_walnut.entity.User;
import org.example.black_sea_walnut.enums.MediaType;
import org.example.black_sea_walnut.enums.RegisterType;
import org.example.black_sea_walnut.enums.Role;
import org.example.black_sea_walnut.enums.UserStatus;
import org.example.black_sea_walnut.mapper.UserMapper;
import org.example.black_sea_walnut.password.PasswordResetTokenRepository;
import org.example.black_sea_walnut.password.PasswordResetTokenService;
import org.example.black_sea_walnut.password.token.VerificationToken;
import org.example.black_sea_walnut.password.token.VerificationTokenRepository;
import org.example.black_sea_walnut.repository.UserRepository;
import org.example.black_sea_walnut.service.*;
import org.example.black_sea_walnut.service.specifications.UserSpecification;
import org.example.black_sea_walnut.util.LogUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ImageService imageService;
    private final RegionService regionService;
    private final CityService cityService;
    private final PasswordEncoder passwordEncoder;
    private final CountryService countryService;
    private final PasswordResetTokenService passwordResetTokenService;
    private final VerificationTokenRepository tokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final MessageSource messageSource;

    @Value("${upload.path}")
    private String contextPath;

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
                    return new EntityNotFoundException(getMessage("error.notfoundUser") + " Id: " + id);
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
    public User save(UserFopRequestForView dto) {
        User userToSave;
        if (dto.getId() != null) {
            userToSave = getById(dto.getId());

            if (dto.getPathToImage().isEmpty()) {
                imageService.deleteByPath(userToSave.getPathToImage());
            }

            if (dto.getFileImage() != null) {
                String generatedPath = contextPath + "/users/" + MediaType.image + "/" + imageService.generateFileName(dto.getFileImage());
                dto.setPathToImage(generatedPath);
            }

            userMapper.updateEntityFromRequest(dto, userToSave);
        } else {
            userToSave = userMapper.toEntityFromRequest(dto);
        }
        imageService.save(dto.getFileImage(), dto.getPathToImage());


        userToSave.setCity(findOrThrow(dto.getCityForDeliveryId(), cityService::getById, "City"));
        userToSave.setCityAdditional(findOrThrow(dto.getCityAdditionallyId(), cityService::getById, "City"));
        userToSave.setRegion(findOrThrow(dto.getRegionForDeliveryId(), regionService::getById, "Region"));
        userToSave.setRegionAdditional(findOrThrow(dto.getRegionAdditionallyId(), regionService::getById, "Region"));

        return save(userToSave);
    }

    @SneakyThrows
    @Override
    public User save(UserIndividualRequestForAdd dto) {
        User userToSave;
        if (dto.getId() != null) {
            userToSave = getById(dto.getId());

            if (dto.getPathToImage().isEmpty()) {
                imageService.deleteByPath(userToSave.getPathToImage());
            }

            if (dto.getFileImage() != null) {
                String generatedPath = contextPath + "/users/" + MediaType.image + "/" + imageService.generateFileName(dto.getFileImage());
                dto.setPathToImage(generatedPath);
            }

            userMapper.updateEntityFromRequest(dto,userToSave);
        }else{
            userToSave = userMapper.toEntityFromRequest(dto);
        }
        imageService.save(dto.getFileImage(), dto.getPathToImage());

        userToSave.setCity(findOrThrow(dto.getCityForDeliveryId(), cityService::getById, "City"));
        userToSave.setRegion(findOrThrow(dto.getRegionForDeliveryId(), regionService::getById, "Region"));

        return save(userToSave);
    }

    @SneakyThrows
    @Override
    public User save(UserLegalRequestForView dto) {
        User userToSave;
        if (dto.getId() != null) {
            userToSave = getById(dto.getId());

            if (dto.getPathToImage().isEmpty()) {
                imageService.deleteByPath(userToSave.getPathToImage());
            }

            if (dto.getFileImage() != null) {
                String generatedPath = contextPath + "/users/" + MediaType.image + "/" + imageService.generateFileName(dto.getFileImage());
                dto.setPathToImage(generatedPath);
            }

            userMapper.updateEntityFromRequest(dto,userToSave);
        }else{
            userToSave = userMapper.toEntityFromRequest(dto);
        }
        imageService.save(dto.getFileImage(), dto.getPathToImage());

        userToSave.setCity(findOrThrow(dto.getCityForDeliveryId(), cityService::getById, "City"));
        userToSave.setCityAdditional(findOrThrow(dto.getCityAdditionallyId(), cityService::getById, "City"));
        userToSave.setRegion(findOrThrow(dto.getRegionForDeliveryId(), regionService::getById, "Region"));
        userToSave.setRegionAdditional(findOrThrow(dto.getRegionAdditionallyId(), regionService::getById, "Region"));

        return save(userToSave);
    }

    @SneakyThrows
    @Override
    public User save(UserRequestForRegistration dto) {

        if (dto.getFileImage() != null) {
            String generatedPath = contextPath + "/users/" + MediaType.image + "/" + imageService.generateFileName(dto.getFileImage());
            dto.setPathToImage(generatedPath);
        }
        dto.setStatus(UserStatus.isActive.toString());
        dto.setRole(Role.USER);

        imageService.save(dto.getFileImage(), dto.getPathToImage());
        User userMapped = userMapper.toEntityForRegistration(dto);

        userMapped.setCountry(countryService.getById(dto.getCountryForDeliveryId()).orElse(null));
        userMapped.setCity(cityService.getById(dto.getCityForDeliveryId()).orElse(null));
        userMapped.setRegion(regionService.getById(dto.getRegionForDeliveryId()).orElse(null));

        if (userMapped.getRegisterType().equals(RegisterType.legal)) {
            userMapped.setCountryAdditional(countryService.getById(dto.getCountryForDeliveryId()).orElse(null));
            userMapped.setCityAdditional(cityService.getById(dto.getCityForDeliveryId()).orElse(null));
            userMapped.setRegionAdditional(regionService.getById(dto.getRegionForDeliveryId()).orElse(null));
        }

        return save(userMapped);
    }

    @SneakyThrows
    @Override
    public void save(UserDtoLegal dto) {
        if (dto.getId() != null) {
            User userById = getById(dto.getId());
            if (dto.getPathToImage().isEmpty()) {
                imageService.deleteByPath(userById.getPathToImage());
            }
            if (dto.getFileImage() != null) {
                String generatedPath = contextPath + "/users/" + MediaType.image + "/" + imageService.generateFileName(dto.getFileImage());
                dto.setPathToImage(generatedPath);
            }
            userById.setCompany(dto.getCompany());
            userById.setFullName(dto.getFullName());
            userById.setEmail(dto.getEmail());
            userById.setPhone(dto.getPhone());
            userById.setPathToImage(dto.getPathToImage());
            imageService.save(dto.getFileImage(), dto.getPathToImage());
            save(userById);
        }
    }

    @SneakyThrows
    @Override
    public void save(UserDtoIndividual dto) {
        if (dto.getId() != null) {
            User userById = getById(dto.getId());
            if (dto.getPathToImage() != null && dto.getPathToImage().isEmpty()) {
                imageService.deleteByPath(userById.getPathToImage());
            }
            if (dto.getFileImage() != null) {
                String generatedPath = contextPath + "/users/" + MediaType.image + "/" + imageService.generateFileName(dto.getFileImage());
                dto.setPathToImage(generatedPath);
            }
            userById.setFullName(dto.getFullName());
            userById.setEmail(dto.getEmail());
            userById.setPhone(dto.getPhone());
            userById.setPathToImage(dto.getPathToImage());
            imageService.save(dto.getFileImage(), dto.getPathToImage());
            save(userById);
        }
    }

    @Override
    public void save(AddressDtoIndividual dto) {
        if (dto.getId() != null) {
            User userById = getById(dto.getId());
            userById.setCountry(countryService.getById(dto.getIdCountry()).orElse(null));
            userById.setCity(cityService.getById(dto.getIdCity()).orElse(null));
            userById.setRegion(regionService.getById(dto.getIdRegion()).orElse(null));
            userById.setAddress(dto.getAddress());
            save(userById);
        }
    }

    @Override
    public void save(AddressDtoLegal dto) {
        if (dto.getId() != null) {
            User userById = getById(dto.getId());
            userById.setCountry(countryService.getById(dto.getIdCountry()).orElse(null));
            userById.setCity(cityService.getById(dto.getIdCity()).orElse(null));
            userById.setRegion(regionService.getById(dto.getIdRegion()).orElse(null));
            userById.setCountryAdditional(countryService.getById(dto.getIdCountryLegal()).orElse(null));
            userById.setCityAdditional(cityService.getById(dto.getIdCityLegal()).orElse(null));
            userById.setRegionAdditional(regionService.getById(dto.getIdRegionLegal()).orElse(null));
            userById.setAddress(dto.getAddress());
            userById.setAddressAdditional(dto.getAddressLegal());
            userById.setPaymentDetails(dto.getOkpo());
            save(userById);
        }
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
            throw new EntityNotFoundException(getMessage("error.notfoundUser"));
        }
        if (user.getRole() == Role.SUPER_ADMIN) {
            throw new SecurityException(getMessage("error.delete.superAdmin"));
        }
        userRepository.deleteById(id);
    }

    private String getMessage(String code) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }

    private <T> T findOrThrow(Long id, Function<Long, Optional<T>> findMethod, String entityName) {
        if (id == null) return null;
        return findMethod.apply(id).
                orElseThrow(() -> new EntityNotFoundException
                        (entityName + " with id: " + id + " was not found!")
                );
    }
}
