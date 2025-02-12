package org.example.black_sea_walnut.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.contact.ContactDtoForAdd;
import org.example.black_sea_walnut.service.ContactService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class ContactController {
    private final ContactService contactService;
    @GetMapping("/contacts")
    public ModelAndView showContact(){
        ModelAndView model = new ModelAndView("admin/contacts/contacts");
        model.addObject("id",1L);
        return model;
    }
    @GetMapping("/contact/{id}")
    public ResponseEntity<ContactDtoForAdd> getContact(@PathVariable Long id){
        return new ResponseEntity<>(contactService.getByIdInDto(id), HttpStatus.OK);
    }
    @PostMapping("/contact/save")
    public ResponseEntity<?> saveContact(@Valid @ModelAttribute ContactDtoForAdd dto,
                                         BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

            return ResponseEntity
                    .status(HttpStatus.valueOf(400))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errors);
        }
        contactService.save(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @ModelAttribute("isActiveContacts")
    public boolean toActiveSidebarButton(){
        return true;
    }
}
