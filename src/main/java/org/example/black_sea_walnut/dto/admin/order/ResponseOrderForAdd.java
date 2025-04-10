package org.example.black_sea_walnut.dto.admin.order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.black_sea_walnut.enums.DeliveryStatus;
import org.example.black_sea_walnut.enums.DeliveryType;
import org.example.black_sea_walnut.enums.OrderStatus;
import org.example.black_sea_walnut.enums.PaymentType;

import java.util.List;

@Builder
@Getter
@Setter
public class ResponseOrderForAdd {
    private Long id;
    private boolean isFop;
    private String userRegisterType;
    private String fio;
    private String phone;
    private String email;
    private String dateOfOrdering;
    private DeliveryStatus deliveryStatus;
    private DeliveryType deliveryType;
    private PaymentType paymentType;
    private Boolean isPayed;
    private OrderStatus orderStatus;
    private String city;
    private String companyDelivery;
    private String personNameDelivery;
    private String emailDelivery;
    private String phoneDelivery;
    private String addressDelivery;
    List<ResponseOrderDetailForView> orderDetails;
}
