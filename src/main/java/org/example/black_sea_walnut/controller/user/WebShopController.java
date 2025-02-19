package org.example.black_sea_walnut.controller.user;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.contact.ContactDtoForAdd;
import org.example.black_sea_walnut.dto.discount.DiscountResponseForView;
import org.example.black_sea_walnut.dto.pages.catalog.response.BannerBlockResponseForAdd;
import org.example.black_sea_walnut.dto.pages.catalog.response.EcologicallyBlockResponseForAdd;
import org.example.black_sea_walnut.dto.product.ProductResponseForShopPage;
import org.example.black_sea_walnut.dto.taste.TasteResponseForView;
import org.example.black_sea_walnut.dto.web.ProductResponseForView;
import org.example.black_sea_walnut.dto.web.ShopResponseForView;
import org.example.black_sea_walnut.entity.Contact;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.enums.PageType;
import org.example.black_sea_walnut.service.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        PageRequest pageable = PageRequest.of(page, size,sort);
        PageResponse<ProductResponseForView> pageResponse = productService.getAll(response, pageable, LanguageCode.fromString(languageCode));
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
}
