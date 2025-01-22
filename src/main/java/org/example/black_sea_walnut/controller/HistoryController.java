package org.example.black_sea_walnut.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.pages.main.PageMainRequestForAdd;
import org.example.black_sea_walnut.dto.pages.main.PageMainResponseForAdd;
import org.example.black_sea_walnut.dto.pages.main.response.*;
import org.example.black_sea_walnut.enums.PageType;
import org.example.black_sea_walnut.service.HistoryMainService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class HistoryController {
    private final HistoryMainService historyMainService;

    @GetMapping("/pages")
    public ModelAndView viewPages() {
        return new ModelAndView("admin/page/pages");
    }

    @GetMapping("/page/main")
    public ModelAndView viewMainPage() {
        return new ModelAndView("admin/page/main");
    }

    @GetMapping("/page/main/data")
    public ResponseEntity<PageMainResponseForAdd> getDataForPageMain() {
        MainBlockResponseForAdd mainBlock = historyMainService.getByPageTypeInResponseMainBlock(PageType.main_banner);
        ProductionBlockResponseForAdd productionBlock = historyMainService.getByPageTypeInResponseProductionBlock(PageType.main_production);
        FactoryBlockResponseForAdd factoryAboutBlock = historyMainService.getByPageTypeInResponseFactoryBlock(PageType.main_factory_about);
        NumberBlockResponseForAdd numberBlock = historyMainService.getByPageTypeInResponseNumberBlock(PageType.main_numbers);
        AimBlockResponseForAdd aimBlock = historyMainService.getByPageTypeInResponseAimBlock(PageType.main_aim);
        EcoProductionResponseForAdd ecoProductionBlock = historyMainService.getByPageTypeInResponseEcoProductionBlock(PageType.main_eco_production);
        PageMainResponseForAdd pageMainResponse = PageMainResponseForAdd.builder().responseMainBlock(mainBlock).responseProductionBlock(productionBlock).responseFactoryBlock(factoryAboutBlock).responseNumberBlock(numberBlock).responseAimBlock(aimBlock).responseEcoProductionBlock(ecoProductionBlock).build();
        return new ResponseEntity<>(pageMainResponse, HttpStatus.OK);
    }

    @PostMapping("/page/main/save")
    public ResponseEntity<?> savePageMainBanner(@Valid PageMainRequestForAdd dto,
                                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

            return ResponseEntity
                    .status(HttpStatus.valueOf(400))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errors);
        }
        historyMainService.saveHistoryMainBlock(dto.getRequestMainForAdd());
        historyMainService.saveHistoryNumberBlock(dto.getRequestNumberBlockForAdd());
        historyMainService.saveHistoryProductionBlock(dto.getRequestProductionBlockForAdd());
        historyMainService.saveHistoryAimBlock(dto.getRequestAimBlockForAdd());
        historyMainService.saveHistoryEcoProductionBlock(dto.getRequestEcoProductionForAdd());
        historyMainService.saveHistoryFactoryBlock(dto.getRequestFactoryForAdd());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
