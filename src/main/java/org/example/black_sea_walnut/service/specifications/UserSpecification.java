package org.example.black_sea_walnut.service.specifications;

import lombok.experimental.UtilityClass;
import org.example.black_sea_walnut.dto.admin.user.UserResponseForView;
import org.example.black_sea_walnut.entity.User;
import org.example.black_sea_walnut.enums.RegisterType;
import org.example.black_sea_walnut.enums.UserStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class UserSpecification {
    public static Specification<User> getSpecification(UserResponseForView dto) {
        Specification<User> specification = Specification.where(null);
        if (dto.getId() != null) {
            specification = specification.and(hasId(dto.getId()));
        }
        if (dto.getFullName() != null && !dto.getFullName().isBlank()) {
            specification = specification.and(likeFullName(dto.getFullName()));
        }
        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            specification = specification.and(likeEmail(dto.getEmail()));
        }
        if (dto.getPhone() != null && !dto.getPhone().isBlank()) {
            specification = specification.and(likePhone(dto.getPhone()));
        }
        if (dto.getDate() != null && !dto.getDate().isBlank()) {
            LocalDate date = LocalDate.parse(dto.getDate(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            specification = specification.and(hasDate(date));
        }
        if (dto.getUserStatus() != null && !dto.getUserStatus().isBlank()) {
            specification = specification.and(hasUserStatus(dto.getUserStatus()));
        }
        if (dto.getRegistrationType() != null && !dto.getRegistrationType().isBlank()) {
            specification = specification.and(hasRegistryType(dto.getRegistrationType()));
        }
        return specification;
    }

    private static Specification<User> hasRegistryType(String registryType) {
        RegisterType registerType = RegisterType.valueOf(registryType.toLowerCase());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("registerType"), registerType);
    }

    private static Specification<User> hasUserStatus(String statusPayment) {
        UserStatus userStatus = UserStatus.valueOf(statusPayment);
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), userStatus);
    }

    private static Specification<User> hasDate(LocalDate date) {
        LocalDateTime startDay = date.atStartOfDay();
        LocalDateTime endDay = date.atTime(LocalTime.MAX);
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("dateRegistered"), startDay, endDay));
    }

    private static Specification<User> hasId(Long id) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("id"), id));
    }

    private static Specification<User> likeFullName(String fullName) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("fullName"), "%" + fullName + "%");
    }

    private static Specification<User> likePhone(String phone) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("phone"), "%" + phone + "%");
    }

    private static Specification<User> likeEmail(String email) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("email"), "%" + email + "%");
    }
}
