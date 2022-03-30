package com.cscu9yw.eventregistrationbackend.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class OtherAuthExceptionHandler {
    public OtherAuthExceptionHandler(String message, HttpServletResponse response) throws IOException {
        response.setStatus(UNAUTHORIZED.value());
        Map<String, String> error = new HashMap<>();
        error.put("cause", "OTHER");
        error.put("error_message", message);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), error);
    }
}
