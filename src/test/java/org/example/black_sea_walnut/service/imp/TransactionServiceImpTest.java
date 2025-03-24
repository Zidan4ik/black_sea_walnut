package org.example.black_sea_walnut.service.imp;


import jakarta.persistence.EntityNotFoundException;
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
import org.example.black_sea_walnut.service.specifications.TransactionSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImpTest {
    @Mock
    private TransactionsRepository transactionsRepository;

    @Mock
    private TransactionMapper mapper;

    @InjectMocks
    private TransactionServiceImp transactionService;

    private Transaction transaction;
    private User user;
    private Pageable pageable;
    private ResponseTransactionForView responseTransactionForView;
    private ResponseTransactionForAccount responseTransactionForAccount;
    private TransactionResponseForAccount transactionResponseForAccount;
    private CheckoutUser checkoutUser;

    @BeforeEach
    void setUp() {
        transaction = new Transaction();
        transaction.setId(1L);

        user = new User();
        user.setId(1L);

        pageable = PageRequest.of(0, 10, Sort.by("id"));

        responseTransactionForView = ResponseTransactionForView.builder().build();
        responseTransactionForAccount = ResponseTransactionForAccount.builder().build();
        transactionResponseForAccount = TransactionResponseForAccount.builder().build();

        checkoutUser = new CheckoutUser();
    }

    @Test
    void testGetAll() {
        when(transactionsRepository.findAll()).thenReturn(List.of(transaction));

        List<Transaction> result = transactionService.getAll();

        assertEquals(1, result.size());
        assertEquals(transaction, result.get(0));
        verify(transactionsRepository, times(1)).findAll();
    }

    @Test
    void testGetById_TransactionExists() {
        when(transactionsRepository.findById(1L)).thenReturn(Optional.of(transaction));

        Transaction result = transactionService.getById(1L);

        assertNotNull(result);
        assertEquals(transaction, result);
        verify(transactionsRepository, times(1)).findById(1L);
    }

    @Test
    void testGetById_TransactionNotFound() {
        when(transactionsRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> transactionService.getById(1L));

        assertEquals("Transaction with id: 1 was not found!", exception.getMessage());
        verify(transactionsRepository, times(1)).findById(1L);
    }

    @Test
    void testGetByIdInResponseForAccount() {
        when(transactionsRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(mapper.toResponseForWeb(transaction)).thenReturn(responseTransactionForAccount);

        ResponseTransactionForAccount result = transactionService.getByIdInResponseForAccount(1L);

        assertNotNull(result);
        assertEquals(responseTransactionForAccount, result);
        verify(transactionsRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAll_WithResponseTransactionForView() {
        Page<Transaction> page = new PageImpl<>(List.of(transaction), pageable, 1);
        when(transactionsRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);
        when(mapper.toDTOView(transaction)).thenReturn(responseTransactionForView);

        PageResponse<ResponseTransactionForView> result = transactionService.getAll(responseTransactionForView, pageable, LanguageCode.en);

        assertEquals(1, result.getContent().size());
        verify(transactionsRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void testGetAll_ForAccounts() {
        Page<Transaction> page = new PageImpl<>(List.of(transaction), pageable, 1);
        when(transactionsRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);
        when(mapper.toResponseForAccount(transaction)).thenReturn(transactionResponseForAccount);

        PageResponse<TransactionResponseForAccount> result = transactionService.getAll(pageable, LanguageCode.en);

        assertEquals(1, result.getContent().size());
        verify(transactionsRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void testGetAll_ForUser() {
        Page<Transaction> page = new PageImpl<>(List.of(transaction), pageable, 1);
        when(transactionsRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);
        when(mapper.toResponseForAccount(transaction)).thenReturn(transactionResponseForAccount);

        PageResponse<TransactionResponseForAccount> result = transactionService.getAll(user, pageable, LanguageCode.en);

        assertEquals(1, result.getContent().size());
        verify(transactionsRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void testSave_TransactionEntity() {
        when(transactionsRepository.save(transaction)).thenReturn(transaction);

        Transaction savedTransaction = transactionService.save(transaction);

        assertNotNull(savedTransaction);
        assertEquals(transaction, savedTransaction);
        verify(transactionsRepository, times(1)).save(transaction);
    }

    @Test
    void testSave_CheckoutUser() {
        when(mapper.toMapperFromRequestCheckout(checkoutUser)).thenReturn(transaction);
        when(transactionsRepository.save(transaction)).thenReturn(transaction);

        Transaction savedTransaction = transactionService.save(checkoutUser);

        assertNotNull(savedTransaction);
        assertEquals(transaction, savedTransaction);
        verify(transactionsRepository, times(1)).save(transaction);
    }
}