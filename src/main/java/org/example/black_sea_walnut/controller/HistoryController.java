package org.example.black_sea_walnut.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.pages.catalog.PageCatalogRequestForAdd;
import org.example.black_sea_walnut.dto.pages.catalog.PageCatalogResponseForAdd;
import org.example.black_sea_walnut.dto.pages.catalog.response.BannerBlockResponseForAdd;
import org.example.black_sea_walnut.dto.pages.catalog.response.EcologicallyBlockResponseForAdd;
import org.example.black_sea_walnut.dto.pages.factory.PageFactoryRequestForAdd;
import org.example.black_sea_walnut.dto.pages.factory.PageFactoryResponseForAdd;
import org.example.black_sea_walnut.dto.pages.factory.response.BlockResponseForAdd;
import org.example.black_sea_walnut.dto.pages.factory.response.FactoryBannerBlockResponseForAdd;
import org.example.black_sea_walnut.dto.pages.main.PageMainRequestForAdd;
import org.example.black_sea_walnut.dto.pages.main.PageMainResponseForAdd;
import org.example.black_sea_walnut.dto.pages.main.response.*;
import org.example.black_sea_walnut.enums.PageType;
import org.example.black_sea_walnut.service.HistoryCatalogService;
import org.example.black_sea_walnut.service.HistoryFactoryService;
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
    private final HistoryCatalogService catalogService;
    private final HistoryFactoryService factoryService;

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

    @GetMapping("/page/catalog")
    public ModelAndView viewCatalogPage() {
        return new ModelAndView("admin/page/catalog");
    }
    @GetMapping("/page/catalog/data")
    public ResponseEntity<PageCatalogResponseForAdd> getDataForPageCatalog() {
        BannerBlockResponseForAdd bannerResponse = catalogService.getByPageTypeInResponseBannerBlock(PageType.catalog_banner);
        EcologicallyBlockResponseForAdd ecologicallyResponse = catalogService.getByPageTypeInResponseEcologicallyBlock(PageType.catalog_ecologically_pure_walnut);
        PageCatalogResponseForAdd response = PageCatalogResponseForAdd.builder().bannerBlockResponseForAdd(bannerResponse).ecologicallyBlockResponseForAdd(ecologicallyResponse).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/page/catalog/save")
    public ResponseEntity<?> savePageCatalogBanner(@Valid PageCatalogRequestForAdd dto,
                                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

            return ResponseEntity
                    .status(HttpStatus.valueOf(400))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errors);
        }
        catalogService.saveHistoryBannerBlock(dto.getRequestBannerForAdd());
        catalogService.saveHistoryEcologicallyBlock(dto.getRequestEcologicallyForAdd());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/page/factory")
    public ModelAndView viewFactoryPage() {
        return new ModelAndView("admin/page/factory");
    }
    @GetMapping("/page/factory/data")
    public ResponseEntity<PageFactoryResponseForAdd> getDataForPageFactory() {
        FactoryBannerBlockResponseForAdd bannerResponse = factoryService.getByPageTypeInResponseBannerBlock(PageType.factory_banner);
        BlockResponseForAdd blockResponse = factoryService.getByPageTypeInResponseBlock(PageType.factory_block2);
        PageFactoryResponseForAdd response = PageFactoryResponseForAdd.builder().responseFactoryBannerForAdd(bannerResponse).responseFactoryBlockForAdd(blockResponse).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/page/factory/save")
    public ResponseEntity<?> savePageFactoryBanner(@Valid PageFactoryRequestForAdd dto,
                                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

            return ResponseEntity
                    .status(HttpStatus.valueOf(400))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errors);
        }
        factoryService.saveHistoryBannerBlock(dto.getRequestFactoryBannerForAdd());
        factoryService.saveHistoryBlock(dto.getRequestFactoryBlockForAdd());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
