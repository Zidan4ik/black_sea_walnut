package org.example.black_sea_walnut.controller;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.service.CityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class CityController {
    private final CityService cityService;

    @GetMapping("/cities/get")
    public ResponseEntity<?> getCities() {
        return new ResponseEntity<>(cityService.getAll(), HttpStatus.OK);
    }
}
