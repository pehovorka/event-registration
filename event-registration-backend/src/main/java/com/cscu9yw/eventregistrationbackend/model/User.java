package com.cscu9yw.eventregistrationbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class User {
    @Id
    private String uid;
    private String name;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Interest> interests;

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties("user")
    private Set<EventRegistration> registrations = new HashSet<>();

    public User(String uid, String name, Set<Interest> interests) {
        this.uid = uid;
        this.name = name;
        this.interests = interests;
    }

    protected User() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Interest> getInterests() {
        return interests;
    }

    public void setInterests(Set<Interest> interests) {
        this.interests = interests;
    }

    public Set<EventRegistration> getRegistrations() {
        return registrations;
    }

    public void setRegistrations(Set<EventRegistration> registrations) {
        this.registrations = registrations;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", interests=" + interests +
                '}';
    }
}
