package com.cscu9yw.eventregistrationbackend.controller;

import com.cscu9yw.eventregistrationbackend.model.Event;
import com.cscu9yw.eventregistrationbackend.model.ValidationResult;
import com.cscu9yw.eventregistrationbackend.service.EventRegistrationService;
import com.cscu9yw.eventregistrationbackend.service.EventService;
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
import java.util.List;


@RestController
@RequestMapping("/events")
@Tag(name = "Events")
public class EventController {
    private final EventService eventService;
    private final EventRegistrationService eventRegistrationService;

    public EventController(EventService eventService, EventRegistrationService eventRegistrationService) {
        this.eventService = eventService;
        this.eventRegistrationService = eventRegistrationService;
    }

    @GetMapping()
    @Operation(summary = "Get all events")
    @ApiResponse(responseCode = "200", description = "successful operation")
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    @PostMapping()
    @Operation(summary = "Create a new event")
    @ApiResponse(responseCode = "201", description = "event was successfully created")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Event> addEvent(@RequestBody Event eventRequest) {

        Event newEvent = eventService.addEvent(eventRequest);
        URI location = URI.create("/api/v1/events/" + eventRequest.getId());

        return ResponseEntity.created(location).body(newEvent);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update event by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "event was successfully updated"),
            @ApiResponse(responseCode = "404", description = "event was was not found",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "bearerAuth")
    public Event updateEvent(@RequestBody Event eventRequest, @PathVariable Long id) {
        return eventService.getEventById(id)
                .map(event -> {
                    event.setName(eventRequest.getName());
                    event.setCapacity(eventRequest.getCapacity());
                    event.setDate(eventRequest.getDate());
                    event.setDuration(eventRequest.getDuration());
                    return eventService.updateEvent(event);
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    }

    @GetMapping("/{id}")
    @Operation(summary = "Get event by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "404", description = "event was was not found",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    public Event getEventById(@PathVariable Long id) {
        return eventService
                .getEventById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete event by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "404", description = "event was was not found",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "bearerAuth")
    public void deleteEventById(@PathVariable Long id) {
        if (eventService.eventExists(id)) {
            eventService.deleteEventById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{id}/validate-users")
    @Operation(summary = "Validate attendees of an event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "404", description = "event was was not found",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "bearerAuth")
    public ValidationResult validateUsers(@PathVariable Long id) {
        Event event = eventService.
                getEventById(id).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return eventRegistrationService.validateUsers(event);
    }
}
