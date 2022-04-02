package com.cscu9yw.eventregistrationbackend.service;

import com.cscu9yw.eventregistrationbackend.model.Event;
import com.cscu9yw.eventregistrationbackend.model.EventRegistration;
import com.cscu9yw.eventregistrationbackend.model.ValidationResult;

import java.util.Optional;


public interface EventRegistrationService {
    EventRegistration register(Long eventId, String userUid);

    void deleteRegistration(Long eventId, String userUid);

    boolean userExists(String userUid);

    boolean eventExists(Long eventId);

    boolean registrationExists(String userUid, Long eventId);

    boolean eventIsFull(Long eventId);

    Optional<EventRegistration> getRegistrationById(Long eventId, String userUid);

    ValidationResult validateUsers(Event event);
}
