package org.example.black_sea_walnut.dto.transaction;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResponseTransactionForAccount {
    private Long id;
    private String customer;
    private String date;
    private String phone;
    private String email;
    private String summa;
    private String paymentType;
    private String paymentStatus;
}
