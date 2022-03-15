package com.cscu9yw.eventregistrationbackend.service;

import com.cscu9yw.eventregistrationbackend.model.Event;
import com.cscu9yw.eventregistrationbackend.model.User;

public interface UserService {
    User registerNewUser();
    boolean userExists(String uid);
}
