package com.example.banca_digital.services;

import com.example.banca_digital.models.Account;
import com.example.banca_digital.models.Transaction;
import com.example.banca_digital.respositories.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class AccountServiceTest {


    private AccountRepository accountRepository;


    private UserService userService;


    private AccountService accountService;

    @BeforeEach
    void setUp() {
        String accountId = "account123";
        Mono<Account> account = Mono.just(new Account("1", "123", "Ahorro", List.of()));

        accountRepository = mock(AccountRepository.class);
        when(accountRepository.findById(accountId)).thenReturn(account);
        userService = mock(UserService.class);
        accountService = new AccountService(accountRepository, userService);

    }

    @Test
    void createAccount() {
        Account account = new Account("1", "123", "Ahorro", List.of());
        when(accountRepository.save(account)).thenReturn(Mono.just(account));

        Mono<Account> result = accountService.createAccount(account);

        StepVerifier.create(result)
                .expectNext(account)
                .verifyComplete();
        verify(accountRepository).save(account);
    }

    @Test
    void findAccountById() {

        Mono<Account> account = accountService.findAccountById("account123");
        StepVerifier.create(account)
                .assertNext(x -> {
                    assertEquals("1", x.getId());
                    assertEquals("123", x.getNumber());
                    assertEquals("Ahorro", x.getType());
                    assertEquals(List.of(), x.getTransactions());
                })
                .verifyComplete();
        verify(accountRepository).findById("account123");
    }

    @Test
    void getAccountBalance() {
        String accountId = "account123";
        Account account = new Account(  "1", "123", "Ahorro", List.of());
        account.setTransactions(List.of(
                new Transaction("1","depósito", 100.0),
                new Transaction("2","retiro", 50.0)
        ));

        when(accountRepository.findById(accountId)).thenReturn(Mono.just(account));

        Mono<Double> result = accountService.getAccountBalance(accountId);

        StepVerifier.create(result)
                .expectNext(50.0)
                .verifyComplete();
        verify(accountRepository).findById(accountId);
    }

    @Test
    void getTotalAndGeneralBalanceByUserId() {
        String userId = "user123";
        List<String> accounts = List.of("account1", "account2");
        Account account1 = new Account( "1", "123", "Ahorro", List.of());
        account1.setTransactions(List.of(new Transaction("1","depósito", 100.0)));
        Account account2 = new Account( "2", "124", "Ahorro", List.of());
        account2.setTransactions(List.of(new Transaction("2","depósito", 200.0)));

        when(userService.findUserAccountsById(userId)).thenReturn(Mono.just(accounts));
        when(accountRepository.findById("account1")).thenReturn(Mono.just(account1));
        when(accountRepository.findById("account2")).thenReturn(Mono.just(account2));

        Mono<Map<String, Double>> result = accountService.getTotalAndGeneralBalanceByUserId(userId);

        StepVerifier.create(result)
                .expectNextMatches(map -> map.get("balance general") == 300.0)
                .verifyComplete();



    }
}