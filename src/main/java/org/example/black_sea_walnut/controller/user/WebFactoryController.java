package org.example.black_sea_walnut.controller.user;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.contact.ContactDtoForAdd;
import org.example.black_sea_walnut.dto.gallery.GalleryResponseForAdd;
import org.example.black_sea_walnut.dto.new_.NewRequestForAdd;
import org.example.black_sea_walnut.dto.pages.factory.response.BlockResponseForAdd;
import org.example.black_sea_walnut.dto.pages.factory.response.FactoryBannerBlockResponseForAdd;
import org.example.black_sea_walnut.dto.pages.main.response.EcoProductionResponseForAddInMain;
import org.example.black_sea_walnut.dto.pages.main.response.NumberBlockResponseForAddInMain;
import org.example.black_sea_walnut.dto.web.FactoryResponseForView;
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
@RequiredArgsConstructor
@RequestMapping("/web")
public class WebFactoryController {
    private final HistoryFactoryService historyFactoryService;
    private final HistoryMainService historyMainService;
    private final GalleryService galleryService;
    private final NewService newService;
    private final ContactService contactService;

    @GetMapping("/factory")
    public ModelAndView viewFactoryPage() {
        return new ModelAndView("web/factory/factory");
    }

    @GetMapping("/factory/data")
    public ResponseEntity<?> getDataForFactoryPage(@RequestParam("lang") String lang) {
        BlockResponseForAdd bock = historyFactoryService.getByPageTypeInResponseBlock(PageType.factory_block2);
        FactoryBannerBlockResponseForAdd banner = historyFactoryService.getByPageTypeInResponseBannerBlock(PageType.factory_banner);
        NumberBlockResponseForAddInMain numbers = historyMainService.getByPageTypeInResponseNumberBlock(PageType.main_numbers);
        EcoProductionResponseForAddInMain ecology = historyMainService.getByPageTypeInResponseEcoProductionBlock(PageType.main_eco_production);
        ContactDtoForAdd contacts = contactService.getByIdInDto(1l);
        List<NewRequestForAdd> news = newService.getAllInResponseForAdd();
        List<GalleryResponseForAdd> gallery = galleryService.getAllInResponseByLanguageCode(LanguageCode.fromString(lang));
        return new ResponseEntity<>(FactoryResponseForView
                .builder()
                .banner(banner)
                .block(bock)
                .contacts(contacts)
                .news(news)
                .gallery(gallery)
                .ecology(ecology)
                .numbers(numbers)
                .build(), HttpStatus.OK);
    }
}
