package org.example.black_sea_walnut.controller;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.entity.Country;
import org.example.black_sea_walnut.service.CountryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CountryController {
    private final CountryService countryService;

    @GetMapping("/countries/get")
    public ResponseEntity<List<Country>> getCountries() {
        return new ResponseEntity<>(countryService.getAll(), HttpStatus.OK);
    }
}
