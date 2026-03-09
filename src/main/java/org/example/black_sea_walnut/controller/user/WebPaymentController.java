package org.example.black_sea_walnut.controller.user;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.admin.contact.ContactDtoForAdd;
import org.example.black_sea_walnut.dto.admin.pages.main.response.AimBlockResponseForAddInMain;
import org.example.black_sea_walnut.dto.admin.pages.main.response.NumberBlockResponseForAddInMain;
import org.example.black_sea_walnut.dto.web.PaymentResponseForView;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.enums.PageType;
import org.example.black_sea_walnut.mapper.ContactMapper;
import org.example.black_sea_walnut.mapper.pages.HistoryMainMapper;
import org.example.black_sea_walnut.service.contact.ContactService;
import org.example.black_sea_walnut.service.history.HistoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@RequestMapping("/web")
public class WebPaymentController {
    private final ContactService contactService;
    private final HistoryService historyService;
    private final HistoryMainMapper mainMapper;
    private final ContactMapper contactMapper;

    @GetMapping("/payment")
    public ModelAndView viewPaymentPage() {
        return new ModelAndView("web/payment/payment");
    }

    @GetMapping("/payment/data")
    public ResponseEntity<PaymentResponseForView> getPaymentData(@RequestParam(name = "lang") LanguageCode code) {
        NumberBlockResponseForAddInMain numbers = historyService.getResponseByPageType(PageType.main_numbers,mainMapper::toResponseNumberBlockForAdd);
        AimBlockResponseForAddInMain aim = historyService.getResponseByPageType(PageType.main_aim,mainMapper::toResponseAimBlockForAdd);
        ContactDtoForAdd contacts = contactService.getDtoResponseById(1L,contactMapper::toDtoContactForAdd);
        return new ResponseEntity<>(
                PaymentResponseForView.builder()
                .aim(aim)
                .numbers(numbers)
                .contacts(contacts)
                .build(), HttpStatus.OK);
    }
}
