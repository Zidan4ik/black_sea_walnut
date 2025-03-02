package org.example.black_sea_walnut.controller.user;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.contact.ContactDtoForAdd;
import org.example.black_sea_walnut.dto.user.request.UserIndividualRequestForAdd;
import org.example.black_sea_walnut.dto.web.RegistrationResponseForView;
import org.example.black_sea_walnut.entity.Country;
import org.example.black_sea_walnut.entity.User;
import org.example.black_sea_walnut.enums.RegisterType;
import org.example.black_sea_walnut.enums.UserStatus;
import org.example.black_sea_walnut.service.ContactService;
import org.example.black_sea_walnut.service.CountryService;
import org.example.black_sea_walnut.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

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
    public ResponseEntity<?> saveUser(UserIndividualRequestForAdd user) {
        user.setRegistrationType(RegisterType.individual.toString());
        user.setStatus(UserStatus.isActive.toString());
        userService.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
