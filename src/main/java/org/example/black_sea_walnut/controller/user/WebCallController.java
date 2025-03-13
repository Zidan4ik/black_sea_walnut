package org.example.black_sea_walnut.controller.user;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.calls.CallResponseForView;
import org.example.black_sea_walnut.service.CallService;
import org.example.black_sea_walnut.service.UserService;
import org.example.black_sea_walnut.validator.groupValidation.OrderedEmailValidation;
import org.example.black_sea_walnut.validator.groupValidation.OrderedPhoneValidation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/web")
public class WebCallController {
    private final CallService callService;
    private final UserService userService;

    @PostMapping("/call/save")
    public ResponseEntity<?> saveCall(@Validated({OrderedEmailValidation.class, OrderedPhoneValidation.class, Default.class})
                                                        @ModelAttribute CallResponseForView dto,
                                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity
                    .status(HttpStatus.valueOf(400))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errors);
        }
        callService.save(dto);
        return ResponseEntity.ok().build();
    }
}
