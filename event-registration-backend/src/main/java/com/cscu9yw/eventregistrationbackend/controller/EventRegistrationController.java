package com.cscu9yw.eventregistrationbackend.controller;

import com.cscu9yw.eventregistrationbackend.model.EventRegistration;
import com.cscu9yw.eventregistrationbackend.model.User;
import com.cscu9yw.eventregistrationbackend.service.EventRegistrationService;
import com.cscu9yw.eventregistrationbackend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("api/v1/registrations")
@CrossOrigin
public class EventRegistrationController {
    private final EventRegistrationService ers;

    public EventRegistrationController(EventRegistrationService ers) {
        this.ers = ers;
    }

    @PostMapping()
    public ResponseEntity<EventRegistration> register(@RequestBody EventRegistration registrationRequest,
                                                      @RequestHeader("X-User-Uid") String userUidHeader) {
        Long eventId = registrationRequest.getEvent().getId();
        String userUid = registrationRequest.getUser().getUid();

        if (!userUid.equals(userUidHeader)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        if (!ers.eventExists(eventId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event with id: " + eventId + " does not exist!");
        }
        if (!ers.userExists(userUid)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with uid: " + userUid + " does not exist!");
        }
        if (ers.registrationExists(userUid, eventId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Registration already exists!");
        }
        if (ers.eventIsFull(eventId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Registrations are full!");
        }

        EventRegistration registration = ers.register(eventId, userUid);

        URI location = URI.create("/api/v1/registrations/" + registration.getId());
        return ResponseEntity.created(location).body(registration);
    }

    @DeleteMapping()
    public void deleteRegistration(@RequestBody EventRegistration registrationRequest,
                                   @RequestHeader("X-User-Uid") String userUidHeader) {
        Long eventId = registrationRequest.getEvent().getId();
        String userUid = registrationRequest.getUser().getUid();

        if (!userUid.equals(userUidHeader)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        if (!ers.registrationExists(userUid, eventId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Registration doesn't exist!");
        }
        ers.deleteRegistration(eventId,userUid);
    }
}
