package org.example.black_sea_walnut.dto.admin.order;

import lombok.Builder;
import lombok.Getter;
import org.example.black_sea_walnut.enums.DeliveryStatus;
import org.example.black_sea_walnut.enums.DeliveryType;
import org.example.black_sea_walnut.enums.OrderStatus;
import org.example.black_sea_walnut.enums.PaymentType;

@Builder
@Getter
public class ResponseOrderForView {
    private Long id;
    private String fullName;
    private String email;
    private Integer totalCount;
    private Integer totalPrice;
    private DeliveryStatus statusDelivery;
    private PaymentType typePayment;
    private Boolean isPay;
    private OrderStatus statusOrder;
    private String dateOfOrdering;
    private DeliveryType typeOfDelivery;
}