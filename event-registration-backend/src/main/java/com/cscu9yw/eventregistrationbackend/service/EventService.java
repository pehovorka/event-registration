package com.cscu9yw.eventregistrationbackend.service;

import com.cscu9yw.eventregistrationbackend.model.Event;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventService {

    Set<Event> getAllEvents();
    Optional<Event> getEventById(Long id);
    boolean eventExists(Long id);
}
