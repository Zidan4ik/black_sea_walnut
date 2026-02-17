package org.example.black_sea_walnut.controller.user;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.admin.contact.ContactDtoForAdd;
import org.example.black_sea_walnut.dto.admin.pages.main.response.AimBlockResponseForAddInMain;
import org.example.black_sea_walnut.dto.admin.pages.main.response.NumberBlockResponseForAddInMain;
import org.example.black_sea_walnut.dto.web.PaymentResponseForView;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.enums.PageType;
import org.example.black_sea_walnut.service.ContactService;
import org.example.black_sea_walnut.service.HistoryMainService;
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
    private final HistoryMainService historyMainService;
    private final ContactService contactService;

    @GetMapping("/payment")
    public ModelAndView viewPaymentPage() {
        return new ModelAndView("web/payment/payment");
    }

    @GetMapping("/payment/data")
    public ResponseEntity<PaymentResponseForView> getPaymentData(@RequestParam(name = "lang") LanguageCode code) {
        NumberBlockResponseForAddInMain numbers = historyMainService.getByPageTypeInResponseNumberBlock(PageType.main_numbers);
        AimBlockResponseForAddInMain aim = historyMainService.getByPageTypeInResponseAimBlock(PageType.main_aim);
        ContactDtoForAdd contacts = contactService.getByIdInDto(1l);
        return new ResponseEntity<>(
                PaymentResponseForView.builder()
                .aim(aim)
                .numbers(numbers)
                .contacts(contacts)
                .build(), HttpStatus.OK);
    }
}
