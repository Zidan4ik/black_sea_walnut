package org.example.black_sea_walnut.controller.user;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.web.BasketResponseForCart;
import org.example.black_sea_walnut.entity.Basket;
import org.example.black_sea_walnut.entity.Product;
import org.example.black_sea_walnut.entity.User;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.service.BasketService;
import org.example.black_sea_walnut.service.ProductService;
import org.example.black_sea_walnut.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/web")
public class WebCartController {
    private final UserService userService;
    private final BasketService basketService;
    private final ProductService productService;

    @GetMapping("/cart")
    public ModelAndView viewCart() {
        return new ModelAndView("web/cart/cart");
    }

    @GetMapping("/cart/get")
    public ResponseEntity<List<BasketResponseForCart>> getCurrentTemporaryOrders(@RequestParam String languageCode) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            User user = userService.getByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new EntityNotFoundException("User with email: " + userDetails.getUsername() + " was not found!"));

            List<BasketResponseForCart> baskets = basketService.getAllInResponseForCart(user, LanguageCode.fromString(languageCode));
            return ResponseEntity.ok(baskets);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/cart/decrease-amount")
    public ResponseEntity<?> decreaseAmount(@RequestParam("id") Long id) {
        basketService.decreaseAmountById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/cart/increment-amount")
    public ResponseEntity<?> incrementAmount(@RequestParam("id") Long id) {
        basketService.increaseAmountById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/cart/delete")
    public ResponseEntity<Void> deleteCartItem(@RequestParam("id") Long id) {
        Basket basket = basketService.getById(id);
        Product product = basket.getProducts().get(0);
        productService.getById(product.getId()).setTotalCount(product.getTotalCount() + basket.getCount());
        basketService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cart/save")
    public ResponseEntity<?> deleteCartItem(@RequestParam("id") Long id,
                                            @RequestParam("value") Integer value) {
        basketService.saveCountProduct(id, value);
        return ResponseEntity.ok().build();
    }
}
