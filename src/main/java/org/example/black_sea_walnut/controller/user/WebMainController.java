package org.example.black_sea_walnut.controller.user;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.admin.contact.ContactDtoForAdd;
import org.example.black_sea_walnut.dto.admin.new_.NewRequestForAdd;
import org.example.black_sea_walnut.dto.admin.nut.NutResponseForAdd;
import org.example.black_sea_walnut.dto.admin.pages.main.response.*;
import org.example.black_sea_walnut.dto.web.MainResponseForView;
import org.example.black_sea_walnut.dto.web.ProductResponseForViewInTable;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.enums.PageType;
import org.example.black_sea_walnut.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/web")
@RequiredArgsConstructor
public class WebMainController {
    private final ProductService productService;
    private final HistoryMainService historyMainService;
    private final NutService nutService;
    private final NewService newService;
    private final ContactService contactService;

    @GetMapping("/main")
    public ModelAndView viewMainPage() {
        return new ModelAndView("web/main/main");
    }

    @GetMapping("/main/data")
    public ResponseEntity<MainResponseForView> getDataForMainPage(@RequestParam("lang") String lang) {
        List<ProductResponseForViewInTable> response = productService.getRandomProductsBySize(5, LanguageCode.fromString(lang));
        BlockResponseForAddInMain banner = historyMainService.getByPageTypeInResponseMainBlock(PageType.main_banner);
        ProductionResponseForAddInMain production = historyMainService.getByPageTypeInResponseProductionBlock(PageType.main_production);
        FactoryBlockResponseForAddInMain factory = historyMainService.getByPageTypeInResponseFactoryBlock(PageType.main_factory_about);
        NumberBlockResponseForAddInMain numbers = historyMainService.getByPageTypeInResponseNumberBlock(PageType.main_numbers);
        AimBlockResponseForAddInMain aim = historyMainService.getByPageTypeInResponseAimBlock(PageType.main_aim);
        EcoProductionResponseForAddInMain eco = historyMainService.getByPageTypeInResponseEcoProductionBlock(PageType.main_eco_production);
        List<NutResponseForAdd> nuts = nutService.getAllInResponseForAdd();
        List<NewRequestForAdd> news = newService.getAllInResponseForAdd();
        ContactDtoForAdd contacts = contactService.getByIdInDto(1l);
        return new ResponseEntity<>(
                MainResponseForView.builder()
                        .products(response)
                        .banner(banner)
                        .production(production)
                        .number(numbers)
                        .factory(factory)
                        .aim(aim)
                        .eco(eco)
                        .nuts(nuts)
                        .news(news)
                        .contacts(contacts)
                        .build(), HttpStatus.OK);
    }
}
