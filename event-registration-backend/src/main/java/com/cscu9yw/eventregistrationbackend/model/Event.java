package com.cscu9yw.eventregistrationbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private LocalDateTime date;
    private int duration;
    private int capacity;
    private int registered;


    @OneToMany(mappedBy = "event")
    @JsonIgnoreProperties("event")
    private Set<EventRegistration> registrations = new HashSet<>();

    public Event(String name, LocalDateTime date, int duration, int capacity) {
        this.name = name;
        this.date = date;
        this.duration = duration;
        this.capacity = capacity;
    }

    protected Event() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Set<EventRegistration> getRegistrations() {
        return registrations;
    }

    public void setRegistrations(Set<EventRegistration> registrations) {
        this.registrations = registrations;
    }

    public int getRegistered() {
        return registered;
    }

    public void setRegistered(int registered) {
        this.registered = registered;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", duration=" + duration +
                ", capacity=" + capacity +
                '}';
    }
}
