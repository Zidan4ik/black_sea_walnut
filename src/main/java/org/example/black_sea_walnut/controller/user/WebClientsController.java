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
import org.example.black_sea_walnut.service.ContactService;
import org.example.black_sea_walnut.service.HistoryClientService;
import org.example.black_sea_walnut.service.HistoryMainService;
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
    private final HistoryMainService historyMainService;
    private final ContactService contactService;

    @GetMapping("/clients")
    public ModelAndView viewClientsPage() {
        return new ModelAndView("web/clients/clients");
    }

    @GetMapping("/clients/data")
    public ResponseEntity<ClientsResponseForView> getClientsPageData(@RequestParam(name = "lang") LanguageCode code) {
        ClientBannerResponseForAdd banner = historyClientService.getByPageTypeInResponseBannerBlock(PageType.clients_banner);
        ClientEcoProductionResponseForAdd eco = historyClientService.getByPageTypeInResponseEcoProductionBlock(PageType.clients_eco_production);
        List<ClientCategoryResponseForAdd> categories = historyClientService.getAllInResponseCategoryBlock();
        ContactDtoForAdd contacts = contactService.getByIdInDto(1L);
        NumberBlockResponseForAddInMain numbers = historyMainService.getByPageTypeInResponseNumberBlock(PageType.main_numbers);

        return new ResponseEntity<>(ClientsResponseForView.builder()
                .banner(banner)
                .eco(eco)
                .contacts(contacts)
                .numbers(numbers)
                .categories(categories)
                .build(), HttpStatus.OK);
    }
}
