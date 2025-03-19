package org.example.black_sea_walnut.mapper;

import org.example.black_sea_walnut.dto.admin.transaction.ResponseTransactionForAccount;
import org.example.black_sea_walnut.dto.admin.transaction.ResponseTransactionForView;
import org.example.black_sea_walnut.dto.web.TransactionResponseForAccount;
import org.example.black_sea_walnut.dto.web.checkout.CheckoutUser;
import org.example.black_sea_walnut.entity.Transaction;
import org.example.black_sea_walnut.enums.PaymentStatus;
import org.example.black_sea_walnut.enums.PaymentType;
import org.example.black_sea_walnut.util.DateUtil;
import org.springframework.stereotype.Component;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
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

    public ResponseTransactionForAccount toResponseForWeb(Transaction entity) {
        DateTimeFormatter outputDateTime = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        return ResponseTransactionForAccount
                .builder()
                .id(entity.getId())
                .customer(entity.getCustomer())
                .summa(String.valueOf(entity.getSumma()))
                .phone(entity.getPhone())
                .email(entity.getEmail())
                .paymentType(entity.getPaymentType().toString())
                .paymentStatus(entity.getPaymentStatus().toString())
                .date(entity.getDate().format(outputDateTime))
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

    public Transaction toMapperFromRequestCheckout(CheckoutUser dto) {
        Transaction entity = new Transaction();
        entity.setCustomer(dto.getFio());
        entity.setDate(LocalDateTime.now());
        entity.setSumma(Math.toIntExact(dto.getTotalAmount()));
        entity.setPhone(dto.getPhone());
        entity.setEmail(dto.getEmail());
        PaymentType paymentType = PaymentType.fromString(dto.getTypeOfPayment());
        entity.setPaymentType(paymentType);
        boolean isCard = paymentType.equals(PaymentType.card);
        if (isCard) {
            entity.setPaymentStatus(PaymentStatus.payed);
        } else {
            entity.setPaymentStatus(PaymentStatus.unPayed);
        }
        entity.setUser(dto.getUser());
        return entity;
    }
}
