package com.cscu9yw.eventregistrationbackend.service;

import com.cscu9yw.eventregistrationbackend.model.Event;

import java.util.List;
import java.util.Optional;

public interface EventService {

    List<Event> getAllEvents();

    Optional<Event> getEventById(Long id);

    boolean eventExists(Long id);

    void deleteEventById(Long id);

    Event addEvent(Event event);

    Event updateEvent(Event event);
}
