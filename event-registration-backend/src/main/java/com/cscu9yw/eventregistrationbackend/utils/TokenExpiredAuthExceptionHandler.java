package com.cscu9yw.eventregistrationbackend.utils;

import com.cscu9yw.eventregistrationbackend.model.AuthException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class TokenExpiredAuthExceptionHandler extends Throwable {

    public TokenExpiredAuthExceptionHandler(String message, HttpServletResponse response) throws IOException {
        response.setStatus(UNAUTHORIZED.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        AuthException exception = new AuthException("TOKEN_EXPIRED", message);
        new ObjectMapper().writeValue(response.getOutputStream(), exception);
    }

}
