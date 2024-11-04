package com.example.banca_digital.services;

import com.example.banca_digital.models.User;
import com.example.banca_digital.respositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Mono<User> registerUser(User user) {
        return userRepository.save(user);
    }

    public Flux<User> findAllUsers() {
        return userRepository.findAll();

    }

    public Mono<List<String>> findUserAccountsById(String id) {
        return userRepository.findById(id)
                .map(User::getAccounts);
    }

}
