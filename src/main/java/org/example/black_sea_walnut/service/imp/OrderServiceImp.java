package org.example.black_sea_walnut.service.imp;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.admin.order.*;
import org.example.black_sea_walnut.dto.web.OrderResponseForAccount;
import org.example.black_sea_walnut.dto.web.checkout.CheckoutUser;
import org.example.black_sea_walnut.entity.Order;
import org.example.black_sea_walnut.entity.OrderDetail;
import org.example.black_sea_walnut.entity.User;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.mapper.OrderDetailMapper;
import org.example.black_sea_walnut.mapper.OrderMapper;
import org.example.black_sea_walnut.repository.OrderDetailsRepository;
import org.example.black_sea_walnut.repository.OrderRepository;
import org.example.black_sea_walnut.service.OrderService;
import org.example.black_sea_walnut.service.specifications.OrderSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImp implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper mapper = new OrderMapper();
    private final OrderDetailMapper orderDetailMapper;
    private final OrderDetailsRepository orderDetailsRepository;

//    private final OrderMapper orderMapper;

    @Override
    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    @Override
    public List<ResponseOrderDetailForView> getByUserIdAndPersonalId(Long userId, Long personalId) {
        Optional<Order> orderOptional = orderRepository.findOrderByUserAndPersonalId(userId, personalId);
        if (orderOptional.isPresent()) {
            List<OrderDetail> details = orderOptional.get().getOrderDetails();
            return details.stream().map(OrderDetailMapper::toDTOView).toList();
        }
        return null;
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
    public PageResponse<OrderResponseForAccount> getAll(User user, Pageable pageable, LanguageCode code) {
        Specification<Order> spec = Specification.where(OrderSpecification.hasUser(user));
        Page<Order> page = orderRepository.findAll(spec, pageable);
        List<OrderResponseForAccount> responsesDtoAdd = page.map(mapper::toResponseForAccount).stream().toList();
        return new PageResponse<>(responsesDtoAdd, new PageResponse.Metadata(
                page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages()
        ));
    }


    @Override
    public PageResponse<OrderResponseForAccount> getAll(Pageable pageable, LanguageCode code) {
        Specification<Order> spec = Specification.where(null);
        Page<Order> page = orderRepository.findAll(spec, pageable);
        List<OrderResponseForAccount> responsesDtoAdd = page.map(mapper::toResponseForAccount).stream().toList();
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

    @Override
    public void save(Order entity) {
        orderRepository.save(entity);
    }

    @Override
    public void saveAfterPayment(CheckoutUser dto) {
        Order order = mapper.toEntityAfterCheckout(dto);
//        dto.getProducts().stream().map(OrderDetailMapper::toDTOView).toList()
    }

    @Override
    public List<Map<String, Object>> getOrdersCountByDate(LocalDate start, LocalDate end) {
        return orderRepository.countOrdersByMonth(start, end);
    }

    @Override
    public List<OrderResponseForStatsProducts> getTopProductBySalesInMonth(LocalDate start, LocalDate end, int size) {
        Pageable pageAble = PageRequest.of(0, size);
        List<Object[]> entities = orderDetailsRepository.findSummaWithDiscountByProductAndDateRange(start, end, pageAble);
        return mapper.toResponseForStatsProduct(entities);
    }
}
