package org.example.black_sea_walnut.controller;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.service.RegionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class RegionController {
    private final RegionService regionService;

    @GetMapping("/regions/get")
    public ResponseEntity<?> getRegions() {
        return new ResponseEntity<>(regionService.getAll(), HttpStatus.OK);
    }
}
