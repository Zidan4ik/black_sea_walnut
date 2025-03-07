package org.example.black_sea_walnut.controller.user;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.order.ResponseOrderDetailForView;
import org.example.black_sea_walnut.dto.web.OrderResponseForAccount;
import org.example.black_sea_walnut.dto.web.TransactionResponseForAccount;
import org.example.black_sea_walnut.entity.Order;
import org.example.black_sea_walnut.entity.OrderDetail;
import org.example.black_sea_walnut.entity.User;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.service.OrderDetailService;
import org.example.black_sea_walnut.service.OrderService;
import org.example.black_sea_walnut.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
@RequestMapping("/web")
public class WebOrderController {
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;
    private final UserService userService;

    @GetMapping("/checkout-fiz")
    public ModelAndView showCheckoutFiz() {
        return new ModelAndView("web/cart/checkout-fiz");
    }

    @GetMapping("/checkout-ur")
    public ModelAndView showCheckoutUr() {
        return new ModelAndView("web/cart/checkout-ur");
    }

    @GetMapping("/order")
    public ModelAndView viewOrderById(@RequestParam(required = false) Long id) {
        return new ModelAndView("web/order/order");
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<?> getOrderForWeb(@PathVariable Long id) {
        Order order = orderService.getById(id);
        List<ResponseOrderDetailForView> orderDetails = orderDetailService.getAllInResponseForViewInPageByOrder(order);
        return new ResponseEntity<>(orderDetails, HttpStatus.OK);
    }

    @PostMapping("/order/decrease-amount")
    public ResponseEntity<?> decreaseAmountOrderDetails(@RequestParam("id") Long id) {
        orderDetailService.decreaseAmountById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/order/increment-amount")
    public ResponseEntity<?> incrementAmountOrderDetails(@RequestParam("id") Long id) {
        orderDetailService.increaseAmountById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/order/save")
    public ResponseEntity<?> deleteCartItemOrderDetails(@RequestParam("id") Long id,
                                                        @RequestParam("value") Integer value) {
        OrderDetail orderDetail = orderDetailService.getById(id);
        orderDetail.setCount(value);
        orderDetailService.save(orderDetail);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/orders/table/load")
    public ModelAndView loadTableOrders(Pageable pageable,
                                        @RequestParam String languageCode) {
        ModelAndView model = new ModelAndView("web/personal/load/orders-table");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            User user = userService.getByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new EntityNotFoundException("User with email: " + userDetails.getUsername() + " was not found!"));
            PageResponse<OrderResponseForAccount> pageResponse = orderService.getAll(user, pageable, LanguageCode.fromString(languageCode));
            Sort sort = pageable.getSort();
            List<String> sortFields = List.of("id", "dateOfOrdering", "totalPrice", "countProducts", "deliveryStatus");
            sortFields.forEach(field -> model.addObject(field, Optional.ofNullable(sort.getOrderFor(field))
                    .map(Sort.Order::getDirection)
                    .orElse(Sort.Direction.ASC)));
            model.addObject("data", pageResponse.getContent());
        }
        return model;
    }

    @GetMapping("/orders/pagination/load")
    public ModelAndView loadPaginationOrders(Pageable pageable,
                                             @RequestParam String languageCode) {
        ModelAndView model = new ModelAndView("web/personal/load/pagination");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            User user = userService.getByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new EntityNotFoundException("User with email: " + userDetails.getUsername() + " was not found!"));
            PageResponse<OrderResponseForAccount> pageResponse = orderService.getAll(user, pageable, LanguageCode.fromString(languageCode));
            model.addObject("pageData", pageResponse.getMetadata());
        }
        return model;
    }
}