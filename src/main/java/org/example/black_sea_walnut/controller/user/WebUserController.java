package org.example.black_sea_walnut.controller.user;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.contact.ContactDtoForAdd;
import org.example.black_sea_walnut.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@RequestMapping("/web")
public class WebUserController {
    private final ContactService contactService;
    private final CountryService countryService;
    @GetMapping("/account")
    public ModelAndView viewPersonalPage() {
        return new ModelAndView("web/personal/account");
    }


    @GetMapping("/info-fiz/load")
    public ModelAndView loadInfoFiz() {
        ModelAndView model = new ModelAndView("web/personal/load/info-fiz");
        return model;
    }

    @GetMapping("/password/load")
    public ModelAndView loadPassword() {
        return new ModelAndView("web/personal/load/password");
    }

    @GetMapping("/contacts/get")
    public ResponseEntity<ContactDtoForAdd> getContacts() {
        return new ResponseEntity<>(contactService.getByIdInDto(1L), HttpStatus.OK);
    }

    @GetMapping("/address-fiz/load")
    public ModelAndView loadAddressFiz(){
        ModelAndView model = new ModelAndView("web/personal/load/address-fiz");
        model.addObject("countries",countryService.getAll());
        return model;
    }

    @GetMapping("/address-ur/load")
    public ModelAndView loadAddressUr(){
        ModelAndView model = new ModelAndView("web/personal/load/address-ur");
        model.addObject("countries",countryService.getAll());
        return model;
    }
}
