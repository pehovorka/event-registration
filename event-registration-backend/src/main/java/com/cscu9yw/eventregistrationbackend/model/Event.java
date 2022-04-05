package com.cscu9yw.eventregistrationbackend.model;

import com.cscu9yw.eventregistrationbackend.view.View;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String location;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime date;
    private int duration;
    private int capacity;

    // Registrations array is available only for authenticated users – admins.
    @JsonView(View.Admin.class)
    @OneToMany(mappedBy = "event")
    @OrderBy("registeredAt DESC")
    private Set<EventRegistration> registrations = new HashSet<>();

    public Event(String name, String location, ZonedDateTime date, int duration, int capacity) {
        this.name = name;
        this.location = location;
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

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
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
        return registrations.size();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
