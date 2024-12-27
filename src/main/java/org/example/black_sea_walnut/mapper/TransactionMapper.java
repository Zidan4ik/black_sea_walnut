package org.example.black_sea_walnut.mapper;

import org.example.black_sea_walnut.dto.ResponseTransactionForView;
import org.example.black_sea_walnut.entity.Transaction;


import java.time.format.DateTimeFormatter;

public class TransactionMapper {
    public ResponseTransactionForView toDTOView(Transaction entity) {
        DateTimeFormatter outputDate = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DateTimeFormatter outputTime = DateTimeFormatter.ofPattern("HH:mm:ss");
        return ResponseTransactionForView
                .builder()
                .id(entity.getId())
                .customer(entity.getCustomer())
                .summa(String.valueOf(entity.getSumma()))
                .phone(entity.getPhone())
                .email(entity.getEmail())
                .typeOfPayment(entity.getPaymentType().toString())
                .statusPayment(entity.getPaymentStatus().toString())
                .date(entity.getDate().toLocalDate().format(outputDate))
                .time(entity.getDate().toLocalTime().format(outputTime))
                .build();
    }
}
