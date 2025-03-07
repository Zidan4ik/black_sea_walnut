package org.example.black_sea_walnut.controller.user;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.transaction.ResponseTransactionForAccount;
import org.example.black_sea_walnut.dto.web.BasketResponseForCart;
import org.example.black_sea_walnut.dto.web.TransactionResponseForAccount;
import org.example.black_sea_walnut.entity.User;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.service.TransactionsService;
import org.example.black_sea_walnut.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/web")
public class WebTransactionController {
    private final TransactionsService transactionsService;
    private final UserService userService;

    @GetMapping("/transaction")
    public ModelAndView viewTransactionById(@RequestParam(required = false) Long id) {
        return new ModelAndView("web/transaction/transaction");
    }

    @GetMapping("/transaction/{id}")
    public ResponseEntity<ResponseTransactionForAccount> getTransactionById(@PathVariable Long id) {
        return new ResponseEntity<>(transactionsService.getByIdInResponseForAccount(id), HttpStatus.OK);
    }

    @GetMapping("/transactions/table/load")
    public ModelAndView loadTableTransactions(Pageable pageable,
                                              @RequestParam String languageCode) {
        ModelAndView model = new ModelAndView("web/personal/load/transactions-table");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            User user = userService.getByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new EntityNotFoundException("User with email: " + userDetails.getUsername() + " was not found!"));
            PageResponse<TransactionResponseForAccount> pageResponse = transactionsService.getAll(user, pageable, LanguageCode.fromString(languageCode));
            Sort sort = pageable.getSort();
            List<String> sortFields = List.of("date", "paymentStatus", "summa");
            sortFields.forEach(field -> model.addObject(field, Optional.ofNullable(sort.getOrderFor(field))
                    .map(Sort.Order::getDirection)
                    .orElse(Sort.Direction.ASC)));
            model.addObject("data", pageResponse.getContent());
        }
        return model;
    }

    @GetMapping("/transactions/pagination/load")
    public ModelAndView loadPaginationTransactions(Pageable pageable,
                                                   @RequestParam String languageCode) {
        ModelAndView model = new ModelAndView("web/personal/load/pagination");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            User user = userService.getByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new EntityNotFoundException("User with email: " + userDetails.getUsername() + " was not found!"));
            PageResponse<TransactionResponseForAccount> pageResponse = transactionsService.getAll(user, pageable, LanguageCode.fromString(languageCode));
            model.addObject("pageData", pageResponse.getMetadata());

        }
        return model;
    }
}
