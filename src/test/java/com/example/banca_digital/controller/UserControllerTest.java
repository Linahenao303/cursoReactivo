package com.example.banca_digital.controller;

import com.example.banca_digital.models.User;
import com.example.banca_digital.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

 class UserControllerTest {

    private UserService userService;

    private WebTestClient webTestClient;

    public UserControllerTest() {
        this.userService = mock(UserService.class);
        this.webTestClient = WebTestClient
                .bindToController(new UserController(userService)).build();
    }

    @Test
    void registerUser() {
        User user = new User("user123", "user1", "user123@exemple", "user1", List.of("account1", "account2"));
        when(userService.registerUser(any(User.class))).thenReturn(Mono.just(user));

        webTestClient.post()
                .uri("/users")
                .bodyValue(user)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(User.class);
                verify(userService).registerUser(any(User.class));
    }

    @Test
    void findAllUsers() {

        when(userService.findAllUsers())
                .thenReturn(Flux.just(new User("user123", "user1", "user123@exemple", "user1", List.of("account1", "account2"))));

        webTestClient.get()
                .uri("/users")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(User.class)
                .hasSize(1)
                .value(users -> {
                    User user = users.get(0);
                    assertEquals("user123", user.getId());
                    assertEquals("user1", user.getName());
                    assertEquals("user123@exemple", user.getEmail());
                    assertEquals("user1", user.getAddress());
                    assertEquals(List.of("account1", "account2"), user.getAccounts());
                });
        verify(userService).findAllUsers();
    }
}

