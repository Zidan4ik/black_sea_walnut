package org.example.black_sea_walnut.controller;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.transaction.ResponseTransactionForView;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.service.TransactionsService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class TransactionController {
    private final TransactionsService transactionsService;
    @GetMapping("/transactions")
    private ModelAndView viewTransactions(){
        return new ModelAndView("/admin/transactions/table");
    }
    @GetMapping("/transactions/table/load")
    public ModelAndView loadTable(@ModelAttribute ResponseTransactionForView responseTransactionForView,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "5") int size,
                                  @RequestParam String languageCode) {
        ModelAndView model = new ModelAndView("admin/fragments/table-transactions");
        PageRequest pageable = PageRequest.of(page, size);
        PageResponse<ResponseTransactionForView> pageResponse = transactionsService.getAll(responseTransactionForView, pageable, LanguageCode.fromString(languageCode));
        model.addObject("data", pageResponse.getContent());
        return model;
    }
    @GetMapping("/transactions/pagination/load")
    public ModelAndView loadPagination(@ModelAttribute ResponseTransactionForView responseTransactionForView,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "5") int size,
                                       @RequestParam String languageCode) {
        ModelAndView model = new ModelAndView("admin/fragments/pagination");
        PageRequest pageable = PageRequest.of(page, size);
        PageResponse<ResponseTransactionForView> pageResponse = transactionsService.getAll(responseTransactionForView, pageable, LanguageCode.fromString(languageCode));
        model.addObject("pageData", pageResponse.getMetadata());
        return model;
    }

    @ModelAttribute("isActiveTransactions")
    public boolean toActiveApplications() {
        return true;
    }
}
