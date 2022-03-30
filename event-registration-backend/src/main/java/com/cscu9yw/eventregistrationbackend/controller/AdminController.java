package com.cscu9yw.eventregistrationbackend.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.cscu9yw.eventregistrationbackend.service.AdminService;
import com.cscu9yw.eventregistrationbackend.utils.JWTTokenValidator;
import com.cscu9yw.eventregistrationbackend.utils.OtherAuthExceptionHandler;
import com.cscu9yw.eventregistrationbackend.utils.TokenExpiredAuthExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final AdminService adminService;
    private final Environment environment;

    public AdminController(AdminService adminService, Environment environment) {
        this.adminService = adminService;
        this.environment = environment;
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // API path is not admin login EP, authorization header is present and starts with 'Bearer ' substring.
            try {
                // Verify refresh token validity and initiate a new authentication token.
                JWTTokenValidator validator = new JWTTokenValidator(authorizationHeader,environment, adminService);

                // Generate new JWT access token.
                String accessToken = JWT.create()
                        .withSubject(validator.getAdmin().getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 1000)) // 1 min
                        .withIssuer(request.getRequestURL().toString()) // Refresh token EP path
                        .sign(validator.getAlgorithm());

                // Put tokens to HashMap in order to return them as JSON response.
                Map<String, String> tokens = new HashMap<>();
                tokens.put("accessToken", accessToken);
                tokens.put("refreshToken", validator.getToken());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);

            } catch (TokenExpiredException exception) {
                // Handle expired tokens
                new TokenExpiredAuthExceptionHandler(exception.getMessage(), response);

            } catch (Exception exception) {
                // Handle other errors
                new OtherAuthExceptionHandler(exception.getMessage(), response);
            }
        } else {
            // API path is not login EP, authorization header is not present or doesn't start with 'Bearer ' substring.
            throw new RuntimeException("Refresh token is not present");
        }
    }
}
