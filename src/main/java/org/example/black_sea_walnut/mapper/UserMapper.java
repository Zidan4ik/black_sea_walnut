package org.example.black_sea_walnut.mapper;

import lombok.RequiredArgsConstructor;
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
import org.example.black_sea_walnut.enums.RegisterType;
import org.example.black_sea_walnut.enums.UserStatus;
import org.example.black_sea_walnut.util.DateUtil;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final OrderMapper orderMapper;

    public UserResponseForView toResponseForView(User entity) {
        int amountOrders = entity.getOrders().size();
        return UserResponseForView
                .builder()
                .id(entity.getId())
                .fullName(entity.getFullName())
                .email(entity.getEmail())
                .dateOfRegistration(DateUtil.toFormatDateFromDB(entity.getDateRegistered(), "dd.MM.yyyy"))
                .registrationType(entity.getRegisterType().toString())
                .amountOrders(String.valueOf(amountOrders))
                .userStatus(entity.getStatus().toString())
                .phone(entity.getPhone())
                .build();
    }

    public User toEntityFromRequest(UserIndividualRequestForAdd dto) {
        User entity = new User();
        entity.setId(dto.getId());
        entity.setFullName(dto.getFullName());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setDepartment(dto.getDepartmentForDeliveryId());
        entity.setRegisterType(RegisterType.fromString(dto.getRegistrationType()));
        entity.setStatus(UserStatus.fromString(dto.getStatus()));
        entity.setPathToImage(dto.getPathToImage());
        entity.setPassword(dto.getPassword());
        entity.setRole(dto.getRole());
        entity.setDateRegistered(LocalDate.now());
        return entity;
    }

    public User toEntityForRegistration(UserRequestForRegistration dto) {
        User entity = new User();
        entity.setFullName(dto.getFullName());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setRole(dto.getRole());
        entity.setDateRegistered(LocalDate.now());
        entity.setAddress(dto.getAddress());
        entity.setAddressAdditional(dto.getAddressLegal());
        entity.setIndexAdditional(dto.getIndex());
        entity.setRegisterType(RegisterType.valueOf(dto.getRegistrationType()));
        entity.setStatus(UserStatus.valueOf(dto.getStatus()));
        entity.setPaymentDetails(dto.getPaymentDetails());
        entity.setPathToImage(dto.getPathToImage());
        entity.setPassword(dto.getPassword());
        entity.setFop(dto.isFop());
        return entity;
    }

    public User toEntityFromRequest(UserLegalRequestForView dto) {
        User entity = new User();
        entity.setId(dto.getId());
        entity.setFullName(dto.getFullName());
        entity.setPhone(dto.getPhone());
        entity.setEmail(dto.getEmail());
        entity.setRegisterType(RegisterType.fromString(dto.getRegistrationType()));
        entity.setStatus(UserStatus.fromString(dto.getStatus()));
        entity.setPathToImage(dto.getPathToImage());
        entity.setPaymentDetails(dto.getOkpo());
        entity.setIndexAdditional(dto.getIndexAdditionally());
        entity.setPassword(dto.getPassword());
        entity.setAddressAdditional(dto.getAddressAdditionally());
        entity.setDepartment(Integer.valueOf(dto.getDepartmentForDelivery()));
        entity.setRole(dto.getRole());
        entity.setDateRegistered(LocalDate.now());
        return entity;
    }

    public User toEntityFromRequest(UserFopRequestForView dto) {
        User entity = new User();
        entity.setId(dto.getId());
        entity.setFullName(dto.getFullName());
        entity.setPhone(dto.getPhone());
        entity.setEmail(dto.getEmail());
        entity.setDepartment(Integer.valueOf(dto.getDepartmentForDelivery()));
        entity.setRegisterType(RegisterType.fromString(dto.getRegistrationType()));
        entity.setStatus(UserStatus.fromString(dto.getStatus()));
        entity.setPathToImage(dto.getPathToImage());
        entity.setPaymentDetails(dto.getEdrpou());
        entity.setAddressAdditional(dto.getAddressAdditionally());
        entity.setPassword(dto.getPassword());
        entity.setRole(dto.getRole());
        entity.setDateRegistered(LocalDate.now());
        return entity;
    }

    public UserFopResponseForAdd toResponseForUserFopAdd(User entity) {
        return UserFopResponseForAdd
                .builder()
                .id(entity.getId())
                .fullName(entity.getFullName())
                .phone(entity.getPhone())
                .email(entity.getEmail())
                .regionForDeliveryId(entity.getRegion() != null ? entity.getRegion().getId() : null)
                .cityForDeliveryId(entity.getCity() != null ? entity.getCity().getId() : null)
                .departmentForDelivery(String.valueOf(entity.getDepartment()))
                .registrationType(entity.getRegisterType().toString())
                .status(entity.getStatus().toString())
                .pathToImage(entity.getPathToImage())
                .edrpou(entity.getPaymentDetails())
                .regionAdditionallyId(entity.getRegionAdditional() != null ? entity.getRegionAdditional().getId() : null)
                .cityAdditionallyId(entity.getCity() != null ? entity.getCity().getId() : null)
                .addressAdditionally(entity.getAddressAdditional())
                .role(entity.getRole())
                .orders(entity.getOrders().stream().map(orderMapper::toResponseForUserOrderView).toList())
                .build();
    }

    public UserLegalResponseForView toResponseForUserLegalAdd(User entity) {
        return UserLegalResponseForView
                .builder()
                .id(entity.getId())
                .fullName(entity.getFullName())
                .phone(entity.getPhone())
                .email(entity.getEmail())
                .regionForDeliveryId(entity.getRegion() != null ? entity.getRegion().getId() : null)
                .cityForDeliveryId(entity.getCity() != null ? entity.getCity().getId() : null)
                .departmentForDelivery(String.valueOf(entity.getDepartment()))
                .registrationType(entity.getRegisterType().toString())
                .status(entity.getStatus().toString())
                .pathToImage(entity.getPathToImage())
                .okpo(entity.getPaymentDetails())
                .regionAdditionallyId(entity.getRegionAdditional() != null ? entity.getRegionAdditional().getId() : null)
                .cityAdditionallyId(entity.getCity() != null ? entity.getCity().getId() : null)
                .addressAdditionally(entity.getAddressAdditional())
                .indexLawful(entity.getIndexAdditional())
                .role(entity.getRole())
                .orders(entity.getOrders().stream().map(orderMapper::toResponseForUserOrderView).toList())
                .build();
    }

    public UserIndividualResponseForAdd toResponseForUserIndividualAdd(User entity) {
        return UserIndividualResponseForAdd
                .builder()
                .id(entity.getId())
                .fullName(entity.getFullName())
                .phone(entity.getPhone())
                .email(entity.getEmail())
                .regionForDeliveryId(entity.getRegion() != null ? entity.getRegion().getId() : null)
                .cityForDeliveryId(entity.getCity() != null ? entity.getCity().getId() : null)
                .departmentForDelivery(entity.getDepartment())
                .registrationType(entity.getRegisterType().toString())
                .status(entity.getStatus().toString())
                .pathToImage(entity.getPathToImage())
                .role(entity.getRole())
                .orders(entity.getOrders().stream().map(orderMapper::toResponseForUserOrderView).toList())
                .build();
    }

    public List<UserResponseForStats> toResponseUsersForStats(List<Object[]> entities) {
        List<UserResponseForStats> listDto = new ArrayList<>();
        for (Object[] o : entities) {
            listDto.add(
                    UserResponseForStats
                            .builder()
                            .status(String.valueOf(o[0]))
                            .count((long) o[1])
                            .build()
            );
        }
        return listDto;
    }
}
