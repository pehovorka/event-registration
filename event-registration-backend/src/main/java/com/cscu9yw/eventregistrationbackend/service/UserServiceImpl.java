package com.cscu9yw.eventregistrationbackend.service;

import com.cscu9yw.eventregistrationbackend.model.Event;
import com.cscu9yw.eventregistrationbackend.model.RandomUserResponse;
import com.cscu9yw.eventregistrationbackend.model.User;
import com.cscu9yw.eventregistrationbackend.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.Set;

@Component
public class UserServiceImpl implements UserService {
    private final UserRepository db;
    public UserServiceImpl(UserRepository db) {
        this.db = db;
    }

    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(15);

    @Bean
    private WebClient randomUserService() {
        return WebClient.create("https://pmaier.eu.pythonanywhere.com/random-user");
    }

    public User registerNewUser() {
        RandomUserResponse randomUserResponse = randomUserService().get()
                .accept(MediaType.APPLICATION_JSON).retrieve()
                .bodyToMono(RandomUserResponse.class).block(REQUEST_TIMEOUT);

        User user = db.save(randomUserResponse.getUser());
        return user;
    }

    public boolean userExists(String uid) {
        return db.existsByUid(uid);
    }

}
