package org.example.black_sea_walnut.controller.admin;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.entity.City;
import org.example.black_sea_walnut.service.CityService;
import org.example.black_sea_walnut.service.RegionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminCityController {
    private final CityService cityService;
    private final RegionService regionService;

    @GetMapping("/cities/get")
    public ResponseEntity<?> getCities() {
        return new ResponseEntity<>(cityService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/cities")
    public ResponseEntity<List<City>> getCitiesByRegion(@RequestParam("regionId") Long id) {
        return new ResponseEntity<>(cityService.getByRegion(regionService.getById(id).orElseThrow(
                () -> new EntityNotFoundException("Region with id:" + id + " was not found!")
        )), HttpStatus.OK);
    }
}
