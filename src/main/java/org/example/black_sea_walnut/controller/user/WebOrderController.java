package org.example.black_sea_walnut.controller.user;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.order.ResponseOrderDetailForView;
import org.example.black_sea_walnut.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/web")
public class WebOrderController {
    private final OrderService orderService;

    @GetMapping("/checkout-fiz")
    public ModelAndView showCheckoutFiz() {
        return new ModelAndView("web/cart/checkout-fiz");
    }

    @GetMapping("/checkout-ur")
    public ModelAndView showCheckoutUr() {
        return new ModelAndView("web/cart/checkout-ur");
    }

    @GetMapping("/order/{id}")
    public ModelAndView viewOrderById(@PathVariable Long id) {
        ModelAndView model = new ModelAndView("web/order/order");
        model.addObject("orders", orderService.getByUserIdAndPersonalId(1L, id));
        model.addObject("orderId", id);
        return model;
    }
}
