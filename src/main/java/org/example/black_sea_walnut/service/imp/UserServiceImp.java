package org.example.black_sea_walnut.service.imp;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.stats.UserResponseForStats;
import org.example.black_sea_walnut.dto.user.UserResponseForView;
import org.example.black_sea_walnut.dto.user.request.UserFopRequestForView;
import org.example.black_sea_walnut.dto.user.request.UserIndividualRequestForAdd;
import org.example.black_sea_walnut.dto.user.request.UserLegalRequestForView;
import org.example.black_sea_walnut.dto.user.response.UserFopResponseForAdd;
import org.example.black_sea_walnut.dto.user.response.UserIndividualResponseForAdd;
import org.example.black_sea_walnut.dto.user.response.UserLegalResponseForView;
import org.example.black_sea_walnut.entity.User;
import org.example.black_sea_walnut.enums.MediaType;
import org.example.black_sea_walnut.enums.Role;
import org.example.black_sea_walnut.mapper.UserMapper;
import org.example.black_sea_walnut.repository.UserRepository;
import org.example.black_sea_walnut.service.CityService;
import org.example.black_sea_walnut.service.ImageService;
import org.example.black_sea_walnut.service.RegionService;
import org.example.black_sea_walnut.service.UserService;
import org.example.black_sea_walnut.service.specifications.UserSpecification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ImageService imageService;
    private final RegionService regionService;
    private final CityService cityService;
    private final PasswordEncoder passwordEncoder;

    @Value("${upload.path}")
    private String contextPath;

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public List<UserResponseForView> getAllInResponseForView() {
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
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with id:" + id + " was not found!"));
    }

    @Override
    public UserFopResponseForAdd getByIdForFopResponse(Long id) {
        return userMapper.toResponseForUserFopAdd(getById(id));
    }

    @Override
    public UserIndividualResponseForAdd getByIdForIndividualResponse(Long id) {
        return userMapper.toResponseForUserIndividualAdd(getById(id));
    }

    @Override
    public UserLegalResponseForView getByIdForLegalResponse(Long id) {
        return userMapper.toResponseForUserLegalAdd(getById(id));
    }


    @Override
    public User save(User entity) {
        if (entity.getId() == null && !entity.getPassword().isEmpty()) {
            entity.setPassword(passwordEncoder.encode(entity.getPassword()));
            entity.setRole(Role.USER);
        }
        return userRepository.save(entity);
    }

    @SneakyThrows
    @Override
    public User save(UserFopRequestForView dto) {
        if (dto.getId() != null) {
            User userById = getById(dto.getId());
            if (dto.getPathToImage().isEmpty()) {
                imageService.deleteByPath(userById.getPathToImage());
            }
            if (dto.getFileImage() != null) {
                String generatedPath = contextPath + "/users/" + MediaType.image + "/" + imageService.generateFileName(dto.getFileImage());
                dto.setPathToImage(generatedPath);
            }
            dto.setPassword(userById.getPassword());
            dto.setRole(dto.getRole());
            dto.setStatus(userById.getStatus().toString());
            dto.setRegistrationType(userById.getRegisterType().toString());
        }
        imageService.save(dto.getFileImage(), dto.getPathToImage());
        User userMapped = userMapper.toEntityFromRequest(dto);
        Long cityId = dto.getCityForDeliveryId();
        if (cityId != null) {
            userMapped.setCity(cityService.getById(cityId));
        }
        Long cityAdditionallyId = dto.getCityAdditionallyId();
        if (cityAdditionallyId != null) {
            userMapped.setCityAdditional(cityService.getById(cityAdditionallyId));
        }
        Long regionId = dto.getRegionForDeliveryId();
        if (regionId != null) {
            userMapped.setRegion(regionService.getById(regionId));
        }
        Long regionAdditionallyId = dto.getRegionAdditionallyId();
        if (regionAdditionallyId != null) {
            userMapped.setRegionAdditional(regionService.getById(regionAdditionallyId));
        }
        return save(userMapped);
    }

    @SneakyThrows
    @Override
    public User save(UserIndividualRequestForAdd dto) {
        if (dto.getId() != null) {
            User userById = getById(dto.getId());
            if (dto.getPathToImage().isEmpty()) {
                imageService.deleteByPath(userById.getPathToImage());
            }
            if (dto.getFileImage() != null) {
                String generatedPath = contextPath + "/users/" + MediaType.image + "/" + imageService.generateFileName(dto.getFileImage());
                dto.setPathToImage(generatedPath);
            }
            dto.setPassword(userById.getPassword());
            dto.setRole(userById.getRole());
            dto.setStatus(userById.getStatus().toString());
            dto.setRegistrationType(userById.getRegisterType().toString());
        }
        imageService.save(dto.getFileImage(), dto.getPathToImage());
        User userMapped = userMapper.toEntityFromRequest(dto);
//        userMapped.setCity(cityService.getById(dto.getCityForDeliveryId()));
//        userMapped.setRegion(regionService.getById(dto.getRegionForDeliveryId()));
        return save(userMapped);
    }

    @SneakyThrows
    @Override
    public User save(UserLegalRequestForView dto) {
        if (dto.getId() != null) {
            User userById = getById(dto.getId());
            if (dto.getPathToImage().isEmpty()) {
                imageService.deleteByPath(userById.getPathToImage());
            }
            if (dto.getFileImage() != null) {
                String generatedPath = contextPath + "/users/" + MediaType.image + "/" + imageService.generateFileName(dto.getFileImage());
                dto.setPathToImage(generatedPath);
            }
            dto.setPassword(userById.getPassword());
            dto.setRole(dto.getRole());
            dto.setStatus(userById.getStatus().toString());
            dto.setRegistrationType(userById.getRegisterType().toString());
        }
        imageService.save(dto.getFileImage(), dto.getPathToImage());
        User userMapped = userMapper.toEntityFromRequest(dto);
        userMapped.setCity(cityService.getById(dto.getCityForDeliveryId()));
        userMapped.setCityAdditional(cityService.getById(dto.getCityAdditionallyId()));
        userMapped.setRegion(regionService.getById(dto.getRegionForDeliveryId()));
        userMapped.setRegionAdditional(regionService.getById(dto.getRegionAdditionallyId()));
        return save(userMapped);
    }

    @Override
    public List<UserResponseForStats> getUsersByDate(LocalDate start, LocalDate end) {
        return userMapper.toResponseUsersForStats(userRepository.getUsersBetweenStartDayAndEndDay(start, end));
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return userRepository.getByEmail(email);
    }
}
