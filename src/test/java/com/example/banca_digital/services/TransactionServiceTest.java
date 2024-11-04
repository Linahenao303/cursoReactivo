package com.example.banca_digital.services;

import com.example.banca_digital.models.Account;
import com.example.banca_digital.models.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    private AccountService accountService;

    private TransactionService transactionService;


    @BeforeEach
    void setUp() {
        accountService = mock(AccountService.class);
        transactionService = new TransactionService(accountService);
    }

    @Test
    void createTransaction() {
        Transaction transaction = new Transaction("1", "depósito", 300.0);
        Account account = new Account("1", "123", "Ahorro", new ArrayList<>());

        when(accountService.findAccountById("1")).thenReturn(Mono.just(account));
        when(accountService.createAccount(any(Account.class))).thenReturn(Mono.just(account));

        Mono<Transaction> result = transactionService.createTransaction(transaction, "1");

        StepVerifier.create(result)
                .expectNext(transaction)
                .verifyComplete();
        verify(accountService).findAccountById("1");
        verify(accountService).createAccount(any(Account.class));

    }
    @Test
    void getAccountBalance() {
        List<Transaction> transactions = List.of(
                new Transaction("1", "depósito", 300.0),
                new Transaction("2", "depósito", 200.0),
                new Transaction("3", "retiro", 200.0)
        );
        Account account = new Account("1", "123", "Ahorro", transactions);

        double balance = transactionService.getAccountBalance(account);

        assertEquals(300.0, balance);
    }

    @Test
    void getWithdrawals() {
        List<Transaction> withdrawals = List.of(
                new Transaction("3", "retiro", 200.0)
        );
        Account account = new Account("1", "123", "Ahorro", withdrawals);

        when(accountService.findAccountById("1")).thenReturn(Mono.just(account));

        Mono<List<Transaction>> result = transactionService.getWithdrawals("1");

        StepVerifier.create(result)
                .assertNext(x -> assertEquals(withdrawals, x))
                .verifyComplete();
        verify(accountService).findAccountById("1");
    }

    @Test
    void getDeposits() {
        List<Transaction> deposits = List.of(
                new Transaction("1", "depósito", 300.0),
                new Transaction("2", "depósito", 200.0)
        );
        Account account = new Account("1", "123", "Ahorro", deposits);

        when(accountService.findAccountById("1")).thenReturn(Mono.just(account));

        Mono<List<Transaction>> result = transactionService.getDeposits("1");

        StepVerifier.create(result)
                .assertNext(x -> assertEquals(deposits, x))
                .verifyComplete();
        verify(accountService).findAccountById("1");
    }

    @Test
    void getAllTransactions() {
        List<Transaction> transactions = List.of(
                new Transaction("1", "depósito", 300.0),
                new Transaction("2", "depósito", 200.0),
                new Transaction("3", "retiro", 200.0)
        );
        Account account = new Account("1", "123", "Ahorro", transactions);

        when(accountService.findAccountById("1")).thenReturn(Mono.just(account));

        Mono<List<Transaction>> result = transactionService.getAllTransactions("1");

        StepVerifier.create(result)
                .assertNext(x -> assertEquals(transactions, x))
                .verifyComplete();
        verify(accountService).findAccountById("1");
    }
}