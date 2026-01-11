package org.example.black_sea_walnut.dto.admin.order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ResponseOrderDetailForView {
    private Long id;
    private String productName;
    private int count;
    private int price;
    private int totalSumma;
}