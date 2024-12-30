package org.example.black_sea_walnut.controller;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.ResponseDiscountForView;
import org.example.black_sea_walnut.dto.ResponseProductForView;
import org.example.black_sea_walnut.dto.ResponseTasteForView;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.service.DiscountService;
import org.example.black_sea_walnut.service.ProductService;
import org.example.black_sea_walnut.service.TasteService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class ProductController {
    private final ProductService productService;
    private final DiscountService discountService;
    private final TasteService tasteService;
    @GetMapping("/warehouse")
    public ModelAndView viewWareAndHouse(){
        return new ModelAndView("/admin/products/warehouse");
    }
    @GetMapping("/products")
    private ModelAndView viewTransactions(){
        return new ModelAndView("/admin/products/table");
    }
    @GetMapping("/products/table/load")
    public ModelAndView loadTable(@ModelAttribute ResponseProductForView responseProductForView,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "5") int size,
                                  @RequestParam String languageCode) {
        ModelAndView model = new ModelAndView("admin/fragments/table-products");
        PageRequest pageable = PageRequest.of(page, size);
        PageResponse<ResponseProductForView> pageResponse = productService.getAll(responseProductForView, pageable, LanguageCode.fromString(languageCode));
        model.addObject("data", pageResponse.getContent());

        Set<ResponseTasteForView> names = tasteService.getAllByLanguageCodeInDTO(LanguageCode.valueOf(languageCode));
        Set<ResponseDiscountForView> discounts = discountService.getAllByLanguageCodeInDTO(LanguageCode.valueOf(languageCode));
        model.addObject("tastes", tasteService.getSentence(names));
        model.addObject("discounts", discountService.getSentence(discounts));

        return model;
    }
    @GetMapping("/products/pagination/load")
    public ModelAndView loadPagination(@ModelAttribute ResponseProductForView responseProductForView,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "5") int size,
                                       @RequestParam String languageCode) {
        ModelAndView model = new ModelAndView("admin/fragments/pagination");
        PageRequest pageable = PageRequest.of(page, size);
        PageResponse<ResponseProductForView> pageResponse = productService.getAll(responseProductForView, pageable, LanguageCode.fromString(languageCode));
        model.addObject("pageData", pageResponse.getMetadata());
        return model;
    }

    @GetMapping("/product/create")
    public ModelAndView createProduct(){
        return new ModelAndView("admin/products/product-add");
    }

//    @GetMapping("/product/{id}")
//    public ResponseEntity<ResponseProductForView> getProductById(@PathVariable Long id){
//
//    }
}
