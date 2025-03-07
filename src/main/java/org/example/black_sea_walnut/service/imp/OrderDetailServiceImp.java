package org.example.black_sea_walnut.service.imp;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.order.ResponseOrderDetailForView;
import org.example.black_sea_walnut.entity.Order;
import org.example.black_sea_walnut.entity.OrderDetail;
import org.example.black_sea_walnut.mapper.OrderDetailMapper;
import org.example.black_sea_walnut.repository.OrderDetailsRepository;
import org.example.black_sea_walnut.service.OrderDetailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImp implements OrderDetailService {
    private final OrderDetailsRepository orderDetailsRepository;

    @Override
    public List<ResponseOrderDetailForView> getAllInResponseForViewInPageByOrder(Order order) {
        return orderDetailsRepository.getAllByOrder(order).stream().map(OrderDetailMapper::toDTOView).toList();
    }

    @Override
    @Transactional
    public void decreaseAmountById(Long id) {
        OrderDetail orderDetail = getById(id);
        if (orderDetail.getCount() <= 1) {
            deleteById(id);
        } else {
            orderDetail.setCount(orderDetail.getCount() - 1);
        }
    }

    @Override
    @Transactional
    public void increaseAmountById(Long id) {
        OrderDetail orderDetail = getById(id);
        orderDetail.setCount(orderDetail.getCount() + 1);
    }

    @Override
    public void save(OrderDetail orderDetail) {
        orderDetailsRepository.save(orderDetail);
    }

    @Override
    public OrderDetail getById(Long id) {
        return orderDetailsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("OrderDetail with id: " + id + "was not found!"));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        orderDetailsRepository.deleteById(id);
    }
}
