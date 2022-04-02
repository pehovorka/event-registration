package com.cscu9yw.eventregistrationbackend.service;

import com.cscu9yw.eventregistrationbackend.model.Event;
import com.cscu9yw.eventregistrationbackend.repository.EventRegistrationRepository;
import com.cscu9yw.eventregistrationbackend.repository.EventRepository;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.*;


@Component
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventRegistrationRepository eventRegistrationRepository;

    public EventServiceImpl(EventRepository eventRepository, EventRegistrationRepository eventRegistrationRepository) {
        this.eventRepository = eventRepository;
        this.eventRegistrationRepository = eventRegistrationRepository;
    }

    public List<Event> getAllEvents() {
        return eventRepository.findByOrderByDateDesc();
    }

    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    public boolean eventExists(Long id) {
        return eventRepository.existsById(id);
    }

    @Transactional
    public void deleteEventById(Long id) {
        eventRegistrationRepository.deleteAllByEvent_Id(id);
        eventRepository.deleteById(id);
    }

    public Event addEvent(Event event) {
        return eventRepository.save(event);
    }

    public Event updateEvent(Event event) {
        return eventRepository.save(event);
    }
}
