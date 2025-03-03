package org.example.black_sea_walnut.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.contact.ContactDtoForAdd;
import org.example.black_sea_walnut.dto.web.RegistrationResponseForView;
import org.example.black_sea_walnut.dto.web.security.UserRequestForRegistration;
import org.example.black_sea_walnut.entity.Country;
import org.example.black_sea_walnut.enums.RegisterType;
import org.example.black_sea_walnut.enums.UserStatus;
import org.example.black_sea_walnut.service.ContactService;
import org.example.black_sea_walnut.service.CountryService;
import org.example.black_sea_walnut.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("")
public class AuthorizationController {
    private final UserService userService;
    private final CountryService countryService;
    private final ContactService contactService;


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

    @GetMapping("/password-recovery")
    public ModelAndView viewPasswordRecovery() {
        return new ModelAndView("web/password-recovery");
    }

    @PostMapping("/user-fiz/save")
    public ResponseEntity<?> saveUser(@Valid UserRequestForRegistration user,
                                      BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
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
}