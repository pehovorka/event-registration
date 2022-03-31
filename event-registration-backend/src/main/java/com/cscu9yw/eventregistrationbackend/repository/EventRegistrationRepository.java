package com.cscu9yw.eventregistrationbackend.repository;

import com.cscu9yw.eventregistrationbackend.model.EventRegistration;
import com.cscu9yw.eventregistrationbackend.model.EventRegistrationKey;
import org.springframework.data.repository.CrudRepository;


public interface EventRegistrationRepository extends CrudRepository<EventRegistration, Long> {
    boolean existsById(EventRegistrationKey key);
    void deleteById(EventRegistrationKey key);
}
