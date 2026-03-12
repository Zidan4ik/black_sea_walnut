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
import org.example.black_sea_walnut.mapper.ContactMapper;
import org.example.black_sea_walnut.mapper.NewMapper;
import org.example.black_sea_walnut.mapper.pages.HistoryMainMapper;
import org.example.black_sea_walnut.service.contact.ContactService;
import org.example.black_sea_walnut.service.history.HistoryService;
import org.example.black_sea_walnut.service.news.NewService;
import org.example.black_sea_walnut.service.nut.NutService;
import org.example.black_sea_walnut.service.product.ProductService;
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
    private final NutService nutService;
    private final NewService newService;
    private final ContactService contactService;
    private final HistoryService historyService;
    private final HistoryMainMapper mainMapper;
    private final ContactMapper contactMapper;
    private final NewMapper newMapper;


    @GetMapping("/main")
    public ModelAndView viewMainPage() {
        return new ModelAndView("web/main/main");
    }

    @GetMapping("/main/data")
    public ResponseEntity<MainResponseForView> getDataForMainPage(@RequestParam("lang") String lang) {
        List<ProductResponseForViewInTable> response = productService.getRandomProductsBySizeForDto(5, LanguageCode.fromString(lang));
        BlockResponseForAddInMain banner = historyService.getResponseByPageType(PageType.main_banner,mainMapper::toResponseMainBlockForAdd);
        ProductionResponseForAddInMain production = historyService.getResponseByPageType(PageType.main_production,mainMapper::toResponseProductionBlockForAdd);
        FactoryBlockResponseForAddInMain factory = historyService.getResponseByPageType(PageType.main_factory_about,mainMapper::toResponseFactoryBlockForAdd);
        NumberBlockResponseForAddInMain numbers = historyService.getResponseByPageType(PageType.main_numbers,mainMapper::toResponseNumberBlockForAdd);
        AimBlockResponseForAddInMain aim = historyService.getResponseByPageType(PageType.main_aim,mainMapper::toResponseAimBlockForAdd);
        EcoProductionResponseForAddInMain eco = historyService.getResponseByPageType(PageType.main_eco_production,mainMapper::toResponseEcoProductionBLockForAdd);
        List<NutResponseForAdd> nuts = nutService.getAllActiveInResponseForAdd();
        List<NewRequestForAdd> news = newService.getAllInResponseByActive(true, newMapper::toDtoAdd);
        ContactDtoForAdd contacts = contactService.getDtoResponseById(1L,contactMapper::toDtoContactForAdd);
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
