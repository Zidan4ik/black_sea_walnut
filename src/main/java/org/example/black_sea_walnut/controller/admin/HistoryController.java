package org.example.black_sea_walnut.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.admin.pages.catalog.PageCatalogRequestForAdd;
import org.example.black_sea_walnut.dto.admin.pages.catalog.PageCatalogResponseForAdd;
import org.example.black_sea_walnut.dto.admin.pages.catalog.response.BannerBlockResponseForAdd;
import org.example.black_sea_walnut.dto.admin.pages.catalog.response.EcologicallyBlockResponseForAdd;
import org.example.black_sea_walnut.dto.admin.pages.clients.PageClientResponseForAdd;
import org.example.black_sea_walnut.dto.admin.pages.clients.PageClientsRequestForAdd;
import org.example.black_sea_walnut.dto.admin.pages.clients.response.ClientBannerResponseForAdd;
import org.example.black_sea_walnut.dto.admin.pages.clients.response.ClientCategoryResponseForAdd;
import org.example.black_sea_walnut.dto.admin.pages.clients.response.ClientEcoProductionResponseForAdd;
import org.example.black_sea_walnut.dto.admin.pages.factory.PageFactoryRequestForAdd;
import org.example.black_sea_walnut.dto.admin.pages.factory.PageFactoryResponseForAdd;
import org.example.black_sea_walnut.dto.admin.pages.factory.response.BlockResponseForAdd;
import org.example.black_sea_walnut.dto.admin.pages.factory.response.FactoryBannerBlockResponseForAdd;
import org.example.black_sea_walnut.dto.admin.pages.main.PageMainRequestForAdd;
import org.example.black_sea_walnut.dto.admin.pages.main.PageMainResponseForAdd;
import org.example.black_sea_walnut.dto.admin.pages.main.response.*;
import org.example.black_sea_walnut.enums.PageType;
import org.example.black_sea_walnut.mapper.pages.HistoryCatalogMapper;
import org.example.black_sea_walnut.mapper.pages.HistoryClientsMapper;
import org.example.black_sea_walnut.mapper.pages.HistoryFactoryMapper;
import org.example.black_sea_walnut.mapper.pages.HistoryMainMapper;
import org.example.black_sea_walnut.service.ClientCategoryService;
import org.example.black_sea_walnut.service.history.HistoryService;
import org.example.black_sea_walnut.service.history.client.HistoryClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class HistoryController {
    private final HistoryService historyService;
    private final HistoryCatalogMapper catalogMapper;
    private final HistoryMainMapper mainMapper;
    private final HistoryFactoryMapper factoryMapper;
    private final HistoryClientService clientService;
    private final HistoryClientsMapper clientsMapper;
    private final ClientCategoryService clientCategoryService;

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
        BlockResponseForAddInMain mainBlock = historyService.getResponseByPageType(PageType.main_banner,mainMapper::toResponseMainBlockForAdd);
        ProductionResponseForAddInMain productionBlock = historyService.getResponseByPageType(PageType.main_production,mainMapper::toResponseProductionBlockForAdd);
        FactoryBlockResponseForAddInMain factoryAboutBlock = historyService.getResponseByPageType(PageType.main_factory_about,mainMapper::toResponseFactoryBlockForAdd);
        NumberBlockResponseForAddInMain numberBlock = historyService.getResponseByPageType(PageType.main_numbers,mainMapper::toResponseNumberBlockForAdd);
        AimBlockResponseForAddInMain aimBlock = historyService.getResponseByPageType(PageType.main_aim,mainMapper::toResponseAimBlockForAdd);
        EcoProductionResponseForAddInMain ecoProductionBlock = historyService.getResponseByPageType(PageType.main_eco_production,mainMapper::toResponseEcoProductionBLockForAdd);
        PageMainResponseForAdd pageMainResponse = PageMainResponseForAdd.builder()
                .responseMainBlock(mainBlock)
                .responseProductionBlock(productionBlock)
                .responseFactoryBlock(factoryAboutBlock)
                .responseNumberBlock(numberBlock)
                .responseAimBlock(aimBlock)
                .responseEcoProductionBlock(ecoProductionBlock)
                .build();
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

        historyService.saveHistory(dto.getRequestMainForAdd(),mainMapper);
        historyService.saveHistory(dto.getRequestProductionBlockForAdd(),mainMapper);
        historyService.saveHistory(dto.getRequestFactoryForAdd(),mainMapper);
        historyService.saveHistory(dto.getRequestNumberBlockForAdd(),mainMapper);
        historyService.saveHistory(dto.getRequestAimBlockForAdd(),mainMapper);
        historyService.saveHistory(dto.getRequestEcoProductionForAdd(),mainMapper);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/page/catalog")
    public ModelAndView viewCatalogPage() {
        return new ModelAndView("admin/page/catalog");
    }

    @GetMapping("/page/catalog/data")
    public ResponseEntity<PageCatalogResponseForAdd> getDataForPageCatalog() {
        BannerBlockResponseForAdd bannerResponse = historyService.getResponseByPageType(PageType.catalog_banner, catalogMapper::toResponseBannerBlockForAdd);
        EcologicallyBlockResponseForAdd ecologicallyResponse = historyService.getResponseByPageType(PageType.catalog_ecologically_pure_walnut, catalogMapper::toResponseEcologicallyBlockForAdd);
        PageCatalogResponseForAdd response = PageCatalogResponseForAdd.builder()
                .bannerBlockResponseForAdd(bannerResponse)
                .ecologicallyBlockResponseForAdd(ecologicallyResponse)
                .build();
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
        historyService.saveHistory(dto.getRequestBannerForAdd(),catalogMapper);
        historyService.saveHistory(dto.getRequestEcologicallyForAdd(), catalogMapper);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/page/factory")
    public ModelAndView viewFactoryPage() {
        return new ModelAndView("admin/page/factory");
    }

    @GetMapping("/page/factory/data")
    public ResponseEntity<PageFactoryResponseForAdd> getDataForPageFactory() {
        FactoryBannerBlockResponseForAdd bannerResponse = historyService.getResponseByPageType(PageType.factory_banner,factoryMapper::toResponseBannerBlockForAdd);
        BlockResponseForAdd blockResponse = historyService.getResponseByPageType(PageType.factory_block2,factoryMapper::toResponseBlockForAdd);
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
        historyService.saveHistory(dto.getRequestFactoryBannerForAdd(),factoryMapper);
        historyService.saveHistory(dto.getRequestFactoryBlockForAdd(),factoryMapper);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/page/clients")
    public ModelAndView viewClientsPage() {
        return new ModelAndView("admin/page/clients");
    }

    @GetMapping("/page/clients/data")
    public ResponseEntity<PageClientResponseForAdd> getDataForPageClients() {
        ClientBannerResponseForAdd banner = historyService.getResponseByPageType(PageType.clients_banner,clientsMapper::toResponseBannerBlockForAdd);
        ClientEcoProductionResponseForAdd ecoProduction = historyService.getResponseByPageType(PageType.clients_eco_production,clientsMapper::toResponseEcoProductionBlockForAdd);
        List<ClientCategoryResponseForAdd> categories = clientCategoryService.getAllInResponse();
        PageClientResponseForAdd dto = PageClientResponseForAdd.builder().responseClientBannerForAdd(banner).responseClientCategoryForAdd(categories).responseClientEcoProductionForAdd(ecoProduction).build();
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("/page/clients/save")
    public ResponseEntity<?> savePageClients(@Valid PageClientsRequestForAdd dto,
                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

            return ResponseEntity
                    .status(HttpStatus.valueOf(400))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errors);
        }
        historyService.saveHistory(dto.getRequestClientBannerForAdd(),clientsMapper);
        clientService.saveHistoryCategoryBlock(dto.getRequestClientCategoryForAdd());
        historyService.saveHistory(dto.getRequestClientEcoProductionForAdd(),clientsMapper);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/client/{id}/delete")
    public ResponseEntity<?> deleteClient(@PathVariable Long id) {
        clientService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/client/delete")
    public ResponseEntity<?> deleteClient2(@RequestBody Long id) {
        clientService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ModelAttribute("isActiveInfoPages")
    public boolean toActiveSidebarButton() {
        return true;
    }
}
