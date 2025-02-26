package org.example.black_sea_walnut.mapper;

import org.example.black_sea_walnut.dto.transaction.ResponseTransactionForView;
import org.example.black_sea_walnut.dto.web.TransactionResponseForAccount;
import org.example.black_sea_walnut.entity.Transaction;
import org.example.black_sea_walnut.util.DateUtil;


import java.time.LocalDate;
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

    public TransactionResponseForAccount toResponseForAccount(Transaction entity) {
        return TransactionResponseForAccount
                .builder()
                .id(entity.getId())
                .date(DateUtil.toFormatDateFromDB(LocalDate.from(entity.getDate()), "dd.MM.yyyy"))
                .summa(String.valueOf(entity.getSumma()))
                .status(entity.getPaymentStatus().toString())
                .build();
    }
}
