package org.example.black_sea_walnut.dto.web;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OrderResponseForAccount {
    private Long id;
    private String date;
    private String amount;
    private String status;
    private String cost;
}
