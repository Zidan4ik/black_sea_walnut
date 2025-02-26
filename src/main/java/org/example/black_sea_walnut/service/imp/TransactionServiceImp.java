package org.example.black_sea_walnut.service.imp;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.transaction.ResponseTransactionForView;
import org.example.black_sea_walnut.dto.web.OrderResponseForAccount;
import org.example.black_sea_walnut.dto.web.TransactionResponseForAccount;
import org.example.black_sea_walnut.entity.Order;
import org.example.black_sea_walnut.entity.Transaction;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.mapper.TransactionMapper;
import org.example.black_sea_walnut.repository.TransactionsRepository;
import org.example.black_sea_walnut.service.TransactionsService;
import org.example.black_sea_walnut.service.specifications.TransactionSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImp implements TransactionsService {
    private final TransactionsRepository transactionsRepository;
    private final TransactionMapper mapper = new TransactionMapper();

    @Override
    public List<Transaction> getAll() {
        return transactionsRepository.findAll();
    }

    @Override
    public PageResponse<ResponseTransactionForView> getAll(ResponseTransactionForView response, Pageable pageable, LanguageCode code) {
        Page<Transaction> page = transactionsRepository.findAll(TransactionSpecification.getSpecification(response), pageable);
        List<ResponseTransactionForView> responsesDtoAdd = page.map(mapper::toDTOView).stream().toList();
        return new PageResponse<>(responsesDtoAdd, new PageResponse.Metadata(
                page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages()
        ));
    }

    @Override
    public PageResponse<TransactionResponseForAccount> getAll(Pageable pageable, LanguageCode code) {
        Specification<Transaction> spec = Specification.where(null);
        Page<Transaction> page = transactionsRepository.findAll(spec,pageable);
        List<TransactionResponseForAccount> responsesDtoAdd = page.map(mapper::toResponseForAccount).stream().toList();
        return new PageResponse<>(responsesDtoAdd, new PageResponse.Metadata(
                page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages()
        ));
    }
}
