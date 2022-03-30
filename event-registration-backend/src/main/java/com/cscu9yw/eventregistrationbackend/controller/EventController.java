package com.cscu9yw.eventregistrationbackend.controller;

import com.cscu9yw.eventregistrationbackend.model.Event;
import com.cscu9yw.eventregistrationbackend.service.EventService;
import com.cscu9yw.eventregistrationbackend.view.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@RequestMapping("/api/v1/events")
public class EventController {
    private final EventService es;

    public EventController(EventService es) {
        this.es = es;
    }

    @JsonView(Views.Public.class)
    @GetMapping()
    public List<Event> getAllEvents() {
        return es.getAllEvents();
    }

    @GetMapping("/{id}")
    public Event getEventById(@PathVariable Long id) {
        return es
                .getEventById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    }
}
