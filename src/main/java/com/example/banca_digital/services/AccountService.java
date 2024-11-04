package com.example.banca_digital.services;

import com.example.banca_digital.models.Account;
import com.example.banca_digital.respositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    private final UserService userService;


    private final String DEPOSITO = "depósito";

    public Mono<Account> createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Mono<Account> findAccountById(String id) {
        return accountRepository.findById(id);
    }

    public Mono<Double> getAccountBalance(String id) {
        return findAccountById(id)
                .map(account -> account.getTransactions().stream()
                        .mapToDouble(t -> t.getType().equals(DEPOSITO) ? t.getAmount() : -t.getAmount())
                        .sum());
    }


    public Mono<Map<String, Double>> getTotalAndGeneralBalanceByUserId(String userId) {
        return userService.findUserAccountsById(userId)
                .flatMapMany(Flux::fromIterable)
                .flatMap(this::getAccountBalance)
                .collectList()
                .map(balances -> {
                    double generalBalance = balances.stream().mapToDouble(Double::doubleValue).sum();
                    Map<String, Double> balanceMap = new HashMap<>();
                    balanceMap.put("balance general", generalBalance);
                    return balanceMap;
                });
    }


}
