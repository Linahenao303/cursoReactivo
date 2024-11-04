package com.example.banca_digital.controller;

import com.example.banca_digital.models.Transaction;
import com.example.banca_digital.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;


@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {


    private final TransactionService transactionService;

    @PostMapping("/{accountId}")
    public Mono<Transaction> createTransaction(@RequestBody Transaction transaction, @PathVariable String accountId) {
        return transactionService.createTransaction(transaction, accountId);
    }

    //***Consultar todos los retiros que se han hecho en una cuenta.
    @GetMapping("/{accountId}/withdrawals")
    public Mono<List<Transaction>> getWithdrawals(@PathVariable String accountId) {
        return transactionService.getWithdrawals(accountId);
    }

    //***Consultar todos los depósitos que se han hecho en una cuenta.
    @GetMapping("/{accountId}/deposits")
    public Mono<List<Transaction>> getDeposits(@PathVariable String accountId) {
        return transactionService.getDeposits(accountId);
    }


   //*** Consultar un resumen de todas las transacciones realizadas en una cuenta.
    @GetMapping("/{accountId}")
    public Mono<List<Transaction>> getAllTransactions(@PathVariable String accountId) {
        return transactionService.getAllTransactions(accountId);
    }
}
