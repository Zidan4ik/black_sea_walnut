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
import org.example.black_sea_walnut.service.TransactionsService;
import org.example.black_sea_walnut.service.specifications.OrderSpecification;
import org.example.black_sea_walnut.util.LogUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImp implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper mapper;
    private final OrderDetailMapper orderDetailMapper;
    private final OrderDetailsRepository orderDetailsRepository;
    private final TransactionsService transactionsService;

    @Override
    public List<Order> getAll() {
        LogUtil.logInfo("Fetching all orders.");
        List<Order> orders = orderRepository.findAll();
        LogUtil.logInfo("Fetched " + orders.size() + " orders.");
        return orders;
    }

    @Override
    public List<ResponseOrderDetailForView> getByUserIdAndPersonalId(Long userId, Long personalId) {
        LogUtil.logInfo("Fetching order details for userId: " + userId + " and personalId: " + personalId);
        Optional<Order> orderOptional = orderRepository.findOrderByUserAndPersonalId(userId, personalId);
        if (orderOptional.isPresent()) {
            List<OrderDetail> details = orderOptional.get().getOrderDetails();
            LogUtil.logInfo("Found " + details.size() + " order details for order.");
            return details.stream().map(OrderDetailMapper::toDTOView).toList();
        } else {
            LogUtil.logWarning("Order not found for userId: " + userId + " and personalId: " + personalId);
            return null;
        }
    }

    @Override
    public PageResponse<ResponseOrderForView> getAll(ResponseOrderForView response, Pageable pageable, LanguageCode code) {
        LogUtil.logInfo("Fetching orders with filter: " + response + " and page request: " + pageable);
        Page<Order> page = orderRepository.findAll(OrderSpecification.getSpecification(response), pageable);
        List<ResponseOrderForView> responsesDtoAdd = page.map(mapper::toDTOView).stream().toList();
        LogUtil.logInfo("Fetched " + responsesDtoAdd.size() + " orders.");
        return new PageResponse<>(responsesDtoAdd, new PageResponse.Metadata(
                page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages()
        ));
    }

    @Override
    public PageResponse<OrderUserResponseForView> getAllByUser(Long userId, OrderUserResponseForView response, Pageable pageable, LanguageCode code) {
        LogUtil.logInfo("Fetching orders with user id: " + userId + " with filter: " + response + " and page request: " + pageable);
        Specification<Order> specification = OrderSpecification.getSpecification(response,userId);
        Page<Order> page = orderRepository.findAll(specification, pageable);
        List<OrderUserResponseForView> responsesDtoAdd = page.map(mapper::toResponseForUserOrderView).stream().toList();
        LogUtil.logInfo("Fetched " + responsesDtoAdd.size() + " orders.");
        return new PageResponse<>(responsesDtoAdd, new PageResponse.Metadata(
                page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages()
        ));
    }

    @Override
    public PageResponse<OrderResponseForAccount> getAll(User user, Pageable pageable, LanguageCode code) {
        LogUtil.logInfo("Fetching orders for userId: " + user.getId() + " with page request: " + pageable);
        Specification<Order> spec = Specification.where(OrderSpecification.hasUser(user));
        Page<Order> page = orderRepository.findAll(spec, pageable);
        List<OrderResponseForAccount> responsesDtoAdd = page.map(mapper::toResponseForAccount).stream().toList();
        LogUtil.logInfo("Fetched " + responsesDtoAdd.size() + " orders for user.");
        return new PageResponse<>(responsesDtoAdd, new PageResponse.Metadata(
                page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages()
        ));
    }

    @Override
    public PageResponse<OrderResponseForAccount> getAll(Pageable pageable, LanguageCode code) {
        LogUtil.logInfo("Fetching all orders with page request: " + pageable);
        Specification<Order> spec = Specification.where(null);
        Page<Order> page = orderRepository.findAll(spec, pageable);
        List<OrderResponseForAccount> responsesDtoAdd = page.map(mapper::toResponseForAccount).stream().toList();
        LogUtil.logInfo("Fetched " + responsesDtoAdd.size() + " orders.");
        return new PageResponse<>(responsesDtoAdd, new PageResponse.Metadata(
                page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages()
        ));
    }

    @Override
    public List<OrderUserResponseForView> getAllByUser(User user) {
        LogUtil.logInfo("Fetching orders for userId: " + user.getId());
        List<OrderUserResponseForView> orders = orderRepository.findAllByUser(user).stream()
                .map(mapper::toResponseForUserOrderView)
                .toList();
        LogUtil.logInfo("Fetched " + orders.size() + " orders for user.");
        return orders;
    }

    @Override
    public ResponseOrderForAdd getByIdInDTOAdd(Long id) {
        LogUtil.logInfo("Fetching order by id: " + id);
        ResponseOrderForAdd order = mapper.toDTOAdd(getById(id));
        LogUtil.logInfo("Fetched order by id: " + id);
        return order;
    }

    @Override
    public Order getById(Long id) {
        LogUtil.logInfo("Fetching order by id: " + id);
        return orderRepository.findById(id).orElseThrow(
                () -> {
                    LogUtil.logError("Order not found with id: " + id, null);
                    return new EntityNotFoundException("Order was not found!");
                }
        );
    }

    @Override
    public void deleteById(Long id) {
        LogUtil.logInfo("Deleting order with id: " + id);
        orderRepository.deleteById(id);
        LogUtil.logInfo("Order with id " + id + " deleted.");
    }

    @Override
    @Transactional
    public void save(Order entity) {
        LogUtil.logInfo("Saving order: " + entity);
        orderRepository.save(entity);
        LogUtil.logInfo("Order saved: " + entity);
    }

    @Override
    @Transactional
    public void saveAfterPayment(CheckoutUser dto) {
        LogUtil.logInfo("Saving order after payment for user: " + dto.getUser().getId());
        save(mapper.toEntityAfterCheckout(dto));
    }

    @Override
    public List<Map<String, Object>> getOrdersCountByDate(LocalDate start, LocalDate end) {
        LogUtil.logInfo("Fetching orders count by date range: " + start + " to " + end);
        List<Map<String, Object>> result = orderRepository.countOrdersByMonth(start, end);
        LogUtil.logInfo("Fetched " + result.size() + " counts for orders.");
        return result;
    }

    @Override
    public List<OrderResponseForStatsProducts> getTopProductBySalesInMonth(LocalDate start, LocalDate end, int size) {
        LogUtil.logInfo("Fetching top products by sales from " + start + " to " + end + " with size: " + size);
        Pageable pageAble = PageRequest.of(0, size);
        List<Object[]> entities = orderDetailsRepository.findSummaWithDiscountByProductAndDateRange(start, end, pageAble);
        List<OrderResponseForStatsProducts> responses = mapper.toResponseForStatsProduct(entities);
        LogUtil.logInfo("Fetched " + responses.size() + " top products.");
        return responses;
    }
}
