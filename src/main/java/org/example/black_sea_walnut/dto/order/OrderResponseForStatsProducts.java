package org.example.black_sea_walnut.dto.order;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderResponseForStatsProducts {
    private String article;
    private String name;
    private String count;
    private String summa;
}
