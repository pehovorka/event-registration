package com.cscu9yw.eventregistrationbackend.repository;

import com.cscu9yw.eventregistrationbackend.model.EventRegistration;
import com.cscu9yw.eventregistrationbackend.model.EventRegistrationKey;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface EventRegistrationRepository extends CrudRepository<EventRegistration, Long> {
    boolean existsById(EventRegistrationKey key);
    void deleteById(EventRegistrationKey key);
    Optional<EventRegistration> findEventRegistrationById(EventRegistrationKey key);
    void deleteAllByEvent_Id(Long eventId);
}
