package com.cscu9yw.eventregistrationbackend.controller;

import com.cscu9yw.eventregistrationbackend.model.EventRegistration;
import com.cscu9yw.eventregistrationbackend.model.EventRegistrationKey;
import com.cscu9yw.eventregistrationbackend.service.EventRegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;

@RestController
@RequestMapping("/registrations")
@Tag(name = "Registrations")
public class EventRegistrationController {
    private final EventRegistrationService ers;

    public EventRegistrationController(EventRegistrationService ers) {
        this.ers = ers;
    }

    @PostMapping()
    @Operation(summary = "Create a new registration")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "registration is created"),
            @ApiResponse(responseCode = "403", description = "userUid in request body does not equal value of X-User-Uid header",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "user or event was was not found",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "409", description = "registration already exists or registrations are full",
                    content = @Content(schema = @Schema(hidden = true))),
    })
    public ResponseEntity<EventRegistration> register(
            @RequestBody EventRegistrationKey registrationRequest,
            @RequestHeader("X-User-Uid") String userUidHeader) {
        Long eventId = registrationRequest.getEventId();
        String userUid = registrationRequest.getUserUid();

        if (!userUid.equals(userUidHeader))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        if (!ers.eventExists(eventId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event with id: " + eventId + " does not exist!");
        if (!ers.userExists(userUid))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with uid: " + userUid + " does not exist!");
        if (ers.registrationExists(userUid, eventId))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Registration already exists!");
        if (ers.eventIsFull(eventId))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Registrations are full!");


        EventRegistration registration = ers.register(eventId, userUid);

        URI location = URI.create("/api/v1/registrations/" + registration.getUser().getUid() + "/" + registration.getEvent().getId());
        return ResponseEntity.created(location).body(registration);
    }

    @GetMapping("/{userUid}/{eventId}")
    @Operation(summary = "Get registration by user ID and event ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "404", description = "registration was not found",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "bearerAuth")
    public EventRegistration getRegistration(@PathVariable Long eventId, @PathVariable String userUid) {

        return ers.getRegistrationById(eventId, userUid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping()
    @Operation(summary = "Delete registration")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "registration is deleted"),
            @ApiResponse(responseCode = "403", description = "userUid in request body does not equal value of X-User-Uid header",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "user or event was was not found",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    public void deleteRegistration(@RequestBody EventRegistrationKey registrationRequest,
                                   @RequestHeader("X-User-Uid") String userUidHeader) {
        Long eventId = registrationRequest.getEventId();
        String userUid = registrationRequest.getUserUid();

        if (!userUid.equals(userUidHeader))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        if (!ers.registrationExists(userUid, eventId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Registration doesn't exist!");

        ers.deleteRegistration(eventId, userUid);
    }
}
