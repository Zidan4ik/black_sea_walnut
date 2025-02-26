package org.example.black_sea_walnut.service;

import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.transaction.ResponseTransactionForView;
import org.example.black_sea_walnut.dto.web.TransactionResponseForAccount;
import org.example.black_sea_walnut.entity.Transaction;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TransactionsService {
    List<Transaction> getAll();

    PageResponse<ResponseTransactionForView> getAll(ResponseTransactionForView response, Pageable pageable, LanguageCode code);

    PageResponse<TransactionResponseForAccount> getAll(Pageable pageable, LanguageCode code);
}
