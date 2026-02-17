package org.example.black_sea_walnut.controller;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.web.checkout.CheckoutUser;
import org.example.black_sea_walnut.entity.User;
import org.example.black_sea_walnut.enums.PaymentType;
import org.example.black_sea_walnut.enums.RegisterType;
import org.example.black_sea_walnut.service.*;
import org.example.black_sea_walnut.validator.groupValidation.OrderedEmailValidation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class PaymentController {
    private final OrderService orderService;
    private final TransactionsService transactionsService;
    private final BasketService basketService;
    private final UserService userService;

    @GetMapping("/payment")
    public String showPaymentPage() {
        return "web/payment";
    }

    @PostMapping("/create-payment-intent") // ??? test page for payment
    @ResponseBody
    public Map<String, String> createPaymentIntent(@RequestBody Long amount) throws StripeException {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amount)
                .setCurrency("uah")
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);
        Map<String, String> response = new HashMap<>();
        response.put("clientSecret", paymentIntent.getClientSecret());
        return response;
    }

    @PostMapping("/checkout/card")
    @ResponseBody
    public ResponseEntity<?> checkoutUserCard(@Validated({OrderedEmailValidation.class, Default.class})
                                              @ModelAttribute CheckoutUser dto, BindingResult bindingResult) throws StripeException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
                return ResponseEntity
                        .status(HttpStatus.valueOf(400))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(errors);
            }

            User user = userService.getByEmail(userDetails.getUsername()).orElseThrow(
                    (() -> new EntityNotFoundException("User with email: " + userDetails.getUsername() + " was not found!"))
            );
            dto.setUser(user);

            PaymentType paymentType = PaymentType.fromString(dto.getTypeOfPayment());
            if (paymentType.equals(PaymentType.card)) {

                PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                        .setAmount(dto.getTotalAmount())
                        .setCurrency("uah")
                        .build();

                PaymentIntent paymentIntent = PaymentIntent.create(params);
                Map<String, String> response = new HashMap<>();
                response.put("clientSecret", paymentIntent.getClientSecret());

                orderService.saveAfterPayment(dto);
                transactionsService.save(dto);
                basketService.deleteByUser(user);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            orderService.saveAfterPayment(dto);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}