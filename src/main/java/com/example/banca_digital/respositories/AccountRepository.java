package com.example.banca_digital.respositories;

import com.example.banca_digital.models.Account;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface AccountRepository extends ReactiveMongoRepository<Account, String> {
}
