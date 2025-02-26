package org.example.black_sea_walnut.dto.web;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TransactionResponseForAccount {
    private Long id;
    private String date;
    private String status;
    private String summa;
}
