package org.example.black_sea_walnut.service.document.excel;

import org.apache.poi.ss.usermodel.Row;
import org.example.black_sea_walnut.entity.Product;
import org.example.black_sea_walnut.entity.Taste;
import org.example.black_sea_walnut.entity.translation.ProductTranslation;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Service
public class ProductExcelExporter implements ExcelExporter<Product> {

    public ByteArrayInputStream exportProducts(List<Product> products, LanguageCode lang) {

        Map<String, BiFunction<Product, Row, String>> customMappers = new LinkedHashMap<>();

        customMappers.put("PRODUCT_NAME", (product, row) ->
                product.getProductTranslations().stream()
                        .filter(t -> t.getLanguageCode() == lang)
                        .map(ProductTranslation::getName)
                        .findFirst()
                        .orElse("No translation")
        );

        customMappers.put("TASTES", (product, row) ->
                product.getTastes().stream()
                        .filter(t -> t.getLanguageCode() == lang)
                        .map(Taste::getName)
                        .collect(Collectors.joining(", "))
        );

        return exportToExcel("Products Warehouse", Product.class, products, customMappers);
    }
}