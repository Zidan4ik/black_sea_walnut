package org.example.black_sea_walnut.service;

import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.ResponseProductForAdd;
import org.example.black_sea_walnut.dto.ResponseProductForView;
import org.example.black_sea_walnut.entity.Product;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    List<Product> getAll();

    PageResponse<ResponseProductForView> getAll(ResponseProductForView response, Pageable pageable, LanguageCode code);

    Product save(Product entity);

    Product getById(Long id);

    ResponseProductForAdd getByIdLikeDTOAdd(Long id);

    void deleteById(Long id);
}
