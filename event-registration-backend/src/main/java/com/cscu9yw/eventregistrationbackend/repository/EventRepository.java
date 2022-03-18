package com.cscu9yw.eventregistrationbackend.repository;

import com.cscu9yw.eventregistrationbackend.model.Event;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EventRepository extends CrudRepository<Event, Long> {
    List<Event> findByOrderByDateDesc();
}
