package org.example.black_sea_walnut.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.entity.Region;
import org.example.black_sea_walnut.service.CountryService;
import org.example.black_sea_walnut.service.RegionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class RegionController {
    private final RegionService regionService;
    private final CountryService countryService;

    @GetMapping("/regions")
    public ResponseEntity<?> getRegionsByCountry(@RequestParam("countryId") Long id) {
        List<Region> regions = countryService.getById(id)
                .map(regionService::getByCountry).orElse(Collections.emptyList());
        return new ResponseEntity<>(regions, HttpStatus.OK);
    }
}
