package com.cscu9yw.eventregistrationbackend.controller;

import com.cscu9yw.eventregistrationbackend.model.Event;
import com.cscu9yw.eventregistrationbackend.model.User;
import com.cscu9yw.eventregistrationbackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {
    private final UserService us;

    public UserController(UserService us) {
        this.us = us;
    }

    @PostMapping()
    public ResponseEntity<User> registerUser() {
        User user = us.registerNewUser();
        URI location = URI.create("/users/"+user.getUid());
        return ResponseEntity.created(location).body(user);
    }
}
