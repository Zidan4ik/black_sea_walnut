package org.example.black_sea_walnut.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResponseTransactionForView {
    private Long id;
    private String customer;
    private String date;
    private String time;
    private String summa;
    private String phone;
    private String email;
    private String typeOfPayment;
    private String statusPayment;
}
