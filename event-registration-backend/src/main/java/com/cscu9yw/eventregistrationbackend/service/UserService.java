package com.cscu9yw.eventregistrationbackend.service;

import com.cscu9yw.eventregistrationbackend.model.EventRegistration;
import com.cscu9yw.eventregistrationbackend.model.User;

import java.util.Set;

public interface UserService {
    User registerNewUser();
    User getUser(String uid);
    Set<EventRegistration> getUserRegistrations(String uid);
    boolean userExists(String uid);
}
