package org.example.black_sea_walnut.mapper;

import org.example.black_sea_walnut.dto.order.OrderUserResponseForView;
import org.example.black_sea_walnut.dto.order.ResponseOrderDetailForView;
import org.example.black_sea_walnut.dto.order.ResponseOrderForAdd;
import org.example.black_sea_walnut.dto.order.ResponseOrderForView;
import org.example.black_sea_walnut.dto.user.response.UserFopResponseForAdd;
import org.example.black_sea_walnut.entity.Order;
import org.example.black_sea_walnut.entity.OrderDetail;
import org.example.black_sea_walnut.util.DateUtil;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {
    public ResponseOrderForView toDTOView(Order entity) {
        return ResponseOrderForView.builder()
                .id(entity.getId())
                .fullName(entity.getFio())
                .email(entity.getEmail())
                .totalCount(entity.getCountProducts())
                .totalPrice(entity.getTotalPrice())
                .statusDelivery(entity.getDeliveryStatus())
                .statusOrder(entity.getOrderStatus())
                .typePayment(entity.getPaymentType())
                .isPay(entity.isPayed())
                .dateOfOrdering(DateUtil.toFormatDateFromDB(entity.getDateOfOrdering(), "dd.MM.yyyy"))
                .build();
    }

    public ResponseOrderForAdd toDTOAdd(Order entity) {
        List<ResponseOrderDetailForView> list = entity.getOrderDetails().stream().map(OrderDetailMapper::toDTOView).toList();
        return ResponseOrderForAdd
                .builder()
                .id(entity.getId())
                .fio(entity.getFio())
                .phone(entity.getPhone())
                .email(entity.getEmail())
                .dateOfOrdering(DateUtil.toFormatDateFromDB(entity.getDateOfOrdering(), "dd.MM.yyyy"))
                .deliveryStatus(entity.getDeliveryStatus())
                .paymentType(entity.getPaymentType())
                .isPayed(entity.isPayed())
                .orderStatus(entity.getOrderStatus())
                .city(entity.getCity().getName())
                .companyDelivery(entity.getCompanyDelivery())
                .personNameDelivery(entity.getPersonNameDelivery())
                .emailDelivery(entity.getEmailDelivery())
                .phoneDelivery(entity.getPhoneDelivery())
                .addressDelivery(entity.getAddressDelivery())
                .orderDetails(list)
                .isFop(String.valueOf(entity.getUser().isFop()))
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
}