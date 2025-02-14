package org.example.black_sea_walnut.dto.order;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderResponseForStatsProducts {
    private String article;
    private String nameUk;
    private String nameEn;
    private String count;
    private String summa;
}
