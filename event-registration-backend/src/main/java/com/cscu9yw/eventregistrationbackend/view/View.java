package com.cscu9yw.eventregistrationbackend.view;

import java.util.HashMap;
import java.util.Map;


public class View {
    public static final Map<String, Class<? extends User>> MAPPING = new HashMap<>();

    static {
        MAPPING.put("ADMIN", Admin.class);
        MAPPING.put("USER", User.class);
    }

    public static class User {
    }

    public static class Admin extends User {
    }


}
