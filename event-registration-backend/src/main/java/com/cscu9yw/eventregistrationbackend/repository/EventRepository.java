package com.cscu9yw.eventregistrationbackend.repository;

import com.cscu9yw.eventregistrationbackend.model.Event;
import org.springframework.data.repository.CrudRepository;

public interface EventRepository extends CrudRepository<Event, Long> {
}
