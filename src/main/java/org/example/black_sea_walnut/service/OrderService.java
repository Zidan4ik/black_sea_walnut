package org.example.black_sea_walnut.service;

import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.ResponseOrderForAdd;
import org.example.black_sea_walnut.dto.ResponseOrderForView;
import org.example.black_sea_walnut.entity.Order;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    List<Order> getAll();

    PageResponse<ResponseOrderForView> getAll(ResponseOrderForView response, Pageable pageable, LanguageCode code);

    Order getById(Long id);

    ResponseOrderForAdd getByIdInDTOAdd(Long id);

    void deleteById(Long id);
}
