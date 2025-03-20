package org.example.black_sea_walnut.service.imp;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.admin.order.ResponseOrderDetailForView;
import org.example.black_sea_walnut.entity.Order;
import org.example.black_sea_walnut.entity.OrderDetail;
import org.example.black_sea_walnut.mapper.OrderDetailMapper;
import org.example.black_sea_walnut.repository.OrderDetailsRepository;
import org.example.black_sea_walnut.service.OrderDetailService;
import org.example.black_sea_walnut.util.LogUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImp implements OrderDetailService {
    private final OrderDetailsRepository orderDetailsRepository;

    @Override
    public List<ResponseOrderDetailForView> getAllInResponseForViewInPageByOrder(Order order) {
        LogUtil.logInfo("Fetching all order details for order with id: " + order.getId());

        List<ResponseOrderDetailForView> response = orderDetailsRepository.getAllByOrder(order).stream()
                .map(OrderDetailMapper::toDTOView)
                .toList();

        LogUtil.logInfo("Mapped " + response.size() + " order details to response view for order with id: " + order.getId());

        return response;
    }

    @Override
    @Transactional
    public void decreaseAmountById(Long id) {
        LogUtil.logInfo("Decreasing amount for order detail with id: " + id);
        OrderDetail orderDetail = getById(id);

        if (orderDetail.getCount() <= 1) {
            LogUtil.logInfo("Count is 1 or less, deleting order detail with id: " + id);
            deleteById(id);
        } else {
            orderDetail.setCount(orderDetail.getCount() - 1);
            LogUtil.logInfo("Decreased count for order detail with id: " + id + ". New count: " + orderDetail.getCount());
        }
    }

    @Override
    @Transactional
    public void increaseAmountById(Long id) {
        LogUtil.logInfo("Increasing amount for order detail with id: " + id);
        OrderDetail orderDetail = getById(id);
        orderDetail.setCount(orderDetail.getCount() + 1);
        LogUtil.logInfo("Increased count for order detail with id: " + id + ". New count: " + orderDetail.getCount());
    }

    @Override
    public void save(OrderDetail orderDetail) {
        LogUtil.logInfo("Saving order detail: " + orderDetail);
        orderDetailsRepository.save(orderDetail);
    }

    @Override
    public OrderDetail getById(Long id) {
        LogUtil.logInfo("Fetching order detail with id: " + id);
        return orderDetailsRepository.findById(id)
                .orElseThrow(() -> {
                    LogUtil.logError("Order detail with id " + id + " not found", null);
                    return new EntityNotFoundException("OrderDetail with id: " + id + " was not found!");
                });
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        LogUtil.logInfo("Deleting order detail with id: " + id);
        orderDetailsRepository.deleteById(id);
    }
}
