package com.cscu9yw.eventregistrationbackend.controller;

import com.cscu9yw.eventregistrationbackend.model.EventRegistration;
import com.cscu9yw.eventregistrationbackend.model.User;
import com.cscu9yw.eventregistrationbackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.Set;

@RestController
@RequestMapping("/users")
@Tag(name = "Users")
public class UserController {
    private final UserService us;

    public UserController(UserService us) {
        this.us = us;
    }

    @GetMapping("/{userUid}")
    @Operation(summary = "Get user's details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "403", description = "URL parameter does not equal value of X-User-Uid header",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "user was was not found",
                    content = @Content(schema = @Schema(hidden = true)))
    })
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
    @Operation(summary = "Get all user's registrations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "403", description = "URL parameter does not equal value of X-User-Uid header",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "user was was not found",
                    content = @Content(schema = @Schema(hidden = true)))
    })
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
    @Operation(summary = "Request a new random user from user profile service and store it in the DB")
    @ApiResponse(responseCode = "201", description = "successful operation")
    public ResponseEntity<User> registerUser() {
        User user = us.registerNewUser();
        URI location = URI.create("/api/v1/users/" + user.getUid());
        return ResponseEntity.created(location).body(user);
    }
}
