package com.cscu9yw.eventregistrationbackend.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cscu9yw.eventregistrationbackend.model.Admin;
import com.cscu9yw.eventregistrationbackend.service.AdminService;
import org.springframework.core.env.Environment;

import java.util.Objects;

public class JWTTokenValidator {
    private final String token;
    private final Algorithm algorithm;
    private final String adminUsername;
    private Admin admin = null;


    public JWTTokenValidator(String authorizationHeader, Environment environment, AdminService adminService) {
        this.token = authorizationHeader.substring("Bearer ".length());
        this.algorithm = Algorithm.HMAC256(Objects.requireNonNull(environment.getProperty("security.secret")).getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        this.adminUsername = decodedJWT.getSubject();
        this.admin = adminService.getAdmin(this.adminUsername);
    }

    public JWTTokenValidator(String authorizationHeader, Environment environment) {
        this.token = authorizationHeader.substring("Bearer ".length());
        this.algorithm = Algorithm.HMAC256(Objects.requireNonNull(environment.getProperty("security.secret")).getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        this.adminUsername = decodedJWT.getSubject();
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public String getAdminUsername() {
        return adminUsername;
    }

    public String getToken() {
        return token;
    }

    public Admin getAdmin() {
        return admin;
    }
}
