package org.example.black_sea_walnut.service;

import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.order.*;
import org.example.black_sea_walnut.dto.web.OrderResponseForAccount;
import org.example.black_sea_walnut.entity.Order;
import org.example.black_sea_walnut.entity.User;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


public interface OrderService {
    List<Order> getAll();

    List<ResponseOrderDetailForView> getByUserIdAndPersonalId(Long userId, Long personalId);

    PageResponse<ResponseOrderForView> getAll(ResponseOrderForView response, Pageable pageable, LanguageCode code);

    PageResponse<OrderResponseForAccount> getAll(User user,Pageable pageable, LanguageCode code);

    PageResponse<OrderResponseForAccount> getAll(Pageable pageable, LanguageCode code);

    List<OrderUserResponseForView> getAllByUser(User user);

    Order getById(Long id);

    ResponseOrderForAdd getByIdInDTOAdd(Long id);

    void deleteById(Long id);

    void save(Order entity);

    List<Map<String, Object>> getOrdersCountByDate(LocalDate start, LocalDate end);

    List<OrderResponseForStatsProducts> getTopProductBySalesInMonth(LocalDate start, LocalDate end, int size);
}
