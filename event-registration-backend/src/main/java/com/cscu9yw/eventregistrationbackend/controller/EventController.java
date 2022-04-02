package com.cscu9yw.eventregistrationbackend.controller;

import com.cscu9yw.eventregistrationbackend.model.Event;
import com.cscu9yw.eventregistrationbackend.model.EventRegistration;
import com.cscu9yw.eventregistrationbackend.model.ValidationResult;
import com.cscu9yw.eventregistrationbackend.service.EventRegistrationService;
import com.cscu9yw.eventregistrationbackend.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/api/v1/events")
public class EventController {
    private final EventService eventService;
    private final EventRegistrationService eventRegistrationService;

    public EventController(EventService eventService, EventRegistrationService eventRegistrationService) {
        this.eventService = eventService;
        this.eventRegistrationService = eventRegistrationService;
    }

    @GetMapping()
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    @PostMapping()
    public ResponseEntity<Event> addEvent(@RequestBody Event eventRequest) {

        Event newEvent = eventService.addEvent(eventRequest);
        URI location = URI.create("/api/v1/events/" + eventRequest.getId());

        return ResponseEntity.created(location).body(newEvent);
    }

    @PutMapping("/{id}")
    public Event updateEvent(@RequestBody Event eventRequest, @PathVariable Long id) {
        return eventService.getEventById(id)
                .map(event -> {
                    event.setName(eventRequest.getName());
                    event.setCapacity(eventRequest.getCapacity());
                    event.setDate(eventRequest.getDate());
                    event.setCapacity(eventRequest.getCapacity());
                    return eventService.updateEvent(event);
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    }

    @GetMapping("/{id}")
    public Event getEventById(@PathVariable Long id) {
        return eventService
                .getEventById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public void deleteEventById(@PathVariable Long id) {
        if (eventService.eventExists(id)) {
            eventService.deleteEventById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{id}/validate-users")
    public ValidationResult validateUsers(@PathVariable Long id) {
        Event event = eventService.
                getEventById(id).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return eventRegistrationService.validateUsers(event);
    }
}
