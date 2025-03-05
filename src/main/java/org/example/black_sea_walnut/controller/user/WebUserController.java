package org.example.black_sea_walnut.controller.user;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.contact.ContactDtoForAdd;
import org.example.black_sea_walnut.dto.web.OrderResponseForAccount;
import org.example.black_sea_walnut.dto.web.TransactionResponseForAccount;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.service.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/web")
public class WebUserController {
    private final OrderService orderService;
    private final TransactionsService transactionsService;
    private final ContactService contactService;
    private final CountryService countryService;
    @GetMapping("/account")
    public ModelAndView viewPersonalPage() {
        return new ModelAndView("web/personal/account");
    }

    @GetMapping("/orders/table/load")
    public ModelAndView loadTableOrders(Pageable pageable,
                                        @RequestParam String languageCode) {
        ModelAndView model = new ModelAndView("web/personal/load/orders-table");
        PageResponse<OrderResponseForAccount> pageResponse = orderService.getAll(pageable, LanguageCode.fromString(languageCode));

        Sort sort = pageable.getSort();
        List<String> sortFields = List.of("id", "dateOfOrdering", "totalPrice", "countProducts", "deliveryStatus");
        sortFields.forEach(field -> model.addObject(field, Optional.ofNullable(sort.getOrderFor(field))
                .map(Sort.Order::getDirection)
                .orElse(Sort.Direction.ASC)));
        model.addObject("data", pageResponse.getContent());

        return model;
    }

    @GetMapping("/orders/pagination/load")
    public ModelAndView loadPaginationOrders(Pageable pageable,
                                             @RequestParam String languageCode) {
        ModelAndView model = new ModelAndView("web/personal/load/pagination");
        PageResponse<OrderResponseForAccount> pageResponse = orderService.getAll(pageable, LanguageCode.fromString(languageCode));
        model.addObject("pageData", pageResponse.getMetadata());
        return model;
    }

    @GetMapping("/transactions/table/load")
    public ModelAndView loadTableTransactions(Pageable pageable,
                                              @RequestParam String languageCode) {
        ModelAndView model = new ModelAndView("web/personal/load/transactions-table");
        PageResponse<TransactionResponseForAccount> pageResponse = transactionsService.getAll(pageable, LanguageCode.fromString(languageCode));

        Sort sort = pageable.getSort();
        List<String> sortFields = List.of("date", "paymentStatus", "summa");
        sortFields.forEach(field -> model.addObject(field, Optional.ofNullable(sort.getOrderFor(field))
                .map(Sort.Order::getDirection)
                .orElse(Sort.Direction.ASC)));
        model.addObject("data", pageResponse.getContent());
        return model;
    }

    @GetMapping("/transactions/pagination/load")
    public ModelAndView loadPaginationTransactions(Pageable pageable,
                                                   @RequestParam String languageCode) {
        ModelAndView model = new ModelAndView("web/personal/load/pagination");
        PageResponse<TransactionResponseForAccount> pageResponse = transactionsService.getAll(pageable, LanguageCode.fromString(languageCode));
        model.addObject("pageData", pageResponse.getMetadata());
        return model;
    }

    @GetMapping("/info-fiz/load")
    public ModelAndView loadInfoFiz() {
        ModelAndView model = new ModelAndView("web/personal/load/info-fiz");
        return model;
    }

    @GetMapping("/password/load")
    public ModelAndView loadPassword() {
        return new ModelAndView("web/personal/load/password");
    }

    @GetMapping("/contacts/get")
    public ResponseEntity<ContactDtoForAdd> getContacts() {
        return new ResponseEntity<>(contactService.getByIdInDto(1L), HttpStatus.OK);
    }

    @GetMapping("/address-fiz/load")
    public ModelAndView loadAddressFiz(){
        ModelAndView model = new ModelAndView("web/personal/load/address-fiz");
        model.addObject("countries",countryService.getAll());
        return model;
    }

    @GetMapping("/address-ur/load")
    public ModelAndView loadAddressUr(){
        ModelAndView model = new ModelAndView("web/personal/load/address-ur");
        model.addObject("countries",countryService.getAll());
        return model;
    }
}
