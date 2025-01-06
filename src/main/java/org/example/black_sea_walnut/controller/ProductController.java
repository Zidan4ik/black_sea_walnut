package org.example.black_sea_walnut.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.HistoryRequestPricesForProduct;
import org.example.black_sea_walnut.dto.discount.ResponseDiscountForView;
import org.example.black_sea_walnut.dto.product.ProductRequestForAdd;
import org.example.black_sea_walnut.dto.product.ResponseAllDiscountsAndTastes;
import org.example.black_sea_walnut.dto.product.ResponseProductForView;
import org.example.black_sea_walnut.dto.taste.ResponseTasteForView;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.service.DiscountService;
import org.example.black_sea_walnut.service.ProductService;
import org.example.black_sea_walnut.service.TasteService;
import org.example.black_sea_walnut.util.JsonUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class ProductController {
    private final ProductService productService;
    private final DiscountService discountService;
    private final TasteService tasteService;
    private final Validator validator;

    @GetMapping("/warehouse")
    public ModelAndView viewWareAndHouse() {
        return new ModelAndView("/admin/products/warehouse");
    }

    @GetMapping("/products")
    private ModelAndView viewTransactions() {
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
    public ModelAndView createProduct() {
        return new ModelAndView("admin/products/product-add");
    }

    @SneakyThrows
    @PostMapping("/product/save")
    public ResponseEntity<?> saveProduct(@Valid ProductRequestForAdd dto,
                                         BindingResult bindingResult) {
        HistoryRequestPricesForProduct readValue = JsonUtil.parseObject(dto.getPrices(), HistoryRequestPricesForProduct.class);
        BindingResult br = new BeanPropertyBindingResult(readValue,"prices");
        validator.validate(readValue,br);

        if (bindingResult.hasErrors() || br.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            br.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

                return ResponseEntity
                    .status(HttpStatus.valueOf(400))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errors);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/tastesAndDiscounts/get")
    public ResponseEntity<ResponseAllDiscountsAndTastes> getTastesAndDiscounts() {
        Set<ResponseTasteForView> tastesUk = tasteService.getAllByLanguageCodeInDTO(LanguageCode.uk);
        Set<ResponseTasteForView> tastesEn = tasteService.getAllByLanguageCodeInDTO(LanguageCode.en);
        Set<ResponseDiscountForView> discountUk = discountService.getAllByLanguageCodeInDTO(LanguageCode.uk);
        Set<ResponseDiscountForView> discountEn = discountService.getAllByLanguageCodeInDTO(LanguageCode.en);
        ResponseAllDiscountsAndTastes dto = ResponseAllDiscountsAndTastes.builder().tastesUk(tastesUk).tastesEn(tastesEn).discountsUk(discountUk).discountsEn(discountEn).build();
        return new ResponseEntity<>(dto, HttpStatus.OK);

    }


//    @GetMapping("/product/{id}")
//    public ResponseEntity<ResponseProductForView> getProductById(@PathVariable Long id){
//
//    }
}
