package org.example.black_sea_walnut.service.imp;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.admin.transaction.ResponseTransactionForAccount;
import org.example.black_sea_walnut.dto.admin.transaction.ResponseTransactionForView;
import org.example.black_sea_walnut.dto.web.TransactionResponseForAccount;
import org.example.black_sea_walnut.dto.web.checkout.CheckoutUser;
import org.example.black_sea_walnut.entity.Transaction;
import org.example.black_sea_walnut.entity.User;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.mapper.TransactionMapper;
import org.example.black_sea_walnut.repository.TransactionsRepository;
import org.example.black_sea_walnut.service.TransactionsService;
import org.example.black_sea_walnut.service.specifications.TransactionSpecification;
import org.example.black_sea_walnut.util.LogUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImp implements TransactionsService {
    private final TransactionsRepository transactionsRepository;
    private final TransactionMapper mapper;

    @Override
    public List<Transaction> getAll() {
        LogUtil.logInfo("Fetching all transactions");
        return transactionsRepository.findAll();
    }

    @Override
    public Transaction getById(Long id) {
        LogUtil.logInfo("Fetching transaction by ID: " + id);
        return transactionsRepository.findById(id)
                .orElseThrow(() -> {
                    LogUtil.logError("Transaction not found with ID: " + id, null);
                    return new EntityNotFoundException("Transaction with id: " + id + " was not found!");
                });
    }

    @Override
    public ResponseTransactionForAccount getByIdInResponseForAccount(Long id) {
        LogUtil.logInfo("Fetching transaction for account response by ID: " + id);
        return mapper.toResponseForWeb(getById(id));
    }

    @Override
    public PageResponse<ResponseTransactionForView> getAll(ResponseTransactionForView response, Pageable pageable, LanguageCode code) {
        LogUtil.logInfo("Fetching transactions with filters: " + response + ", Page: " + pageable);
        Page<Transaction> page = transactionsRepository.findAll(TransactionSpecification.getSpecification(response), pageable);
        List<ResponseTransactionForView> responsesDtoAdd = page.map(mapper::toDTOView).stream().toList();
        return new PageResponse<>(responsesDtoAdd, new PageResponse.Metadata(
                page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages()
        ));
    }

    @Override
    public PageResponse<TransactionResponseForAccount> getAll(Pageable pageable, LanguageCode code) {
        LogUtil.logInfo("Fetching all transactions for accounts, Page: " + pageable);
        Specification<Transaction> spec = Specification.where(null);
        Page<Transaction> page = transactionsRepository.findAll(spec, pageable);
        List<TransactionResponseForAccount> responsesDtoAdd = page.map(mapper::toResponseForAccount).stream().toList();
        return new PageResponse<>(responsesDtoAdd, new PageResponse.Metadata(
                page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages()
        ));
    }

    @Override
    public PageResponse<TransactionResponseForAccount> getAll(User user, Pageable pageable, LanguageCode code) {
        LogUtil.logInfo("Fetching transactions for user: " + user.getId() + ", Page: " + pageable);
        Specification<Transaction> spec = Specification.where(TransactionSpecification.hasUser(user));
        Page<Transaction> page = transactionsRepository.findAll(spec, pageable);
        List<TransactionResponseForAccount> responsesDtoAdd = page.map(mapper::toResponseForAccount).stream().toList();
        return new PageResponse<>(responsesDtoAdd, new PageResponse.Metadata(
                page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages()
        ));
    }

    @Override
    public Transaction save(Transaction entity) {
        LogUtil.logInfo("Saving transaction: " + entity);
        return transactionsRepository.save(entity);
    }

    @Override
    public Transaction save(CheckoutUser dto) {
        LogUtil.logInfo("Saving transaction from CheckoutUser DTO: " + dto);
        return save(mapper.toMapperFromRequestCheckout(dto));
    }
}
