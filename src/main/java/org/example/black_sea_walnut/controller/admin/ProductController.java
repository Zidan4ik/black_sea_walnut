package org.example.black_sea_walnut.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.admin.discount.DiscountResponseForView;
import org.example.black_sea_walnut.dto.admin.product.ProductRequestForAdd;
import org.example.black_sea_walnut.dto.admin.product.ProductResponseForAdd;
import org.example.black_sea_walnut.dto.admin.product.ResponseAllDiscountsAndTastes;
import org.example.black_sea_walnut.dto.admin.product.ProductResponseForViewInProducts;
import org.example.black_sea_walnut.dto.admin.taste.TasteResponseForView;
import org.example.black_sea_walnut.entity.Product;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.mapper.ProductMapper;
import org.example.black_sea_walnut.mapper.TasteMapper;
import org.example.black_sea_walnut.service.DiscountService;
import org.example.black_sea_walnut.service.document.excel.ProductExcelExporter;
import org.example.black_sea_walnut.service.document.pdf.PdfExportService;
import org.example.black_sea_walnut.service.product.ProductService;
import org.example.black_sea_walnut.service.product.taste.TasteService;
import org.example.black_sea_walnut.service.specifications.ProductSpecification;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class ProductController {
    private final ProductService productService;
    private final DiscountService discountService;
    private final TasteService tasteService;
    private final ProductMapper productMapper;
    private final TasteMapper tasteMapper;
    private final ProductExcelExporter productExcelExporter;
    private final PdfExportService pdfExportService;

    @GetMapping("/products/export/excel")
    @ResponseBody
    public ResponseEntity<InputStreamResource> exportToExcelProduct(@RequestParam(defaultValue = "uk") LanguageCode lang){
        List<Product> products = productService.getAll();
        ByteArrayInputStream in = productExcelExporter.exportProducts(products, lang);
        String fileName = "products_report_" + lang + ".xlsx";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(in));
    }

    @GetMapping("/products/export/pdf")
    @ResponseBody
    public ResponseEntity<byte[]> exportToPdfProduct(@ModelAttribute ProductResponseForViewInProducts responseProductForView,
                                                     @RequestParam(defaultValue = "uk") String lang){
        try {
            LanguageCode code = LanguageCode.fromString(lang);
            Pageable unpaged = Pageable.unpaged();
            PageResponse<ProductResponseForViewInProducts> pageResponse = productService.getAll(
                    ProductSpecification.getSpecification(responseProductForView, code),
                    unpaged,
                    product -> productMapper.toDTOForView(product, code)
            );

            List<ProductResponseForViewInProducts> dtoList = pageResponse.getContent();
            String title = code == LanguageCode.en ? "Products Report" : "Звіт по продуктах";
            byte[] pdfBytes = pdfExportService.exportToPdf(dtoList, title);
            String fileName = "products_report_" + code.name().toLowerCase() + ".pdf";
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/warehouse")
    public ModelAndView viewWareAndHouse() {
        return new ModelAndView("admin/products/warehouse");
    }

    @GetMapping("/products")
    private ModelAndView viewTransactions() {
        return new ModelAndView("admin/products/products");
    }

    @GetMapping("/products/table/load")
    public ModelAndView loadTable(@ModelAttribute ProductResponseForViewInProducts responseProductForView,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "5") int size,
                                  @RequestParam String languageCode) {
        ModelAndView model = new ModelAndView("admin/fragments/table-products");
        PageRequest pageable = PageRequest.of(page, size);
        LanguageCode code = LanguageCode.fromString(languageCode);
        PageResponse<ProductResponseForViewInProducts> pageResponse = productService.getAll(
                ProductSpecification.getSpecification(responseProductForView, code), pageable, n -> productMapper.toDTOForView(n, code));
        model.addObject("data", pageResponse.getContent());

        Set<TasteResponseForView> names = tasteService.getAllByLanguageCodeInDTO(LanguageCode.valueOf(languageCode),tasteMapper::toDTOForView);
        Set<DiscountResponseForView> discounts = discountService.getAllByLanguageCodeInDTO(LanguageCode.valueOf(languageCode));
        model.addObject("tastes", tasteService.getSentence(names));
        model.addObject("discounts", discountService.getSentence(discounts));

        return model;
    }

    @GetMapping("/products/pagination/load")
    public ModelAndView loadPagination(@ModelAttribute ProductResponseForViewInProducts responseProductForView,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "5") int size,
                                       @RequestParam String languageCode) {
        ModelAndView model = new ModelAndView("admin/fragments/pagination");
        PageRequest pageable = PageRequest.of(page, size);
        LanguageCode code = LanguageCode.fromString(languageCode);
        PageResponse<ProductResponseForViewInProducts> pageResponse = productService.getAll(
                ProductSpecification.getSpecification(responseProductForView, code), pageable, n -> productMapper.toDTOForView(n, code));
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
        productService.save(dto,productMapper);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/tastesAndDiscounts/get")
    public ResponseEntity<ResponseAllDiscountsAndTastes> getTastesAndDiscounts() {
        Set<TasteResponseForView> tastesUk = tasteService.getAllByLanguageCodeInDTO(LanguageCode.uk,tasteMapper::toDTOForView);
        Set<TasteResponseForView> tastesEn = tasteService.getAllByLanguageCodeInDTO(LanguageCode.en,tasteMapper::toDTOForView);
        Set<DiscountResponseForView> discountUk = discountService.getAllByLanguageCodeInDTO(LanguageCode.uk);
        Set<DiscountResponseForView> discountEn = discountService.getAllByLanguageCodeInDTO(LanguageCode.en);
        ResponseAllDiscountsAndTastes dto = ResponseAllDiscountsAndTastes.builder().tastesUk(tastesUk).tastesEn(tastesEn).discountsUk(discountUk).discountsEn(discountEn).build();
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }


    @GetMapping("/product/{id}/edit")
    public ModelAndView viewProduct(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("admin/products/product-edit");
        modelAndView.addObject("id", id);
        return modelAndView;
    }

    @SneakyThrows
    @DeleteMapping("/product/{id}/delete")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @SneakyThrows
    @DeleteMapping("/product/delete")
    public ResponseEntity<?> deleteProduct2(@RequestBody Long id) {
        productService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/product/{id}")
    @ResponseBody
    public ResponseEntity<ProductResponseForAdd> getProduct(@PathVariable Long id) {
        return new ResponseEntity<>(productService.getByIdLikeDTO(id,productMapper::toResponseForAdd), HttpStatus.OK);
    }

    @GetMapping("/products/configuration")
    public ModelAndView viewConfiguration() {
        return new ModelAndView("admin/products/configuration");
    }

    @ModelAttribute("isActiveWarehouse")
    public boolean toActiveSidebarButton() {
        return true;
    }
}
