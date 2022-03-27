package com.cscu9yw.eventregistrationbackend.service;

import com.cscu9yw.eventregistrationbackend.model.Admin;

public interface AdminService {
    Admin getAdmin(String username);
    Admin saveAdmin(Admin admin);
}
