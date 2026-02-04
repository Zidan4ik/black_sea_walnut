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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public interface ProductService {
    List<Product> getAll();

    PageResponse<ProductResponseForViewInProducts> getAll(ProductResponseForViewInProducts response, Pageable pageable, LanguageCode code);

    PageResponse<ProductResponseForViewInTable> getAll(ProductResponseForShopPage response, Pageable pageable, LanguageCode code);

    Product save(Product entity);

    Product save(ProductRequestForAdd dto);

    Product getById(Long id);

    Product getByArticleId(Long id);

    ProductResponseForAdd getByIdLikeDTOAdd(Long id);

    void deleteById(Long id) throws IOException;

    boolean isExistByArticleId(Long id);

    boolean isExistById(Long id);

    List<ProductResponseForViewInTable> getRandomProductsBySizeForDto(int size, LanguageCode code);

    List<Product> getRandomProductsBySize(int size);

    List<Integer> getAllMasses();

    void decreaseCountItems(Long productId);

    void increaseCountItems(Long productId);

    void processImage(MultipartFile image, String imagePath, Consumer<String> pathSetter, Product existingProduct, String fieldName);

    void updateBasicFields(Product product, ProductRequestForAdd dto);
}
