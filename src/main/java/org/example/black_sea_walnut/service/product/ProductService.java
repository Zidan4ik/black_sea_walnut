package org.example.black_sea_walnut.service.product;

import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.admin.product.ProductRequestForAdd;
import org.example.black_sea_walnut.dto.admin.product.ProductResponseForAdd;
import org.example.black_sea_walnut.dto.web.ProductResponseForViewInTable;
import org.example.black_sea_walnut.entity.Product;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.mapper.ProductMapper;
import org.example.black_sea_walnut.service.history.GenericsMapper;
import org.example.black_sea_walnut.service.user.Saveable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public interface ProductService {
    List<Product> getAll();

    <R> PageResponse<R> getAll(Specification<Product> spec, Pageable pageable, Function<Product, R> mappingFunction);

    Product save(Product entity);

    <M extends GenericsMapper> Product save(Saveable<Product, M> dto, M mapper);

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
}
