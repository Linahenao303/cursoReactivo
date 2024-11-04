package com.example.banca_digital.controller;

import com.example.banca_digital.models.Transaction;
import com.example.banca_digital.services.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


 class TransactionControllerTest {

    private final WebTestClient webTestClient;

    private final TransactionService transactionService;

    public TransactionControllerTest() {
        this.transactionService = mock(TransactionService.class);
        this.webTestClient = WebTestClient
                .bindToController(new TransactionController(transactionService))
                .build();
    }

    @Test
    void createTransaction_createsTransactionSuccessfully() {
        Transaction transaction = new Transaction("1", "retiro", 50.0);
        when(transactionService.createTransaction(any(Transaction.class),eq("account123"))).thenReturn(Mono.just(transaction));

        webTestClient.post()
                .uri("/transactions/account123")
                .bodyValue(transaction)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Transaction.class);
    }

    @Test
    void getWithdrawals() {

        when(transactionService.getWithdrawals(("account123"))).thenReturn(Mono.empty());
        webTestClient.get()
                .uri("/transactions/account123/withdrawals")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Transaction.class);
    }

    @Test
    void getDeposits() {

        when(transactionService.getDeposits(("account123"))).thenReturn(Mono.empty());
        webTestClient.get()
                .uri("/transactions/account123/deposits")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Transaction.class);
    }

    @Test
    void getAllTransactions() {

        when(transactionService.getAllTransactions(("account123"))).thenReturn(Mono.empty());
        webTestClient.get()
                .uri("/transactions/account123")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Transaction.class);
    }
}