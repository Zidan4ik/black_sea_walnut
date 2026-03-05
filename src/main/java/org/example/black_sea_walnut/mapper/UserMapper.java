package org.example.black_sea_walnut.mapper;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.admin.stats.UserResponseForStats;
import org.example.black_sea_walnut.dto.admin.user.request.UserFopRequestForAdd;
import org.example.black_sea_walnut.dto.admin.user.request.UserIndividualRequestForAdd;
import org.example.black_sea_walnut.dto.admin.user.request.UserLegalRequestForAdd;
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
import org.example.black_sea_walnut.enums.RegisterType;
import org.example.black_sea_walnut.enums.UserStatus;
import org.example.black_sea_walnut.service.CityService;
import org.example.black_sea_walnut.service.CountryService;
import org.example.black_sea_walnut.service.RegionService;
import org.example.black_sea_walnut.service.user.Saveable;
import org.example.black_sea_walnut.service.user.UserUpdater;
import org.example.black_sea_walnut.service.user.adress.HasAdditionalAddress;
import org.example.black_sea_walnut.service.user.adress.HasMainAddress;
import org.example.black_sea_walnut.util.DatabaseUtil;
import org.example.black_sea_walnut.util.DateUtil;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final OrderMapper orderMapper;
    private final DatabaseUtil databaseUtil;
    private final RegionService regionService;
    private final CityService cityService;
    private final CountryService countryService;

    public UserResponseForView toResponseForView(User entity) {
        int amountOrders = entity.getOrders().size();
        return UserResponseForView
                .builder()
                .id(entity.getId())
                .fullName(entity.getFullName())
                .email(entity.getEmail())
                .date(DateUtil.toFormatDateFromDB(entity.getDateRegistered(), "dd.MM.yyyy"))
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

    public User toEntityFromRequest(UserLegalRequestForAdd dto) {
        User entity = new User();
        entity.setId(dto.getId());
        entity.setFullName(dto.getFullName());
        entity.setPhone(dto.getPhone());
        entity.setEmail(dto.getEmail());
        entity.setRegisterType(RegisterType.fromString(dto.getRegistrationType()));
        entity.setStatus(UserStatus.fromString(dto.getStatus()));
        entity.setPathToImage(dto.getPathToImage());
        entity.setPaymentDetails(dto.getOkpo());
        entity.setIndexAdditional(dto.getIndexLawful());
        entity.setPassword(dto.getPassword());
        entity.setAddressAdditional(dto.getAddressAdditionally());
        entity.setDepartment(Integer.valueOf(dto.getDepartmentForDelivery()));
        entity.setRole(dto.getRole());
        entity.setDateRegistered(LocalDate.now());
        return entity;
    }

    public User toEntityFromRequest(UserFopRequestForAdd dto) {
        User entity = new User();
        mapBaseFields(dto, entity);
        entity.setId(dto.getId());
        entity.setDepartment(Integer.valueOf(dto.getDepartmentForDelivery()));
        entity.setRegisterType(RegisterType.fromString(dto.getRegistrationType()));
        entity.setStatus(UserStatus.fromString(dto.getStatus()));
        entity.setPaymentDetails(dto.getEdrpou());
        entity.setAddressAdditional(dto.getAddressAdditionally());
        entity.setPassword(dto.getPassword());
        entity.setRole(dto.getRole());
        entity.setDateRegistered(LocalDate.now());
        entity.setPathToImage(dto.getPathToImage());
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
                .cityAdditionallyId(entity.getCityAdditional() != null ? entity.getCityAdditional().getId() : null)
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
                .cityAdditionallyId(entity.getCityAdditional() != null ? entity.getCityAdditional().getId() : null)
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

    public UserDtoLegal toDtoForLegal(User entity) {
        return UserDtoLegal
                .builder()
                .id(entity.getId())
                .fullName(entity.getFullName())
                .phone(entity.getPhone())
                .email(entity.getEmail())
                .pathToImage(entity.getPathToImage())
                .company(entity.getCompany())
                .build();
    }

    public UserDtoIndividual toDtoForIndividual(User entity) {
        return UserDtoIndividual
                .builder()
                .id(entity.getId())
                .fullName(entity.getFullName())
                .phone(entity.getPhone())
                .email(entity.getEmail())
                .pathToImage(entity.getPathToImage())
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

    public AddressDtoIndividual toResponseForAddressIndividual(User entity) {
        return AddressDtoIndividual.builder()
                .idCity(entity.getCity() != null ? entity.getCity().getId() : null)
                .idRegion(entity.getRegion() != null ? entity.getRegion().getId() : null)
                .idCountry(entity.getCountry() != null ? entity.getCountry().getId() : null)
                .address(entity.getAddress())
                .build();
    }

    public AddressDtoLegal toResponseForAddressLegal(User entity) {
        return AddressDtoLegal.builder()
                .idCity(entity.getCity() != null ? entity.getCity().getId() : null)
                .idRegion(entity.getRegion() != null ? entity.getRegion().getId() : null)
                .idCountry(entity.getCountry() != null ? entity.getCountry().getId() : null)
                .idCityLegal(entity.getCityAdditional() != null ? entity.getCityAdditional().getId() : null)
                .idRegionLegal(entity.getRegionAdditional() != null ? entity.getRegionAdditional().getId() : null)
                .idCountryLegal(entity.getCountryAdditional() != null ? entity.getCountryAdditional().getId() : null)
                .address(entity.getAddress())
                .addressLegal(entity.getAddressAdditional())
                .okpo(entity.getPaymentDetails())
                .index(entity.getIndexAdditional())
                .build();
    }

    public void updateEntityFromRequest(AddressDtoIndividual dto, User entity) {
        mapMainAddress(dto,entity);
        entity.setCountry(databaseUtil.findOrThrow(dto.getIdCountry(), countryService::getById, "Country"));
    }

    public void updateEntityFromRequest(AddressDtoLegal dto, User entity) {
        entity.setPaymentDetails(dto.getOkpo());
        mapMainAddress(dto,entity);
        mapAdditionalAddress(dto,entity);
        entity.setCountry(databaseUtil.findOrThrow(dto.getIdCountry(), countryService::getById, "Country"));
        entity.setCountryAdditional(databaseUtil.findOrThrow(dto.getIdCountryLegal(), countryService::getById, "Country"));
    }

    public void updateEntityFromRequest(UserDtoLegal dto, User entity) {
        mapBaseFields(dto, entity);
        entity.setCompany(dto.getCompany());
        entity.setPathToImage(dto.getPathToImage());
    }

    public void updateEntityFromRequest(UserDtoIndividual dto, User entity) {
        mapBaseFields(dto, entity);
        entity.setPathToImage(dto.getPathToImage());
    }

    public void updateEntityFromRequest(UserFopRequestForAdd dto, User entity) {
        mapBaseFields(dto, entity);
        entity.setDepartment(dto.getDepartmentAsInt());
        entity.setPaymentDetails(dto.getEdrpou());
        entity.setPathToImage(dto.getPathToImage());
        mapMainAddress(dto,entity);
        mapAdditionalAddress(dto,entity);
    }

    public void updateEntityFromRequest(UserIndividualRequestForAdd dto, User entity) {
        mapBaseFields(dto, entity);
        entity.setPathToImage(dto.getPathToImage());
        entity.setDepartment(Math.toIntExact(dto.getDepartmentForDeliveryId()));
        mapMainAddress(dto,entity);
    }

    public void updateEntityFromRequest(UserLegalRequestForAdd dto, User entity) {
        mapBaseFields(dto, entity);
        entity.setDepartment(Integer.parseInt(dto.getDepartmentForDelivery()));
        entity.setPaymentDetails(dto.getOkpo());
        entity.setIndexAdditional(dto.getIndexLawful());
        entity.setPathToImage(dto.getPathToImage());
        mapMainAddress(dto,entity);
        mapAdditionalAddress(dto,entity);
    }

    public void updateEntityFromRequest(UserRequestForRegistration dto, User entity) {
        mapBaseFields(dto, entity);
        entity.setPathToImage(dto.getPathToImage());
        entity.setRole(dto.getRole());
        entity.setDateRegistered(LocalDate.now());
        entity.setIndexAdditional(dto.getIndex());
        entity.setRegisterType(RegisterType.fromString(dto.getRegistrationType()));
        entity.setStatus(UserStatus.isActive);
        entity.setPaymentDetails(dto.getPaymentDetails());
        entity.setPassword(dto.getPassword());
        entity.setFop(dto.isFop());

        entity.setCountry(databaseUtil.findOrThrow(dto.getCountryForDeliveryId(), countryService::getById, "Country"));
        entity.setCountryAdditional(databaseUtil.findOrThrow(dto.getCountryForDeliveryIdLegal(), countryService::getById, "Country"));
        mapMainAddress(dto,entity);
        mapAdditionalAddress(dto,entity);
    }

    public void mapBaseFields(UserUpdater dto, User entity) {
        entity.setFullName(dto.getFullName());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
    }

    private void mapMainAddress(Saveable dto, User entity){
        if(dto instanceof HasMainAddress addressDto){
            entity.setCity(databaseUtil.findOrThrow(addressDto.getCityForDeliveryId(), cityService::getById, "City"));
            entity.setRegion(databaseUtil.findOrThrow(addressDto.getRegionForDeliveryId(), regionService::getById, "Region"));
            entity.setAddress(addressDto.getAddress());
        }
    }

    private void mapAdditionalAddress(Saveable dto, User entity){
        if(dto instanceof HasAdditionalAddress addressDto){
            entity.setCity(databaseUtil.findOrThrow(addressDto.getCityAdditionallyId(), cityService::getById, "City"));
            entity.setRegion(databaseUtil.findOrThrow(addressDto.getRegionAdditionallyId(), regionService::getById, "Region"));
            entity.setAddress(addressDto.getAddressAdditionally());
        }
    }
}
