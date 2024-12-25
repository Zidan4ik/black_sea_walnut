package org.example.black_sea_walnut.mapper;

import org.example.black_sea_walnut.dto.ResponseOrderDetailForView;
import org.example.black_sea_walnut.dto.ResponseOrderForAdd;
import org.example.black_sea_walnut.dto.ResponseOrderForView;
import org.example.black_sea_walnut.entity.Order;
import org.example.black_sea_walnut.util.DateUtil;

import java.util.List;

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
                .isFop(entity.getUser().getIsFop().toString())
                .build();
    }
}
