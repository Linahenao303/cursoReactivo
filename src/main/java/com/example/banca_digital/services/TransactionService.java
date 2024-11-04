package com.example.banca_digital.services;

import com.example.banca_digital.models.Account;
import com.example.banca_digital.models.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;


@Service
@RequiredArgsConstructor
public class TransactionService {

    private final AccountService accountService;

    private final String RETIRO = "retiro";

    private final String DEPOSITO = "depósito";

    public Mono<Transaction> createTransaction(Transaction transaction, String accountId) {
        return accountService.findAccountById(accountId)
                .flatMap(account -> {
                    if (transaction.getType().equals(RETIRO) && getAccountBalance(account) < transaction.getAmount()) {
                        return Mono.error(new IllegalArgumentException("Insufficient funds"));
                    }
                    account.getTransactions().add(transaction);
                    return accountService.createAccount(account).thenReturn(transaction);
                });
    }

    public double getAccountBalance(Account account) {
        return account.getTransactions().stream()
                .mapToDouble(t -> t.getType().equals(DEPOSITO) ? t.getAmount() : -t.getAmount())
                .sum();
    }

    public Mono<List<Transaction>> getWithdrawals(String accountId) {
        return accountService.findAccountById(accountId)
                .map(account -> List.copyOf(account.getTransactions()
                        .stream()
                        .filter(transaction -> transaction.getType().equals(RETIRO))
                        .toList()));
    }

    public Mono<List<Transaction>> getDeposits(String accountId) {
        return accountService.findAccountById(accountId)
                .map(account -> List.copyOf(account.getTransactions()
                        .stream()
                        .filter(transaction -> transaction.getType().equals(DEPOSITO))
                        .toList()));
    }


    public Mono<List<Transaction>> getAllTransactions(String accountId) {
        return accountService.findAccountById(accountId)
                .map(account -> List.copyOf(account.getTransactions()))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Account not found")));
    }

}
