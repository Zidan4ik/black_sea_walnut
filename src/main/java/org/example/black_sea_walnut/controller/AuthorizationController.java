package org.example.black_sea_walnut.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.black_sea_walnut.dto.admin.contact.ContactDtoForAdd;
import org.example.black_sea_walnut.dto.web.RegistrationResponseForView;
import org.example.black_sea_walnut.dto.web.security.EmailRecoveryDto;
import org.example.black_sea_walnut.dto.web.security.UserRequestForRegistration;
import org.example.black_sea_walnut.entity.Country;
import org.example.black_sea_walnut.entity.User;
import org.example.black_sea_walnut.password.PasswordResetRequest;
import org.example.black_sea_walnut.password.PasswordResetTokenRepository;
import org.example.black_sea_walnut.password.PasswordResetTokenService;
import org.example.black_sea_walnut.password.event.RegistrationCompleteEventListener;
import org.example.black_sea_walnut.service.ContactService;
import org.example.black_sea_walnut.service.CountryService;
import org.example.black_sea_walnut.service.UserService;
import org.example.black_sea_walnut.validator.groupValidation.EmailValidGroups;
import org.example.black_sea_walnut.validator.groupValidation.OrderedEmailValidation;
import org.example.black_sea_walnut.validator.groupValidation.OrderedPasswordValidation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
@RequiredArgsConstructor
public class AuthorizationController {
    private final UserService userService;
    private final CountryService countryService;
    private final ContactService contactService;
    private final RegistrationCompleteEventListener eventListener;
    private final PasswordResetTokenService passwordResetTokenService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;


    @GetMapping("/login")
    public ModelAndView viewRegistration() {
        return new ModelAndView("web/authorize/login");
    }

    @GetMapping("/registration")
    public ModelAndView viewRegistration2() {
        ModelAndView model = new ModelAndView("web/authorize/registration");
        model.addObject("countries", countryService.getAll());
        return model;
    }

    @GetMapping("/registration/data")
    public ResponseEntity<?> getDataInRegistrationPage() {
        ContactDtoForAdd contacts = contactService.getByIdInDto(1L);
        List<Country> countries = countryService.getAll();
        return new ResponseEntity<>(RegistrationResponseForView
                .builder()
                .countries(countries)
                .contacts(contacts)
                .build(), HttpStatus.OK);
    }

    @GetMapping("/terms-of-use")
    public ModelAndView viewTermsOfUse() {
        return new ModelAndView("web/terms-of-use");
    }

    @GetMapping("/thanks")
    public ModelAndView viewThanks() {
        return new ModelAndView("web/thanks");
    }

    @PostMapping("/user/save")
    public ResponseEntity<?> saveUser(@Validated({OrderedPasswordValidation.class, Default.class})
                                      UserRequestForRegistration user,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity
                    .status(HttpStatus.valueOf(400))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errors);
        }
        userService.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/password-recovery")
    public ModelAndView viewPasswordRecovery(@RequestParam(value = "token", required = false) String passwordResetToken) {
        ModelAndView model = new ModelAndView("web/password-recovery");
        model.addObject("isPresentToken", passwordResetToken);
        return model;
    }

    @PostMapping("/password-reset-request")
    public ResponseEntity<?> resetPasswordRequest(@Validated(EmailValidGroups.class) @ModelAttribute EmailRecoveryDto dto,
                                                  BindingResult bindingResult, final HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity
                    .status(HttpStatus.valueOf(400))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errors);
        }
        String res = "";
        Optional<User> userOptional = userService.getByEmail(dto.getEmail());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordResetTokenService.isExistTokenByUser(user)) {
                passwordResetTokenService.deleteTokenByUser(user);
            }
            String passwordResetToken = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user, passwordResetToken);
            passwordResetEmailLink(user, applicationUrl(request), passwordResetToken);
            res = "We have sent a reset password link to your email. Please check!";
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@ModelAttribute @Valid PasswordResetRequest passwordResetRequest,
                                           BindingResult bindingResult,
                                           @RequestParam("token") String passwordResetToken) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity
                    .status(HttpStatus.valueOf(400))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errors);
        }
        String tokenValidationResult = userService.validatePasswordResetToken(passwordResetToken);
        if (!tokenValidationResult.equalsIgnoreCase("valid")) {
            return new ResponseEntity<>("Invalid token password reset token", HttpStatus.BAD_REQUEST);
        }
        User user = userService.findUserByPasswordToken(passwordResetToken);
        if (user != null) {
            userService.resetUserPassword(user, passwordResetRequest.getNewPassword());
            userService.deleteTokenByToken(passwordResetToken);
            return new ResponseEntity<>("Password has been reset successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid password reset token", HttpStatus.BAD_REQUEST);
    }


    @SneakyThrows
    private void passwordResetEmailLink(User user, String applicationUrl, String passwordResetToken) {
        String url = applicationUrl + "/password-recovery?token=" + passwordResetToken;
        eventListener.sendPasswordResetVerificationEmail(url, user);
    }

    public String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}