package com.example.banca_digital.controller;

import com.example.banca_digital.models.Account;
import com.example.banca_digital.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor

public class AccountController {
    private final AccountService accountService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Account> createAccount(@RequestBody Account account) {
        return accountService.createAccount(account);
    }

    @GetMapping("/{id}")
    public Mono<Account> findAccountById(@PathVariable String id) {
        return accountService.findAccountById(id);
    }

    //**Consultar el balance total de una cuenta.**

    @GetMapping("/{id}/balance")
    public Mono<Double> getAccountBalance(@PathVariable String id) {
        return accountService.getAccountBalance(id);
    }

    //** Consultar un balance total y general de todas las cuentas que tiene el usuario.

    @GetMapping("/{userId}/balancestotal")
    public Mono<Map<String, Double>> getTotalAndGeneralBalanceByUserId(@PathVariable String userId) {
        return accountService.getTotalAndGeneralBalanceByUserId(userId);
    }

}
