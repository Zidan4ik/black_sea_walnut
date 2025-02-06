package org.example.black_sea_walnut.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.HistoryRequestPricesForProduct;
import org.example.black_sea_walnut.dto.discount.DiscountResponseForView;
import org.example.black_sea_walnut.dto.product.ProductRequestForAdd;
import org.example.black_sea_walnut.dto.product.ProductResponseForAdd;
import org.example.black_sea_walnut.dto.product.ResponseAllDiscountsAndTastes;
import org.example.black_sea_walnut.dto.product.ProductResponseForView;
import org.example.black_sea_walnut.dto.taste.TasteResponseForView;
import org.example.black_sea_walnut.entity.Product;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.service.DiscountService;
import org.example.black_sea_walnut.service.HistoryPricesService;
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
    private final HistoryPricesService historyPricesService;

    @GetMapping("/warehouse")
    public ModelAndView viewWareAndHouse() {
        return new ModelAndView("/admin/products/warehouse");
    }

    @GetMapping("/products")
    private ModelAndView viewTransactions() {
        return new ModelAndView("/admin/products/table");
    }

    @GetMapping("/products/table/load")
    public ModelAndView loadTable(@ModelAttribute ProductResponseForView responseProductForView,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "5") int size,
                                  @RequestParam String languageCode) {
        ModelAndView model = new ModelAndView("admin/fragments/table-products");
        PageRequest pageable = PageRequest.of(page, size);
        PageResponse<ProductResponseForView> pageResponse = productService.getAll(responseProductForView, pageable, LanguageCode.fromString(languageCode));
        model.addObject("data", pageResponse.getContent());

        Set<TasteResponseForView> names = tasteService.getAllByLanguageCodeInDTO(LanguageCode.valueOf(languageCode));
        Set<DiscountResponseForView> discounts = discountService.getAllByLanguageCodeInDTO(LanguageCode.valueOf(languageCode));
        model.addObject("tastes", tasteService.getSentence(names));
        model.addObject("discounts", discountService.getSentence(discounts));

        return model;
    }

    @GetMapping("/products/pagination/load")
    public ModelAndView loadPagination(@ModelAttribute ProductResponseForView responseProductForView,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "5") int size,
                                       @RequestParam String languageCode) {
        ModelAndView model = new ModelAndView("admin/fragments/pagination");
        PageRequest pageable = PageRequest.of(page, size);
        PageResponse<ProductResponseForView> pageResponse = productService.getAll(responseProductForView, pageable, LanguageCode.fromString(languageCode));
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

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

            return ResponseEntity
                    .status(HttpStatus.valueOf(400))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errors);
        }
        Product product = productService.save(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/tastesAndDiscounts/get")
    public ResponseEntity<ResponseAllDiscountsAndTastes> getTastesAndDiscounts() {
        Set<TasteResponseForView> tastesUk = tasteService.getAllByLanguageCodeInDTO(LanguageCode.uk);
        Set<TasteResponseForView> tastesEn = tasteService.getAllByLanguageCodeInDTO(LanguageCode.en);
        Set<DiscountResponseForView> discountUk = discountService.getAllByLanguageCodeInDTO(LanguageCode.uk);
        Set<DiscountResponseForView> discountEn = discountService.getAllByLanguageCodeInDTO(LanguageCode.en);
        ResponseAllDiscountsAndTastes dto = ResponseAllDiscountsAndTastes.builder().tastesUk(tastesUk).tastesEn(tastesEn).discountsUk(discountUk).discountsEn(discountEn).build();
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }


    @GetMapping("/product/{id}/edit")
    public ModelAndView viewProduct(@PathVariable Long id){
        ModelAndView modelAndView = new ModelAndView("admin/products/product-edit");
        modelAndView.addObject("id",id);
        return modelAndView;
    }

    @SneakyThrows
    @DeleteMapping("/product/{id}/delete")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/product/{id}")
    @ResponseBody
    public ResponseEntity<ProductResponseForAdd> getProduct(@PathVariable Long id){
        ResponseEntity<ProductResponseForAdd> productResponseForAddResponseEntity = new ResponseEntity<>(productService.getByIdLikeDTOAdd(id), HttpStatus.OK);
        return productResponseForAddResponseEntity;
    }

    @GetMapping("/products/configuration")
    public ModelAndView viewConfiguration(){
        return new ModelAndView("/admin/products/configuration");
    }
}
