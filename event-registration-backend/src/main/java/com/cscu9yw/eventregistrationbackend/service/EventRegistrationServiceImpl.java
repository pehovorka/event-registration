package com.cscu9yw.eventregistrationbackend.service;

import com.cscu9yw.eventregistrationbackend.model.Event;
import com.cscu9yw.eventregistrationbackend.model.EventRegistration;
import com.cscu9yw.eventregistrationbackend.model.EventRegistrationKey;
import com.cscu9yw.eventregistrationbackend.model.User;
import com.cscu9yw.eventregistrationbackend.repository.EventRegistrationRepository;
import com.cscu9yw.eventregistrationbackend.repository.EventRepository;
import com.cscu9yw.eventregistrationbackend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;


@Component
public class EventRegistrationServiceImpl implements EventRegistrationService {

    private final EventRegistrationRepository eventRegistrationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public EventRegistrationServiceImpl(EventRegistrationRepository eventRegistrationRepository, EventRepository eventRepository, UserRepository userRepository) {
        this.eventRegistrationRepository = eventRegistrationRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    public EventRegistration register(Long eventId, String userUid) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException("Event not found: " + eventId));
        User user = userRepository.findByUid(userUid).orElseThrow(() -> new EntityNotFoundException("User not found: " + userUid));

        EventRegistration registration = eventRegistrationRepository.save(new EventRegistration(user, event, LocalDateTime.now()));
        event.setRegistered(event.getRegistered() + 1);
        eventRepository.save(event);
        return registration;
    }

    @Transactional
    public void deleteRegistration(Long eventId, String userUid) {
        EventRegistrationKey key = new EventRegistrationKey(userUid, eventId);
        eventRepository.findById(eventId).ifPresent(event -> event.setRegistered(event.getRegistered() - 1));
        eventRegistrationRepository.deleteById(key);
    }

    ;

    public boolean userExists(String userUid) {
        return userRepository.existsByUid(userUid);
    }

    ;

    public boolean eventExists(Long eventId) {
        return eventRepository.existsById(eventId);
    }

    ;

    public boolean registrationExists(String userUid, Long eventId) {
        EventRegistrationKey key = new EventRegistrationKey(userUid, eventId);
        return eventRegistrationRepository.existsById(key);
    }

    public boolean eventIsFull(Long eventId){
        return eventRepository.findById(eventId).get().getRegistered() == eventRepository.findById(eventId).get().getCapacity();
    }
}
