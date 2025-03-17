package org.example.black_sea_walnut.controller.user;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.admin.contact.ContactDtoForAdd;
import org.example.black_sea_walnut.dto.admin.pages.catalog.response.BannerBlockResponseForAdd;
import org.example.black_sea_walnut.dto.admin.pages.catalog.response.EcologicallyBlockResponseForAdd;
import org.example.black_sea_walnut.dto.admin.product.ProductResponseForShopPage;
import org.example.black_sea_walnut.dto.admin.taste.TasteResponseForView;
import org.example.black_sea_walnut.dto.web.ProductResponseForView;
import org.example.black_sea_walnut.dto.web.ProductResponseForViewInTable;
import org.example.black_sea_walnut.dto.web.ProductResponseInWeb;
import org.example.black_sea_walnut.dto.web.ShopResponseForView;
import org.example.black_sea_walnut.dto.web.shop.ProductRequestForBuy;
import org.example.black_sea_walnut.entity.Product;
import org.example.black_sea_walnut.entity.User;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.enums.PageType;
import org.example.black_sea_walnut.enums.RegisterType;
import org.example.black_sea_walnut.mapper.ProductMapper;
import org.example.black_sea_walnut.service.*;
import org.springframework.data.domain.PageRequest;
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
import java.util.Set;

@Controller
@RequestMapping("/web")
@RequiredArgsConstructor
public class WebShopController {
    private final ProductService productService;
    private final TasteService tasteService;
    private final HistoryCatalogService historyCatalogService;
    private final ContactService contactService;
    private final ProductMapper productMapper;
    private final BasketService basketService;
    private final UserService userService;

    @GetMapping("/shop")
    public ModelAndView viewShopPage() {
        return new ModelAndView("web/shop/shop");
    }

    @GetMapping("/products/table/load")
    public ModelAndView loadTable(@ModelAttribute ProductResponseForShopPage response,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "5") int size,
                                  @RequestParam String languageCode) {
        ModelAndView model = new ModelAndView("web/shop/products-list");
        Sort sort = response.getDirectionCost().equals("ASC") ?
                Sort.by(Sort.Direction.ASC, "priceByUnit") :
                Sort.by(Sort.Direction.DESC, "priceByUnit");
        PageRequest pageable = PageRequest.of(page, size, sort);
        PageResponse<ProductResponseForViewInTable> pageResponse = productService.getAll(response, pageable, LanguageCode.fromString(languageCode));
        model.addObject("data", pageResponse.getContent());
        model.addObject("totalPages", pageResponse.getMetadata().getTotalPages());
        return model;
    }

    @GetMapping("/shop/data")
    public ResponseEntity<ShopResponseForView> getDataForShopPage(@RequestParam("lang") String lang) {
        List<Integer> masses = productService.getAllMasses();
        Set<TasteResponseForView> tastes = tasteService.getAllByLanguageCodeInDTO(LanguageCode.fromString(lang));
        BannerBlockResponseForAdd banner = historyCatalogService.getByPageTypeInResponseBannerBlock(PageType.catalog_banner);
        EcologicallyBlockResponseForAdd ecologically = historyCatalogService.getByPageTypeInResponseEcologicallyBlock(PageType.catalog_ecologically_pure_walnut);
        ContactDtoForAdd contacts = contactService.getByIdInDto(1l);
        return new ResponseEntity<>(
                ShopResponseForView.builder()
                        .masses(masses)
                        .tastes(tastes)
                        .banner(banner)
                        .ecologically(ecologically)
                        .contacts(contacts)
                        .build(), HttpStatus.OK);
    }

    @GetMapping("/product/{id}")
    public ModelAndView viewProductPage(@PathVariable Long id) {
        return new ModelAndView("web/shop/product");
    }

    @GetMapping("/product/{id}/data")
    public ResponseEntity<ProductResponseInWeb> getDataForShopPage(@PathVariable Long id,
                                                                   @RequestParam("lang") String lang) {
        ProductResponseForView product = productMapper.toResponseForView(productService.getById(id), LanguageCode.fromString(lang));
        ContactDtoForAdd contacts = contactService.getByIdInDto(1L);
        return new ResponseEntity<>(ProductResponseInWeb
                .builder()
                .product(product)
                .contacts(contacts)
                .build(), HttpStatus.OK);
    }

    @PostMapping("/product/buy")
    public ResponseEntity<?> buyProduct(@RequestParam(name = "articleId") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            User user = userService.getByEmail(userDetails.getUsername()).orElseThrow(
                    (() -> new EntityNotFoundException("User with email: " + userDetails.getUsername() + " was not found!"))
            );
            basketService.buyProduct(id, user);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
