package com.cscu9yw.eventregistrationbackend.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Interest {
    @ManyToMany(mappedBy = "interests")
    Set<User> users = new HashSet<>();

    @Id
    private String name;

    public Interest(String name) {
        this.name = name;
    }

    protected Interest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Interest{" +
                ", name='" + name +
                '}';
    }
}
