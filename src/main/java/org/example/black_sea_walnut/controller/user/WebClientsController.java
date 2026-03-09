package org.example.black_sea_walnut.controller.user;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.admin.contact.ContactDtoForAdd;
import org.example.black_sea_walnut.dto.admin.pages.clients.response.ClientBannerResponseForAdd;
import org.example.black_sea_walnut.dto.admin.pages.clients.response.ClientCategoryResponseForAdd;
import org.example.black_sea_walnut.dto.admin.pages.clients.response.ClientEcoProductionResponseForAdd;
import org.example.black_sea_walnut.dto.admin.pages.main.response.NumberBlockResponseForAddInMain;
import org.example.black_sea_walnut.dto.web.ClientsResponseForView;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.enums.PageType;
import org.example.black_sea_walnut.mapper.ContactMapper;
import org.example.black_sea_walnut.mapper.pages.HistoryClientsMapper;
import org.example.black_sea_walnut.mapper.pages.HistoryMainMapper;
import org.example.black_sea_walnut.service.contact.ContactService;
import org.example.black_sea_walnut.service.history.HistoryService;
import org.example.black_sea_walnut.service.history.client.HistoryClientService;
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
public class WebClientsController {
    private final HistoryClientService historyClientService;
    private final HistoryService historyService;
    private final HistoryMainMapper mainMapper;
    private final HistoryClientsMapper historyClientsMapper;
    private final ContactService contactService;
    private final ContactMapper contactMapper;

    @GetMapping("/clients")
    public ModelAndView viewClientsPage() {
        return new ModelAndView("web/clients/clients");
    }

    @GetMapping("/clients/data")
    public ResponseEntity<ClientsResponseForView> getClientsPageData(@RequestParam(name = "lang") LanguageCode code) {
        ClientBannerResponseForAdd banner = historyService.getResponseByPageType(PageType.clients_banner,historyClientsMapper::toResponseBannerBlockForAdd);
        ClientEcoProductionResponseForAdd eco = historyService.getResponseByPageType(PageType.clients_eco_production, historyClientsMapper::toResponseEcoProductionBlockForAdd);
        List<ClientCategoryResponseForAdd> categories = historyClientService.getAllInResponseCategoryBlock();
        ContactDtoForAdd contacts = contactService.getDtoResponseById(1L,contactMapper::toDtoContactForAdd);
        NumberBlockResponseForAddInMain numbers = historyService.getResponseByPageType(PageType.main_numbers,mainMapper::toResponseNumberBlockForAdd);

        return new ResponseEntity<>(ClientsResponseForView.builder()
                .banner(banner)
                .eco(eco)
                .contacts(contacts)
                .numbers(numbers)
                .categories(categories)
                .build(), HttpStatus.OK);
    }
}
