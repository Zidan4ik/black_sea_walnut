package org.example.black_sea_walnut.controller.user;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.contact.ContactDtoForAdd;
import org.example.black_sea_walnut.dto.web.user.*;
import org.example.black_sea_walnut.dto.web.user.address.AddressPageResponseIndividual;
import org.example.black_sea_walnut.entity.User;
import org.example.black_sea_walnut.enums.RegisterType;
import org.example.black_sea_walnut.mapper.UserMapper;
import org.example.black_sea_walnut.service.*;
import org.example.black_sea_walnut.validator.groupValidation.OrderedEmailValidation;
import org.example.black_sea_walnut.validator.groupValidation.OrderedPhoneValidation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/web")
public class WebUserController {
    private final ContactService contactService;
    private final CountryService countryService;
    private final UserService userService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/account")
    public ModelAndView viewPersonalPage() {
        return new ModelAndView("web/personal/account");
    }


    @GetMapping("/info-user/load")
    public ModelAndView loadInfoFiz() {
        ModelAndView model = new ModelAndView("web/personal/load/info-ur");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            User user = userService.getByEmail(userDetails.getUsername()).orElseThrow(
                    (() -> new EntityNotFoundException("User with email: " + userDetails.getUsername() + " was not found!"))
            );
            model.addObject("isAuthorized", true);
            model.addObject("status", user.getRegisterType());
            if (user.getRegisterType().equals(RegisterType.individual)) {
                model.setViewName("web/personal/load/info-fiz");
                return model;
            } else if (user.getRegisterType().equals(RegisterType.legal)) {
                model.setViewName("web/personal/load/info-ur");
                return model;
            }
        }
        model.addObject("isAuthorized", false);
        return model;
    }

    @GetMapping("/password/load")
    public ModelAndView loadPassword() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(auth -> auth.getPrincipal() instanceof UserDetails)
                .map(auth -> {
                    ModelAndView model = new ModelAndView("web/personal/load/password");
                    model.addObject("isAuthorized", true);
                    return model;
                })
                .orElseGet(() -> {
                    ModelAndView model = new ModelAndView("web/personal/load/password");
                    model.addObject("isAuthorized", false);
                    return model;
                });
    }

    @GetMapping("/contacts/get")
    public ResponseEntity<ContactDtoForAdd> getContacts() {
        return new ResponseEntity<>(contactService.getByIdInDto(1L), HttpStatus.OK);
    }

    @GetMapping("/info-user/get")
    public ResponseEntity<?> getInfoUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            User user = userService.getByEmail(userDetails.getUsername()).orElseThrow(
                    (() -> new EntityNotFoundException("User with email: " + userDetails.getUsername() + " was not found!"))
            );
            if (user.getRegisterType().equals(RegisterType.individual)) {
                return new ResponseEntity<>(userMapper.toDtoForIndividual(user), HttpStatus.OK);
            } else if (user.getRegisterType().equals(RegisterType.legal)) {
                return new ResponseEntity<>(userMapper.toDtoForLegal(user), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/info-fiz/save")
    public ResponseEntity<?> saveInfoFiz(@Validated({OrderedEmailValidation.class, OrderedPhoneValidation.class, Default.class})
                                         @ModelAttribute UserDtoIndividual dto,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity
                    .status(HttpStatus.valueOf(400))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errors);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            User user = userService.getByEmail(userDetails.getUsername()).orElseThrow(
                    (() -> new EntityNotFoundException("User with email: " + userDetails.getUsername() + " was not found!"))
            );
            dto.setId(user.getId());
        }
        userService.save(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/info-ur/save")
    public ResponseEntity<?> saveInfoUr(@Validated({OrderedEmailValidation.class, OrderedPhoneValidation.class, Default.class})
                                        @ModelAttribute UserDtoLegal dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity
                    .status(HttpStatus.valueOf(400))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errors);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            User user = userService.getByEmail(userDetails.getUsername()).orElseThrow(
                    (() -> new EntityNotFoundException("User with email: " + userDetails.getUsername() + " was not found!"))
            );
            dto.setId(user.getId());
        }
        userService.save(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/account/password-new/save")
    public ResponseEntity<?> saveNewPassword(@ModelAttribute @Valid PasswordDto dto,
                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity
                    .status(HttpStatus.valueOf(400))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errors);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            User user = userService.getByEmail(userDetails.getUsername()).orElseThrow(
                    (() -> new EntityNotFoundException("User with email: " + userDetails.getUsername() + " was not found!"))
            );
            user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
            userService.save(user);
        }
        return ResponseEntity.ok().build();

    }

    @GetMapping("/address/load")
    public ModelAndView loadAddress() {
        ModelAndView model = new ModelAndView("web/personal/load/address-ur");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            User user = userService.getByEmail(userDetails.getUsername()).orElseThrow(
                    (() -> new EntityNotFoundException("User with email: " + userDetails.getUsername() + " was not found!"))
            );
            model.addObject("isAuthorized", true);
            model.addObject("status", user.getRegisterType());
            if (user.getRegisterType().equals(RegisterType.individual)) {
                model.setViewName("web/personal/load/address-fiz");
                return model;
            } else if (user.getRegisterType().equals(RegisterType.legal)) {
                model.setViewName("web/personal/load/address-ur");
                return model;
            }
        }
        model.addObject("isAuthorized", false);
        return model;
    }

    @GetMapping("/address/get")
    public ResponseEntity<?> getAddress() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            User user = userService.getByEmail(userDetails.getUsername()).orElseThrow(
                    (() -> new EntityNotFoundException("User with email: " + userDetails.getUsername() + " was not found!"))
            );
            if (user.getRegisterType().equals(RegisterType.individual)) {
                return new ResponseEntity<>(userMapper.toResponseForAddressIndividual(user), HttpStatus.OK);
            } else if (user.getRegisterType().equals(RegisterType.legal)) {
                return new ResponseEntity<>(userMapper.toResponseForAddressLegal(user), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/address-fiz/save")
    public ResponseEntity<?> saveAddressFiz(@ModelAttribute AddressDtoIndividual dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity
                    .status(HttpStatus.valueOf(400))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errors);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            User user = userService.getByEmail(userDetails.getUsername()).orElseThrow(
                    (() -> new EntityNotFoundException("User with email: " + userDetails.getUsername() + " was not found!"))
            );
            dto.setId(user.getId());
        }
        userService.save(dto);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/address-ur/save")
    public ResponseEntity<?> saveAddressUr(@ModelAttribute AddressDtoLegal dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity
                    .status(HttpStatus.valueOf(400))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errors);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            User user = userService.getByEmail(userDetails.getUsername()).orElseThrow(
                    (() -> new EntityNotFoundException("User with email: " + userDetails.getUsername() + " was not found!"))
            );
            dto.setId(user.getId());
        }
        userService.save(dto);
        return ResponseEntity.ok().build();
    }
}