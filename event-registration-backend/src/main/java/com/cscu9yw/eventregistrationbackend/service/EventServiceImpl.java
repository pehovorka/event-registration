package com.cscu9yw.eventregistrationbackend.service;

import com.cscu9yw.eventregistrationbackend.model.Event;
import com.cscu9yw.eventregistrationbackend.repository.EventRepository;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
public class EventServiceImpl implements EventService {

    private final EventRepository db;

    public EventServiceImpl(EventRepository db) {
        this.db = db;
    }

    public List<Event> getAllEvents() {
        return db.findByOrderByDateDesc();
    }

    public Optional<Event> getEventById(Long id) {
        return db.findById(id);
    }

    public boolean eventExists(Long id) {
        return db.existsById(id);
    }

    public void deleteEventById(Long id) {
        db.deleteById(id);
    }
}
