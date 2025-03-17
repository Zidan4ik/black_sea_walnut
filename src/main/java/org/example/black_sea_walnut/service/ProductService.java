package org.example.black_sea_walnut.service;

import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.admin.product.ProductRequestForAdd;
import org.example.black_sea_walnut.dto.admin.product.ProductResponseForAdd;
import org.example.black_sea_walnut.dto.admin.product.ProductResponseForShopPage;
import org.example.black_sea_walnut.dto.admin.product.ProductResponseForViewInProducts;
import org.example.black_sea_walnut.dto.web.ProductResponseForViewInTable;
import org.example.black_sea_walnut.entity.Product;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    List<Product> getAll();

    PageResponse<ProductResponseForViewInProducts> getAll(ProductResponseForViewInProducts response, Pageable pageable, LanguageCode code);

    PageResponse<ProductResponseForViewInTable> getAll(ProductResponseForShopPage response, Pageable pageable, LanguageCode code);

    Product save(Product entity);

    Product save(ProductRequestForAdd dto);

    Product getById(Long id);

    ProductResponseForAdd getByIdLikeDTOAdd(Long id);

    void deleteById(Long id) throws IOException;

    boolean isExistByArticleId(Long id);

    boolean isExistById(Long id);

    List<ProductResponseForViewInTable> getRandomProductsBySize(int size, LanguageCode code);

    List<Integer> getAllMasses();
}
