package org.example.black_sea_walnut.dto.admin.order;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderResponseForStatistic {
    private String date;
    private Long count;
}
