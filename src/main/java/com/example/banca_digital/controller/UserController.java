package com.example.banca_digital.controller;

import com.example.banca_digital.models.User;
import com.example.banca_digital.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<User> registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @GetMapping
    public Flux<User> findAllUsers() {
        return userService.findAllUsers();

    }
}

