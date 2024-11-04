package com.example.banca_digital.controller;

import com.example.banca_digital.models.Account;
import com.example.banca_digital.services.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AccountControllerTest {

    private final WebTestClient webTestClient;
    private final AccountService accountService;

    public AccountControllerTest() {
        this.accountService = mock(AccountService.class);
        this.webTestClient = WebTestClient
                .bindToController(new AccountController(accountService))
                .build();
    }

    @Test
    void createAccount() {

        Account account = new Account("1", "123", "Ahorro", List.of());
        when(accountService.createAccount(any(Account.class))).thenReturn(Mono.just(account));

        webTestClient.post()
                .uri("/accounts")
                .bodyValue(account)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Account.class);
        verify(accountService).createAccount(any(Account.class));

    }

    @Test
    void findAccountById() {

        Account account = new Account("1", "123", "Ahorro", List.of());
        when(accountService.findAccountById("1")).thenReturn(Mono.just(account));

        webTestClient.get()
                .uri("/accounts/{id}", "1")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Account.class);
        verify(accountService).findAccountById("1");
    }

    @Test
    void getAccountBalance() {
        when(accountService.getAccountBalance("1")).thenReturn(Mono.empty());
        webTestClient.get()
                .uri("/accounts/{id}/balance", "1")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Double.class);
        verify(accountService).getAccountBalance("1");

    }

    @Test
    void getTotalAndGeneralBalanceByUserId() {
        when(accountService.getTotalAndGeneralBalanceByUserId("1")).thenReturn(Mono.empty());
        webTestClient.get()
                .uri("/accounts/{userId}/balancestotal", "1")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Double.class);
        verify(accountService).getTotalAndGeneralBalanceByUserId("1");
    }

}
