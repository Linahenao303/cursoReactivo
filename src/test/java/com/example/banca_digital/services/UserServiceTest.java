package com.example.banca_digital.services;

import com.example.banca_digital.models.User;
import com.example.banca_digital.respositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserServiceTest {


    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        Flux<User> user = Flux.just(
                new User("user123", "user1", "user123@exemple", "user1", List.of("account1", "account2")),
                new User("user124", "user2", "user124@exemple", "user2", List.of("account3", "account4"))
        );

        userRepository = mock(UserRepository.class);
        when(userRepository.findAll()).thenReturn(user);
        userService = new UserService(userRepository);
    }

    @Test
    void findUserAccountsById() {
        String userId = "user123";
        List<String> accounts = List.of("account1", "account2");
        User user = new User("user123", "user1", "user123@exemple", "user1", accounts);
        user.setAccounts(accounts);

        when(userRepository.findById(userId)).thenReturn(Mono.just(user));

        Mono<List<String>> result = userService.findUserAccountsById(userId);

        StepVerifier.create(result)
                .assertNext(x -> assertEquals(accounts, x))
                .verifyComplete();
        verify(userRepository).findById(userId);
    }

    @Test
    void registerUser() {
        User user = new User("user123", "user1", "user123@exemple", "user1", List.of("account1", "account2"));
        when(userRepository.save(user)).thenReturn(Mono.just(user));
        Mono<User> result = userService.registerUser(user);

        StepVerifier.create(result)
                .assertNext(t -> {
                    assertEquals("user123", t.getId());
                    assertEquals("user1", t.getName());
                })
                .verifyComplete();
        verify(userRepository).save(user);
    }

    @Test
    void findAllUsers_returnsAllUsers() {
        Flux<User> result = userService.findAllUsers();
        StepVerifier.create(result)
                .assertNext(user -> assertEquals("user123", user.getId()))
                .assertNext(user -> assertEquals("user124", user.getId()))
                .verifyComplete();
        verify(userRepository).findAll();
    }
}