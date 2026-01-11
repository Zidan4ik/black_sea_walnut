package org.example.black_sea_walnut.mapper;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.admin.order.*;
import org.example.black_sea_walnut.dto.web.OrderResponseForAccount;
import org.example.black_sea_walnut.dto.web.checkout.CheckoutUser;
import org.example.black_sea_walnut.entity.Order;
import org.example.black_sea_walnut.entity.OrderDetail;
import org.example.black_sea_walnut.entity.User;
import org.example.black_sea_walnut.enums.*;
import org.example.black_sea_walnut.util.DateUtil;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderMapper {
    private final OrderDetailMapper orderDetailMapper;

    public ResponseOrderForView toDTOView(Order entity) {
        return ResponseOrderForView.builder()
                .id(entity.getId())
                .fullName(entity.getFio())
                .email(entity.getEmail())
                .totalCount(entity.getCountProducts())
                .totalPrice(entity.getTotalPrice())
                .statusDelivery(entity.getDeliveryStatus())
                .typeOfDelivery(entity.getDeliveryType())
                .statusOrder(entity.getOrderStatus())
                .typePayment(entity.getPaymentType())
                .isPay(entity.isPayed())
                .dateOfOrdering(DateUtil.toFormatDateFromDB(entity.getDateOfOrdering(), "dd.MM.yyyy"))
                .build();
    }

    public ResponseOrderForAdd toDTOAdd(Order entity) {
        List<ResponseOrderDetailForView> list = entity.getOrderDetails().stream().map(OrderDetailMapper::toDTOView).toList();
        User user = entity.getUser();
        return ResponseOrderForAdd
                .builder()
                .id(entity.getId())
                .fio(entity.getFio())
                .phone(entity.getPhone())
                .email(entity.getEmail())
                .dateOfOrdering(DateUtil.toFormatDateFromDB(entity.getDateOfOrdering(), "dd.MM.yyyy"))
                .deliveryStatus(entity.getDeliveryStatus())
                .deliveryType(entity.getDeliveryType())
                .paymentType(entity.getPaymentType())
                .isPayed(entity.isPayed())
                .orderStatus(entity.getOrderStatus())
                .city(user != null && user.getCity() != null? user.getCity().toString() : null)
                .companyDelivery(entity.getCompanyDelivery())
                .personNameDelivery(entity.getPersonNameDelivery())
                .emailDelivery(entity.getEmailDelivery())
                .phoneDelivery(entity.getPhoneDelivery())
                .addressDelivery(entity.getAddressDelivery())
                .orderDetails(list)
                .isFop(user != null && user.isFop())
                .userRegisterType(user != null ? user.getRegisterType().toString() : RegisterType.fop.toString())
                .build();
    }

    public OrderUserResponseForView toResponseForUserOrderView(Order entity) {
        return OrderUserResponseForView
                .builder()
                .id(entity.getId())
                .date(DateUtil.toFormatDateFromDB(entity.getDateOfOrdering(), "dd.MM.yyyy"))
                .statusOrder(entity.getOrderStatus().toString())
                .price(String.valueOf(entity.getTotalPrice()))
                .statusPayment(entity.getPaymentType().toString())
                .build();
    }

    public OrderResponseForStatsProducts toResponseForStatsProducts2(OrderDetail entity) {
        return OrderResponseForStatsProducts
                .builder()
                .article(String.valueOf(entity.getId()))
                .nameUk(entity.getProductName())
                .count(String.valueOf(entity.getCount()))
                .summa(String.valueOf(entity.getSummaWithDiscount()))
                .build();
    }

    public List<OrderResponseForStatsProducts> toResponseForStatsProduct(List<Object[]> entities) {
        List<OrderResponseForStatsProducts> dto = new ArrayList<>();
        for (Object[] o : entities) {
            dto.add(OrderResponseForStatsProducts
                    .builder()
                    .article(String.valueOf(o[0]))
                    .nameUk(String.valueOf(o[1]))
                    .nameEn(String.valueOf(o[2]))
                    .count(String.valueOf(o[3]))
                    .summa(String.valueOf(o[4]))
                    .build());
        }
        return dto;
    }

    public OrderResponseForAccount toResponseForAccount(Order entity) {
        DeliveryStatus status = entity.getDeliveryStatus();
        return OrderResponseForAccount
                .builder()
                .id(entity.getId())
                .date(DateUtil.toFormatDateFromDB(entity.getDateOfOrdering(), "dd.MM.yyyy"))
                .amount(String.valueOf(entity.getCountProducts()))
                .status(status != null ? status.toString() : "")
                .cost(String.valueOf(entity.getTotalPrice()))
                .build();
    }

    public Order toEntityAfterCheckout(CheckoutUser dto) {
        Order entity = new Order();
        entity.setFio(dto.getFio());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setCountProducts(Math.toIntExact(dto.getTotalAmount()));
        entity.setTotalPrice(Math.toIntExact(dto.getTotalCount()));

        entity.setCompanyDelivery(dto.getCompanyDelivery());
        entity.setPersonNameDelivery(dto.getPersonNameDelivery());

        entity.setOrderStatus(OrderStatus.new_);
        entity.setDeliveryStatus(DeliveryStatus.await);
        entity.setDeliveryType(DeliveryType.fromString(dto.getTypeOfDelivery()));
        entity.setDateOfOrdering(LocalDate.now());
        PaymentType paymentType = PaymentType.fromString(dto.getTypeOfPayment());
        entity.setPaymentType(paymentType);
        entity.setPayed(paymentType.equals(PaymentType.card));
        entity.setOrderDetails(dto.getProducts() != null ? dto.getProducts().stream()
                .map(o -> orderDetailMapper.toEntityForBasket(o, entity)).toList() : new ArrayList<>());
        entity.setUser(dto.getUser());
        return entity;
    }
}