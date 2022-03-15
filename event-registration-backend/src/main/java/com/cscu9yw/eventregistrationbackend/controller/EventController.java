package com.cscu9yw.eventregistrationbackend.controller;

import com.cscu9yw.eventregistrationbackend.model.Event;
import com.cscu9yw.eventregistrationbackend.service.EventService;
import com.cscu9yw.eventregistrationbackend.view.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/events")
@CrossOrigin
public class EventController {
    private final EventService es;
    public EventController(EventService es) {
        this.es = es;
    }

    @JsonView(Views.Public.class)
    @GetMapping()
    public List<Event> getAllEvents() {
        List<Event> events = es.getAllEvents();
        return events;
    }

    @GetMapping("/{id}")
    public Optional<Event> getEventById(@PathVariable Long id){
        Optional<Event> event = es.getEventById(id);
        if (event.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return event;
    }
}
