package org.example.black_sea_walnut.controller.user;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.web.TransactionResponseForAccount;
import org.example.black_sea_walnut.service.TransactionsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@RequestMapping("/web")
public class WebTransactionController {
    private final TransactionsService transactionsService;

    @GetMapping("/transaction/{id}")
    public ModelAndView viewTransactionById(@PathVariable Long id) {
        ModelAndView model = new ModelAndView("web/transaction/transaction");
        model.addObject("transaction", transactionsService.getByIdInResponseForAccount(id));
        return model;
    }
}
