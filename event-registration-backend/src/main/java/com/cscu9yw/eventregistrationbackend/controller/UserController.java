package com.cscu9yw.eventregistrationbackend.controller;

import com.cscu9yw.eventregistrationbackend.model.Event;
import com.cscu9yw.eventregistrationbackend.model.EventRegistration;
import com.cscu9yw.eventregistrationbackend.model.User;
import com.cscu9yw.eventregistrationbackend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService us;

    public UserController(UserService us) {
        this.us = us;
    }

    @GetMapping("/{userUid}")
    public ResponseEntity<User> getUser(@PathVariable String userUid,
                                        @RequestHeader("X-User-Uid") String userUidHeader) {
        if (!userUid.equals(userUidHeader))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        if (!us.userExists(userUid))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        User user = us.getUser(userUid);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/{userUid}/registrations")
    public ResponseEntity<Set<EventRegistration>> getUserRegistrations(@PathVariable String userUid,
                                                                       @RequestHeader("X-User-Uid") String userUidHeader) {
        if (!userUid.equals(userUidHeader))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        if (!us.userExists(userUid))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        Set<EventRegistration> registrations = us.getUserRegistrations(userUid);
        return ResponseEntity.ok().body(registrations);
    }

    @PostMapping()
    public ResponseEntity<User> registerUser() {
        User user = us.registerNewUser();
        URI location = URI.create("/api/v1/users/" + user.getUid());
        return ResponseEntity.created(location).body(user);
    }
}
