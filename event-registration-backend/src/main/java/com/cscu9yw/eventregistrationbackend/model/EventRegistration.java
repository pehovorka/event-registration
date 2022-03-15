package com.cscu9yw.eventregistrationbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class EventRegistration {
    @EmbeddedId
    private EventRegistrationKey id = new EventRegistrationKey();

    @ManyToOne
    @MapsId("userUid")
    @JoinColumn(name = "user_uid")
    @JsonIgnoreProperties("registrations")
    private User user;

    @ManyToOne
    @MapsId("eventId")
    @JoinColumn(name = "event_id")
    @JsonIgnoreProperties("registrations")
    private Event event;
    private LocalDateTime registeredAt;

    public EventRegistration(User user, Event event, LocalDateTime registeredAt) {
        this.user = user;
        this.event = event;
        this.registeredAt = registeredAt;
    }

    public EventRegistration(User user, Event event) {
        this.user = user;
        this.event = event;
        this.registeredAt = LocalDateTime.now();
    }

    protected EventRegistration() {
    }

    public EventRegistrationKey getId() {
        return id;
    }

    public void setId(EventRegistrationKey id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(LocalDateTime registeredAt) {
        this.registeredAt = registeredAt;
    }

    @Override
    public String toString() {
        return "EventRegistration{" +
                "id=" + id +
                ", user=" + user +
                ", event=" + event +
                ", registeredAt=" + registeredAt +
                '}';
    }
}
