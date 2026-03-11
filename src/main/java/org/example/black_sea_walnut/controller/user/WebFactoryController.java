package org.example.black_sea_walnut.controller.user;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.admin.contact.ContactDtoForAdd;
import org.example.black_sea_walnut.dto.admin.gallery.GalleryResponseForAdd;
import org.example.black_sea_walnut.dto.admin.new_.NewRequestForAdd;
import org.example.black_sea_walnut.dto.admin.pages.factory.response.BlockResponseForAdd;
import org.example.black_sea_walnut.dto.admin.pages.factory.response.FactoryBannerBlockResponseForAdd;
import org.example.black_sea_walnut.dto.admin.pages.main.response.EcoProductionResponseForAddInMain;
import org.example.black_sea_walnut.dto.admin.pages.main.response.NumberBlockResponseForAddInMain;
import org.example.black_sea_walnut.dto.web.FactoryResponseForView;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.enums.PageType;
import org.example.black_sea_walnut.mapper.ContactMapper;
import org.example.black_sea_walnut.mapper.NewMapper;
import org.example.black_sea_walnut.mapper.pages.HistoryFactoryMapper;
import org.example.black_sea_walnut.mapper.pages.HistoryMainMapper;
import org.example.black_sea_walnut.service.*;
import org.example.black_sea_walnut.service.contact.ContactService;
import org.example.black_sea_walnut.service.history.HistoryService;
import org.example.black_sea_walnut.service.news.NewService;
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
    private final HistoryService historyService;
    private final HistoryMainMapper mainMapper;
    private final HistoryFactoryMapper factoryMapper;
    private final GalleryService galleryService;
    private final NewService newService;
    private final ContactService contactService;
    private final ContactMapper contactMapper;
    private final NewMapper newMapper;

    @GetMapping("/factory")
    public ModelAndView viewFactoryPage() {
        return new ModelAndView("web/factory/factory");
    }

    @GetMapping("/factory/data")
    public ResponseEntity<?> getDataForFactoryPage(@RequestParam("lang") String lang) {
        BlockResponseForAdd bock = historyService.getResponseByPageType(PageType.factory_block2,factoryMapper::toResponseBlockForAdd);
        FactoryBannerBlockResponseForAdd banner = historyService.getResponseByPageType(PageType.factory_banner,factoryMapper::toResponseBannerBlockForAdd);
        NumberBlockResponseForAddInMain numbers = historyService.getResponseByPageType(PageType.main_numbers,mainMapper::toResponseNumberBlockForAdd);
        EcoProductionResponseForAddInMain ecology = historyService.getResponseByPageType(PageType.main_eco_production,mainMapper::toResponseEcoProductionBLockForAdd);
        ContactDtoForAdd contacts = contactService.getDtoResponseById(1L,contactMapper::toDtoContactForAdd);
        List<NewRequestForAdd> news = newService.getAllInResponseByActive(true,newMapper::toDtoAdd);
        List<GalleryResponseForAdd> gallery = galleryService.getAllInResponseByLanguageCodeByActive(LanguageCode.fromString(lang),true);
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
