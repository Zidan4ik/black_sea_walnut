package org.example.black_sea_walnut.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@RequestMapping("/web")
public class WebOrderController {

    @GetMapping("/checkout-fiz")
    public ModelAndView showCheckoutFiz(){
        return new ModelAndView("web/cart/checkout-fiz");
    }
    @GetMapping("/checkout-ur")
    public ModelAndView showCheckoutUr(){
        return new ModelAndView("web/cart/checkout-ur");
    }
}
