package org.example.black_sea_walnut.service.imp;


import jakarta.persistence.EntityNotFoundException;
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
import org.example.black_sea_walnut.entity.City;
import org.example.black_sea_walnut.entity.Country;
import org.example.black_sea_walnut.entity.Region;
import org.example.black_sea_walnut.entity.User;
import org.example.black_sea_walnut.enums.RegisterType;
import org.example.black_sea_walnut.enums.Role;
import org.example.black_sea_walnut.enums.UserStatus;
import org.example.black_sea_walnut.mapper.UserMapper;
import org.example.black_sea_walnut.password.PasswordResetTokenService;
import org.example.black_sea_walnut.password.token.VerificationToken;
import org.example.black_sea_walnut.password.token.VerificationTokenRepository;
import org.example.black_sea_walnut.repository.UserRepository;
import org.example.black_sea_walnut.service.CityService;
import org.example.black_sea_walnut.service.CountryService;
import org.example.black_sea_walnut.service.ImageService;
import org.example.black_sea_walnut.service.RegionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImpTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private PasswordResetTokenService passwordResetTokenService;

    @Mock
    private VerificationTokenRepository tokenRepository;

    @Mock
    private ImageService imageService;

    @Mock
    private CityService cityService;

    @Mock
    private CountryService countryService;

    @Mock
    private RegionService regionService;


    private final String contextPath = "/context";

    @InjectMocks
    private UserServiceImp userService;

    private User user;

    private UserLegalRequestForView dto;
    private UserIndividualRequestForAdd dtoIndividual;
    private UserRequestForRegistration userRequestForRegistration;
    private UserDtoLegal userDtoLegal;
    private UserDtoIndividual userDtoIndividual;
    private UserFopRequestForView userFopRequestForView;


    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setStatus(UserStatus.isActive);
        user.setRegisterType(RegisterType.individual);

        dto = new UserLegalRequestForView();
        dto.setId(1L);
        dto.setFullName("John Doe");
        dto.setPhone("+1234567890");
        dto.setEmail("test@example.com");
        dto.setRegionForDeliveryId(30L);
        dto.setCityForDeliveryId(10L);
        dto.setDepartmentForDelivery("Main Warehouse");
        dto.setRegistrationType("LLC");
        dto.setStatus("Active");
        dto.setPathToImage("");
        dto.setFileImage(new MockMultipartFile("file", "image.jpeg", "image/jpeg", new byte[]{1, 2, 3, 4}));
        dto.setOkpo("12345678");
        dto.setRegionAdditionallyId(40L);
        dto.setCityAdditionallyId(20L);
        dto.setAddressAdditionally("1234 Walnut St, CityName");
        dto.setIndexLawful("56789");
        dto.setPassword("securePass!123");
        dto.setRole(Role.USER);

        MockMultipartFile file = new MockMultipartFile("file", "image.jpeg", "image/jpeg", new byte[]{1, 2, 3, 4});

        dtoIndividual = new UserIndividualRequestForAdd();
        dtoIndividual.setId(1L);
        dtoIndividual.setPathToImage("");
        dtoIndividual.setFileImage(mock(MultipartFile.class));
        dtoIndividual.setCityForDeliveryId(10L);
        dtoIndividual.setRegionForDeliveryId(20L);
        dtoIndividual.setPassword("1234");

        userRequestForRegistration = UserRequestForRegistration.builder().build();
        userRequestForRegistration.setFileImage(file);
        userRequestForRegistration.setCountryForDeliveryId(1L);
        userRequestForRegistration.setCityForDeliveryId(2L);
        userRequestForRegistration.setRegionForDeliveryId(3L);

        userDtoLegal = UserDtoLegal.builder().build();
        userDtoLegal.setId(1L);
        userDtoLegal.setFileImage(file);
        userDtoLegal.setPathToImage("");
        userDtoLegal.setCompany("Company Name");
        userDtoLegal.setFullName("John Doe");
        userDtoLegal.setEmail("legal@example.com");
        userDtoLegal.setPhone("+123456789");

        userDtoIndividual = UserDtoIndividual.builder().build();
        userDtoIndividual.setId(1L);
        userDtoIndividual.setFileImage(file);
        userDtoIndividual.setPathToImage("");
        userDtoIndividual.setFullName("John Doe");
        userDtoIndividual.setEmail("individual@example.com");
        userDtoIndividual.setPhone("+123456789");

        userFopRequestForView = new UserFopRequestForView();
        userFopRequestForView.setId(1L);
        userFopRequestForView.setPathToImage("");
        MultipartFile fileImage = mock(MultipartFile.class);
        userFopRequestForView.setFileImage(fileImage);
        userFopRequestForView.setCityForDeliveryId(10L);
        userFopRequestForView.setCityAdditionallyId(20L);
        userFopRequestForView.setRegionForDeliveryId(30L);
        userFopRequestForView.setRegionAdditionallyId(40L);
        userFopRequestForView.setStatus("isActive");
    }

    @Test
    void testGetAll() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        List<User> users = userService.getAll();
        assertNotNull(users);
        assertEquals(1, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        User foundUser = userService.getById(1L);
        assertNotNull(foundUser);
        assertEquals(1L, foundUser.getId());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetById_NotFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class, () -> userService.getById(2L));
        assertTrue(exception.getMessage().contains("User with id:"));
        verify(userRepository, times(1)).findById(2L);
    }

    @Test
    void testSaveUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        User savedUser = userService.save(user);
        assertNotNull(savedUser);
        assertEquals("test@example.com", savedUser.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testSaveUser_WhenIdIsNull() {
        user.setId(null);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        User savedUser = userService.save(user);
        assertNotNull(savedUser);
        assertEquals("test@example.com", savedUser.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testSaveUser_WhenPasswordIsEmpty() {
        user.setId(null);
        user.setPassword("");
        when(userRepository.save(any(User.class))).thenReturn(user);
        User savedUser = userService.save(user);
        assertNotNull(savedUser);
        assertEquals("test@example.com", savedUser.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testGetAllInResponseForView() {
        UserResponseForView response = UserResponseForView.builder().build();
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toResponseForView(user)).thenReturn(response);
        List<UserResponseForView> responses = userService.getAllInResponseForView();
        assertNotNull(responses);
        assertEquals(1, responses.size());
        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(1)).toResponseForView(user);
    }

    @Test
    void getAll_shouldReturnPageResponse() {
        Pageable pageable = mock(Pageable.class);
        UserResponseForView response = UserResponseForView.builder().build();
        Page<User> page = new PageImpl<>(List.of(user));
        Specification<User> specification = Specification.where(null);
        when(userRepository.findAll(specification, pageable)).thenReturn(page);
        when(userMapper.toResponseForView(any())).thenReturn(UserResponseForView.builder().build());

        PageResponse<UserResponseForView> result = userService.getAll(response, pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(userRepository, times(1)).findAll(specification, pageable);
    }

    @Test
    void getByIdForFopResponse_shouldReturnMappedResponse() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toResponseForUserFopAdd(user)).thenReturn(UserFopResponseForAdd.builder().build());
        UserFopResponseForAdd result = userService.getByIdForFopResponse(1L);
        assertNotNull(result);
        verify(userMapper).toResponseForUserFopAdd(user);
    }

    @Test
    void getByIdForLegalResponse_shouldReturnMappedResponse() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toResponseForUserLegalAdd(user)).thenReturn(UserLegalResponseForView.builder().build());
        UserLegalResponseForView result = userService.getByIdForLegalResponse(1L);
        assertNotNull(result);
        verify(userMapper).toResponseForUserLegalAdd(user);
    }

    @Test
    void getByIdForIndividualResponse_shouldReturnMappedResponse() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toResponseForUserIndividualAdd(user)).thenReturn(UserIndividualResponseForAdd.builder().build());

        UserIndividualResponseForAdd result = userService.getByIdForIndividualResponse(1L);

        assertNotNull(result);
        verify(userMapper).toResponseForUserIndividualAdd(user);
    }


    @Test
    void getUsersByDate_shouldReturnMappedList() {
        when(userRepository.getUsersBetweenStartDayAndEndDay(any(), any())).thenReturn(new ArrayList<>());
        when(userMapper.toResponseUsersForStats(any())).thenReturn(List.of(UserResponseForStats.builder().build()));

        List<UserResponseForStats> result = userService.getUsersByDate(LocalDate.now(), LocalDate.now());

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getByEmail_shouldReturnUser() {
        when(userRepository.getByEmail("test@example.com")).thenReturn(Optional.of(user));

        Optional<User> result = userService.getByEmail("test@example.com");

        assertTrue(result.isPresent());
    }

    @Test
    void isExistUserByEmail_shouldReturnTrueIfExists() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        boolean exists = userService.isExistUserByEmail("test@example.com");

        assertTrue(exists);
    }

    @Test
    void createPasswordResetTokenForUser_shouldCallServiceMethod() {
        doNothing().when(passwordResetTokenService).createResetPasswordTokenForUser(user, "token123");

        userService.createPasswordResetTokenForUser(user, "token123");

        verify(passwordResetTokenService).createResetPasswordTokenForUser(user, "token123");
    }

    @Test
    void validatePasswordResetToken_shouldReturnValidationResult() {
        when(passwordResetTokenService.validatePasswordResetToken("token123")).thenReturn("VALID");

        String result = userService.validatePasswordResetToken("token123");

        assertEquals("VALID", result);
    }

    @Test
    void testSaveUserFopRequestForView_WithImageAndCityData() {
        City cityForDelivery = new City();
        cityForDelivery.setId(10L);
        City cityAdditionally = new City();
        cityAdditionally.setId(20L);
        Region regionForDelivery = new Region();
        regionForDelivery.setId(30L);
        Region regionAdditionally = new Region();
        regionAdditionally.setId(40L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cityService.getById(10L)).thenReturn(Optional.of(cityForDelivery));
        when(cityService.getById(20L)).thenReturn(Optional.of(cityAdditionally));
        when(regionService.getById(30L)).thenReturn(Optional.of(regionForDelivery));
        when(regionService.getById(40L)).thenReturn(Optional.of(regionAdditionally));
        when(userMapper.toEntityFromRequest(any(UserFopRequestForView.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.save(userFopRequestForView);

        verify(userRepository).findById(1L);
        verify(cityService).getById(10L);
        verify(cityService).getById(20L);
        verify(regionService).getById(30L);
        verify(regionService).getById(40L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testSaveUserFopRequestForView_WithImageAndCityData_WhenFileNullAndPathNotEmpty() {
        userFopRequestForView.setFileImage(null);
        userFopRequestForView.setPathToImage("/path/to/image");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cityService.getById(anyLong())).thenReturn(Optional.of(new City()));
        when(regionService.getById(anyLong())).thenReturn(Optional.of(new Region()));
        when(userMapper.toEntityFromRequest(any(UserFopRequestForView.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.save(userFopRequestForView);
        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testSaveUserIndividualRequestForAdd_WithImage() throws IOException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(imageService.generateFileName(any())).thenReturn("img.jpg");
        when(cityService.getById(anyLong())).thenReturn(Optional.of(new City()));
        when(regionService.getById(anyLong())).thenReturn(Optional.of(new Region()));
        when(userMapper.toEntityFromRequest(dtoIndividual)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        User result = userService.save(dtoIndividual);
        assertNotNull(result);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testSaveUserIndividualRequestForAdd_WhereCannotFoundAddressPlace() {
        userFopRequestForView.setId(null);
        userFopRequestForView.setCityForDeliveryId(null);
        userFopRequestForView.setCityAdditionallyId(null);
        userFopRequestForView.setRegionForDeliveryId(null);
        userFopRequestForView.setRegionAdditionallyId(null);
        when(userMapper.toEntityFromRequest(userFopRequestForView)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        User result = userService.save(userFopRequestForView);
        assertNotNull(result);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testSaveUserShouldThrowEntityNotFoundExceptionWhenCityNotFound() {
        dto.setCityForDeliveryId(999L);
        dto.setRegionForDeliveryId(1L);
        assertThrows(EntityNotFoundException.class, () -> userService.save(userFopRequestForView));
    }

    @Test
    void testSaveUserIndividualRequestForAdd_WithNullId() {
        dtoIndividual.setId(null);
        when(cityService.getById(10L)).thenReturn(Optional.of(new City()));
        when(regionService.getById(20L)).thenReturn(Optional.of(new Region()));
        when(userMapper.toEntityFromRequest(dtoIndividual)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.save(dtoIndividual);

        assertNotNull(result);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testSaveUserIndividualRequestForAdd_WithNullId_WhenNoPositions() {
        dtoIndividual.setId(null);
        dtoIndividual.setRegionForDeliveryId(null);
        dtoIndividual.setCityForDeliveryId(null);
        when(userMapper.toEntityFromRequest(dtoIndividual)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.save(dtoIndividual);

        assertNotNull(result);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testSaveUserIndividualRequestForAdd_WithNullFileAndNotEmptyPath() throws IOException {
        dtoIndividual.setPathToImage("/path/to/image");
        dtoIndividual.setFileImage(null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cityService.getById(10L)).thenReturn(Optional.of(new City()));
        when(regionService.getById(20L)).thenReturn(Optional.of(new Region()));
        when(userMapper.toEntityFromRequest(dtoIndividual)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.save(dtoIndividual);

        assertNotNull(result);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testSaveUserLegal_WhenDtoHasId() throws IOException {
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        when(userMapper.toEntityFromRequest(dto)).thenReturn(user);
        when(cityService.getById(anyLong())).thenReturn(Optional.of(new City()));
        when(regionService.getById(anyLong())).thenReturn(Optional.of(new Region()));
        when(userRepository.save(any(User.class))).thenReturn(user);
        User result = userService.save(dto);
        verify(imageService, times(1)).save(any(), any());
        verify(userMapper, times(1)).toEntityFromRequest(dto);
        assertEquals(user, result);
    }

    @Test
    void testSaveUserLegal_WhenDtoHasFileAndPathNull() throws IOException {
        dto.setFileImage(null);
        dto.setPathToImage("/path/to/file");
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        when(userMapper.toEntityFromRequest(dto)).thenReturn(user);
        when(cityService.getById(anyLong())).thenReturn(Optional.of(new City()));
        when(regionService.getById(anyLong())).thenReturn(Optional.of(new Region()));
        when(userRepository.save(any(User.class))).thenReturn(user);
        User result = userService.save(dto);
        verify(imageService, times(1)).save(any(), any());
        verify(userMapper, times(1)).toEntityFromRequest(dto);
        assertEquals(user, result);
    }

    @Test
    void testSaveUserLegal_WhenPositionsAreNotFound() throws IOException {
        dto.setFileImage(null);
        dto.setPathToImage("/path/to/file");
        dto.setRegionForDeliveryId(null);
        dto.setRegionAdditionallyId(null);
        dto.setCityAdditionallyId(null);
        dto.setCityForDeliveryId(null);
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        when(userMapper.toEntityFromRequest(dto)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        User result = userService.save(dto);
        verify(imageService, times(1)).save(any(), any());
        verify(userMapper, times(1)).toEntityFromRequest(dto);
        assertEquals(user, result);
    }

    @Test
    void testSaveUserLegal_WhenDtoNoHasId() throws IOException {
        dto.setId(null);
        when(userMapper.toEntityFromRequest(dto)).thenReturn(user);
        when(cityService.getById(anyLong())).thenReturn(Optional.of(new City()));
        when(regionService.getById(anyLong())).thenReturn(Optional.of(new Region()));
        when(userRepository.save(any(User.class))).thenReturn(user);
        User result = userService.save(dto);
        verify(imageService, times(1)).save(any(), any());
        verify(userMapper, times(1)).toEntityFromRequest(dto);
        assertEquals(user, result);
    }

    @Test
    void testSaveUserWithCityNotFound() {
        dto.setId(1L);
        assertThrows(EntityNotFoundException.class, () -> {
            userService.save(dto);
        });
    }

    @Test
    void testSaveUserWithRegionNotFound() {
        dto.setId(1L);
        assertThrows(EntityNotFoundException.class, () -> {
            userService.save(dto);
        });
    }

    @Test
    void testSaveUserFopRequestForView_CityNotFound() {
        UserFopRequestForView dto = new UserFopRequestForView();
        dto.setCityForDeliveryId(123L);
        dto.setFileImage(mock(MultipartFile.class));
        dto.setPathToImage("path.jpg");
        when(userMapper.toEntityFromRequest(dto)).thenReturn(new User());
        when(cityService.getById(123L)).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> userService.save(dto));
        assertTrue(exception.getMessage().contains("City with id:123 was not found"));
    }

    @Test
    void testSaveUserRequestForRegistration_Success() {
        when(imageService.generateFileName(any())).thenReturn("generatedName.jpeg");
        when(userMapper.toEntityForRegistration(any())).thenReturn(user);
        when(countryService.getById(anyLong())).thenReturn(Optional.of(new Country()));
        when(cityService.getById(anyLong())).thenReturn(Optional.of(new City()));
        when(regionService.getById(anyLong())).thenReturn(Optional.of(new Region()));
        when(userRepository.save(any())).thenReturn(user);
        User savedUser = userService.save(userRequestForRegistration);
        assertNotNull(savedUser);
        assertEquals(UserStatus.isActive, savedUser.getStatus());
        verify(imageService, times(1)).save(any(), any());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void testSaveUserRequestForRegistration_WhenFileIsNullAndUserIsLegal() {
        user.setRegisterType(RegisterType.legal);
        userRequestForRegistration.setFileImage(null);
        when(userMapper.toEntityForRegistration(any())).thenReturn(user);
        when(countryService.getById(anyLong())).thenReturn(Optional.of(new Country()));
        when(cityService.getById(anyLong())).thenReturn(Optional.of(new City()));
        when(regionService.getById(anyLong())).thenReturn(Optional.of(new Region()));
        when(userRepository.save(any())).thenReturn(user);
        User savedUser = userService.save(userRequestForRegistration);
        assertNotNull(savedUser);
        assertEquals(UserStatus.isActive, savedUser.getStatus());
        verify(imageService, times(1)).save(any(), any());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void testSaveUserRequestForRegistration_WhenException() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class, () -> userService.getById(2L));
        assertTrue(exception.getMessage().contains("User with id:"));
        verify(userRepository, times(1)).findById(2L);
    }

    @Test
    void testSaveUserDtoLegal_Success() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(imageService.generateFileName(any())).thenReturn("generatedName.jpeg");
        when(userRepository.save(any(User.class))).thenReturn(user);
        userService.save(userDtoLegal);
        verify(userRepository, times(1)).findById(anyLong());
        verify(imageService, times(1)).save(any(), any());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void testSaveUserDtoLegal_WhenIdNull() {
        userDtoLegal.setId(null);
        userService.save(userDtoLegal);
    }

    @Test
    void testSaveUserDtoLegal_WhenFileNullAndPathNoEmpty() {
        userDtoLegal.setFileImage(null);
        userDtoLegal.setPathToImage("/path/to/image");
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        userService.save(userDtoLegal);
        verify(userRepository, times(1)).findById(anyLong());
        verify(imageService, times(1)).save(any(), any());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void testSaveUserDtoLegal_UserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userService.save(userDtoLegal));
    }

    @Test
    void testSaveUserDtoIndividual_Success() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(imageService.generateFileName(any())).thenReturn("generatedName.jpeg");
        when(userRepository.save(any(User.class))).thenReturn(user);
        userService.save(userDtoIndividual);
        verify(userRepository, times(1)).findById(anyLong());
        verify(imageService, times(1)).save(any(), any());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void testSaveUserDtoIndividual_WhereFileAndPathNull() {
        userDtoIndividual.setFileImage(null);
        userDtoIndividual.setPathToImage(null);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        userService.save(userDtoIndividual);
        verify(userRepository, times(1)).findById(anyLong());
        verify(imageService, times(1)).save(any(), any());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void testSaveUserDtoIndividual_WherePathIsNotEmpty() {
        userDtoIndividual.setFileImage(null);
        userDtoIndividual.setPathToImage("/path/to/image");
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        userService.save(userDtoIndividual);
        verify(userRepository, times(1)).findById(anyLong());
        verify(imageService, times(1)).save(any(), any());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void testSaveUserDtoIndividual_WhereIdNotFound() {
        userDtoIndividual.setId(null);
        userService.save(userDtoIndividual);
    }

    @Test
    void testSaveUserDtoIndividual_UserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userService.save(userDtoIndividual));
    }

    @Test
    void saveUser_throwsEntityNotFoundException_whenCityNotFound() {
        UserIndividualRequestForAdd dto = new UserIndividualRequestForAdd();
        dto.setCityForDeliveryId(1L);
        dto.setRegionForDeliveryId(2L);
        when(cityService.getById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userService.save(dto));
    }

    @Test
    void saveFop_throwsEntityNotFoundException_whenCityNotFound() {
        UserRequestForRegistration dto = UserRequestForRegistration.builder().build();
        dto.setCityForDeliveryId(1L);
        dto.setRegionForDeliveryId(2L);
        assertThrows(NullPointerException.class, () -> userService.save(dto));
    }

    @Test
    void saveUser_throwsEntityNotFoundException_whenRegionNotFound() {
        UserIndividualRequestForAdd dto = new UserIndividualRequestForAdd();
        dto.setCityForDeliveryId(1L);
        dto.setRegionForDeliveryId(2L);
        when(cityService.getById(1L)).thenReturn(Optional.of(new City()));
        assertThrows(NullPointerException.class, () -> userService.save(dto));
    }

    @Test
    void findUserByPasswordToken_returnsUser_whenTokenIsValid() {
        String token = "validToken";
        when(passwordResetTokenService.findUserByPasswordToken(token)).thenReturn(Optional.of(user));
        User result = userService.findUserByPasswordToken(token);
        assertNotNull(result);
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void findUserByPasswordToken_returnsNull_whenTokenIsInvalid() {
        String token = "invalidToken";
        when(passwordResetTokenService.findUserByPasswordToken(token)).thenReturn(Optional.empty());
        User result = userService.findUserByPasswordToken(token);
        assertNull(result);
    }

    @Test
    void resetUserPassword_updatesPasswordSuccessfully() {
        String newPassword = "newSecurePassword";
        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(user);
        userService.resetUserPassword(user, newPassword);
        assertEquals(encodedPassword, user.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void deleteTokenByToken_callsPasswordResetTokenService() {
        String token = "tokenToDelete";
        userService.deleteTokenByToken(token);
        verify(passwordResetTokenService, times(1)).deleteTokenByToken(token);
    }

    @Test
    void saveUserVerificationToken_savesTokenSuccessfully() {
        String token = "verificationToken";
        userService.saveUserVerificationToken(user, token);
        verify(tokenRepository, times(1)).save(Mockito.any(VerificationToken.class));
    }


    @Test
    void testSaveAddressDtoIndividual() {
        AddressDtoIndividual dto = AddressDtoIndividual.builder().build();
        dto.setId(1L);
        dto.setIdCountry(100L);
        dto.setIdCity(200L);
        dto.setIdRegion(300L);
        dto.setAddress("Test Address");
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        when(countryService.getById(100L)).thenReturn(Optional.of(new Country()));
        when(cityService.getById(200L)).thenReturn(Optional.of(new City()));
        when(regionService.getById(300L)).thenReturn(Optional.of(new Region()));
        when(userRepository.save(any(User.class))).thenReturn(user);
        userService.save(dto);
        assertNotNull(user.getCountry());
        assertNotNull(user.getCity());
        assertNotNull(user.getRegion());
        assertEquals("Test Address", user.getAddress());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testSaveAddressDtoIndividual_WhenIdIsNull() {
        userService.save(AddressDtoIndividual.builder().build());
    }

    @Test
    void testSaveAddressDtoLegal() {
        AddressDtoLegal dto = AddressDtoLegal.builder().build();
        dto.setId(1L);
        dto.setIdCountry(100L);
        dto.setIdCity(200L);
        dto.setIdRegion(300L);
        dto.setIdCountryLegal(101L);
        dto.setIdCityLegal(201L);
        dto.setIdRegionLegal(301L);
        dto.setAddress("Main Address");
        dto.setAddressLegal("Legal Address");
        dto.setOkpo("12345678");

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(countryService.getById(100L)).thenReturn(Optional.of(new Country()));
        when(cityService.getById(200L)).thenReturn(Optional.of(new City()));
        when(regionService.getById(300L)).thenReturn(Optional.of(new Region()));
        when(countryService.getById(101L)).thenReturn(Optional.of(new Country()));
        when(cityService.getById(201L)).thenReturn(Optional.of(new City()));
        when(regionService.getById(301L)).thenReturn(Optional.of(new Region()));
        when(userRepository.save(any(User.class))).thenReturn(user);
        userService.save(dto);

        assertNotNull(user.getCountry());
        assertNotNull(user.getCity());
        assertNotNull(user.getRegion());
        assertNotNull(user.getCountryAdditional());
        assertNotNull(user.getCityAdditional());
        assertNotNull(user.getRegionAdditional());
        assertEquals("Main Address", user.getAddress());
        assertEquals("Legal Address", user.getAddressAdditional());
        assertEquals("12345678", user.getPaymentDetails());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testSaveAddressDtoLegal_WhenIdIsNull() {
        userService.save(AddressDtoLegal.builder().build());
    }
}