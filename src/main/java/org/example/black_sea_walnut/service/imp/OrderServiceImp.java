package org.example.black_sea_walnut.service.imp;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.order.OrderUserResponseForView;
import org.example.black_sea_walnut.dto.order.ResponseOrderForAdd;
import org.example.black_sea_walnut.dto.order.ResponseOrderForView;
import org.example.black_sea_walnut.entity.Order;
import org.example.black_sea_walnut.entity.User;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.mapper.OrderMapper;
import org.example.black_sea_walnut.repository.OrderRepository;
import org.example.black_sea_walnut.service.OrderService;
import org.example.black_sea_walnut.service.specifications.OrderSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImp implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper mapper = new OrderMapper();

    @Override
    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    @Override
    public PageResponse<ResponseOrderForView> getAll(ResponseOrderForView response, Pageable pageable, LanguageCode code) {
        Page<Order> page = orderRepository.findAll(OrderSpecification.getSpecification(response), pageable);
        List<ResponseOrderForView> responsesDtoAdd = page.map(mapper::toDTOView).stream().toList();
        return new PageResponse<>(responsesDtoAdd, new PageResponse.Metadata(
                page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages()
        ));
    }

    @Override
    public List<OrderUserResponseForView> getAllByUser(User user) {
        return orderRepository.findAllByUser(user).stream().map(mapper::toResponseForUserOrderView).toList();
    }

    @Override
    public ResponseOrderForAdd getByIdInDTOAdd(Long id) {
        return mapper.toDTOAdd(getById(id));
    }

    @Override
    public Order getById(Long id) {
        return orderRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Order was not found!")
        );
    }

    @Override
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }
}
