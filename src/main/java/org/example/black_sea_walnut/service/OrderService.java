package org.example.black_sea_walnut.service;

import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.order.OrderUserResponseForView;
import org.example.black_sea_walnut.dto.order.ResponseOrderForAdd;
import org.example.black_sea_walnut.dto.order.ResponseOrderForView;
import org.example.black_sea_walnut.entity.Order;
import org.example.black_sea_walnut.entity.User;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    List<Order> getAll();

    PageResponse<ResponseOrderForView> getAll(ResponseOrderForView response, Pageable pageable, LanguageCode code);

    List<OrderUserResponseForView> getAllByUser(User user);

    Order getById(Long id);

    ResponseOrderForAdd getByIdInDTOAdd(Long id);

    void deleteById(Long id);
}
