package org.example.black_sea_walnut.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.admin.discount.DiscountRequestForAdd;
import org.example.black_sea_walnut.dto.admin.discount.DiscountResponseForAdd;
import org.example.black_sea_walnut.dto.admin.discount.DiscountResponseForView;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.service.DiscountService;
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
public class DiscountController {
    private final DiscountService discountService;

    @PostMapping("/discount/save")
    public ResponseEntity<?> saveTaste(@Valid DiscountRequestForAdd dto,
                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity
                    .status(HttpStatus.valueOf(400))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errors);
        }
        discountService.save(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/discount/{id}")
    public ResponseEntity<DiscountResponseForAdd> getTaste(@PathVariable Long id) {
        return new ResponseEntity<>(discountService.getByIdInResponseForAdd(id), HttpStatus.OK);
    }

    @GetMapping("/discounts")
    public ResponseEntity<Set<DiscountResponseForView>> getTastes(@RequestParam String languageCode) {
        Set<DiscountResponseForView> discounts = discountService.getAllByLanguageCodeInDTO(LanguageCode.fromString(languageCode));
        return new ResponseEntity<>(discounts, HttpStatus.OK);
    }
    @DeleteMapping("/discount/{id}/delete")
    public ResponseEntity<String> deleteTasteById(@PathVariable Long id) {
        discountService.deleteCommonById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/discount/delete")
    public ResponseEntity<String> deleteTasteById2(@RequestBody Long id) {
        discountService.deleteCommonById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
