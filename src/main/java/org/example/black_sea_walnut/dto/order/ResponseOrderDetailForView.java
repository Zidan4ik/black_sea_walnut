package org.example.black_sea_walnut.dto.order;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ResponseOrderDetailForView {
    private Long id;
    private String productName;
    private int count;
    private int price;
    private int totalSumma;
}
