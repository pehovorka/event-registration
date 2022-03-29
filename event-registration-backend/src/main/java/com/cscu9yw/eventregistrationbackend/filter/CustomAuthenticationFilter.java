package com.cscu9yw.eventregistrationbackend.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.security.auth.message.AuthException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final Environment environment;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, Environment environment) {
        this.authenticationManager = authenticationManager;
        this.environment = environment;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // Map 'username' and 'password' properties from request body JSON.
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(request.getInputStream());
            String username = jsonNode.has("username") ? jsonNode.get("username").asText() : null;
            String password = jsonNode.has("password") ? jsonNode.get("password").asText() : null;

            // Initiate a new authentication token.
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authentication) throws IOException, ServletException {
        User adminUser = (User) authentication.getPrincipal();
        Algorithm algorithm =
                Algorithm.HMAC256(Objects.requireNonNull(environment.getProperty("security.secret")).getBytes());

        // Generate new JWT tokens.
        String accessToken = JWT.create()
                .withSubject(adminUser.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1 * 60 * 1000)) // 1 minute
                .withIssuer(request.getRequestURL().toString()) // Admin login EP path
                .sign(algorithm);
        String refreshToken = JWT.create()
                .withSubject(adminUser.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 120 * 60 * 1000)) // 120 minutes
                .withIssuer(request.getRequestURL().toString()) // Admin login EP path
                .sign(algorithm);

        // Put tokens to HashMap in order to return them as JSON response.
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }
}
