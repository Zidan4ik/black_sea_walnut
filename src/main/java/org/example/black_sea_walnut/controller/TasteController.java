package org.example.black_sea_walnut.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.taste.TasteRequestForAdd;
import org.example.black_sea_walnut.dto.taste.TasteResponseForAdd;
import org.example.black_sea_walnut.dto.taste.TasteResponseForView;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.service.TasteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class TasteController {
    private final TasteService tasteService;

    @PostMapping("/taste/save")
    public ResponseEntity<?> saveTaste(@Valid TasteRequestForAdd dto,
                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

            return ResponseEntity
                    .status(HttpStatus.valueOf(400))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errors);
        }
        tasteService.save(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/taste/{id}")
    public ResponseEntity<TasteResponseForAdd> getTaste(@PathVariable Long id) {
        TasteResponseForAdd dto = tasteService.getByIdInResponseForAdd(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/tastes")
    public ResponseEntity<Set<TasteResponseForView>> getTastes(@RequestParam String languageCode) {
        Set<TasteResponseForView> tastes = tasteService.getAllByLanguageCodeInDTO(LanguageCode.fromString(languageCode));
        return new ResponseEntity<>(tastes, HttpStatus.OK);
    }
}
