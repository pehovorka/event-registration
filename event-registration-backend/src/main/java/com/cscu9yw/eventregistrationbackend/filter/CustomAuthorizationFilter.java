package com.cscu9yw.eventregistrationbackend.filter;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.cscu9yw.eventregistrationbackend.utils.JWTTokenValidator;
import com.cscu9yw.eventregistrationbackend.utils.OtherAuthExceptionHandler;
import com.cscu9yw.eventregistrationbackend.utils.TokenExpiredAuthExceptionHandler;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class CustomAuthorizationFilter extends OncePerRequestFilter {
    private final Environment environment;

    public CustomAuthorizationFilter(Environment environment) {
        this.environment = environment;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals("/api/v1/admin/login") || request.getServletPath().equals("/api/v1/admin/token/refresh")) {
            // API path is admin login EP or refresh token EP.
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                // API path is not admin login EP, authorization header is present and starts with 'Bearer ' substring.
                try {
                    // Verify JWT validity and initiate a new authentication token.
                    JWTTokenValidator validator = new JWTTokenValidator(authorizationHeader,environment);

                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(validator.getAdminUsername(), null, null);

                    // Set authentication in security context holder.
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                } catch (TokenExpiredException exception) {
                    // Handle expired tokens
                    new TokenExpiredAuthExceptionHandler(exception.getMessage(), response);

                } catch (Exception exception) {
                    // Handle other errors
                    new OtherAuthExceptionHandler(exception.getMessage(), response);
                }
            } else {
                // API path is not login EP, authorization header is not present or doesn't start with 'Bearer ' substring.
                filterChain.doFilter(request, response);
            }
        }
    }
}
