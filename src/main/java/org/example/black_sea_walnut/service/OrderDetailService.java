package org.example.black_sea_walnut.service;

import org.example.black_sea_walnut.dto.admin.order.ResponseOrderDetailForView;
import org.example.black_sea_walnut.entity.Order;
import org.example.black_sea_walnut.entity.OrderDetail;

import java.util.List;

public interface OrderDetailService {
    List<ResponseOrderDetailForView> getAllInResponseForViewInPageByOrder(Order order);

    void decreaseAmountById(Long id);

    void increaseAmountById(Long id);

    void save(OrderDetail orderDetail);

    OrderDetail getById(Long id);

    void deleteById(Long id);

}
