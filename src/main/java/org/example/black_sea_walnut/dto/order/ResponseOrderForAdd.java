package org.example.black_sea_walnut.dto.order;

import lombok.Builder;
import lombok.Getter;
import org.example.black_sea_walnut.enums.DeliveryStatus;
import org.example.black_sea_walnut.enums.OrderStatus;
import org.example.black_sea_walnut.enums.PaymentType;

import java.util.List;

@Builder
@Getter
public class ResponseOrderForAdd {
    private Long id;
    private String isFop;
    private String fio;
    private String phone;
    private String email;
    private String dateOfOrdering;
    private DeliveryStatus deliveryStatus;
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
