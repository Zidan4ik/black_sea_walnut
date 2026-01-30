package org.example.black_sea_walnut.dto.admin.order;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OrderUserResponseForView {
    private Long id;
    private String date;
    private String statusOrder;
    private String price;
    private String typePayment;
}