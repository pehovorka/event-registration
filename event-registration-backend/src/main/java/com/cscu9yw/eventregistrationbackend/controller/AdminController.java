package com.cscu9yw.eventregistrationbackend.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.cscu9yw.eventregistrationbackend.model.AuthException;
import com.cscu9yw.eventregistrationbackend.model.JWTTokens;
import com.cscu9yw.eventregistrationbackend.model.UsernamePasswordRequest;
import com.cscu9yw.eventregistrationbackend.service.AdminService;
import com.cscu9yw.eventregistrationbackend.utils.JWTTokenValidator;
import com.cscu9yw.eventregistrationbackend.utils.OtherAuthExceptionHandler;
import com.cscu9yw.eventregistrationbackend.utils.TokenExpiredAuthExceptionHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin")
public class AdminController {

    private final AdminService adminService;
    private final Environment environment;

    public AdminController(AdminService adminService, Environment environment) {
        this.adminService = adminService;
        this.environment = environment;
    }

    @PostMapping("/login")
    @Operation(summary = "Log-in a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "401", description = "username or password is incorrect",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    public JWTTokens login(@RequestBody UsernamePasswordRequest request) {
        // This method is only for API documentation, Spring Security intercepts POST /login and does verification.
        throw new NotImplementedException();
    }


    @GetMapping("/token/refresh")
    @Operation(summary = "Get a new access token")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "401", description = "refresh token is expired",
                    content = @Content(schema = @Schema(allOf = AuthException.class))),
    })
    public JWTTokens refreshToken(@Parameter(description = "refresh token") @RequestHeader(AUTHORIZATION) String authorizationHeader,
                                  HttpServletRequest request,
                                  HttpServletResponse response)
            throws IOException, TokenExpiredAuthExceptionHandler, OtherAuthExceptionHandler {

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // API path is not admin login EP, authorization header is present and starts with 'Bearer ' substring.
            try {
                // Verify refresh token validity and initiate a new authentication token.
                JWTTokenValidator validator = new JWTTokenValidator(authorizationHeader, environment, adminService);

                // Generate new JWT access token.
                String accessToken = JWT.create()
                        .withSubject(validator.getAdmin().getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 1000)) // 1 min
                        .withIssuer(request.getRequestURL().toString()) // Refresh token EP path
                        .sign(validator.getAlgorithm());

                return new JWTTokens(accessToken, validator.getToken());

            } catch (TokenExpiredException exception) {
                // Handle expired tokens
                throw new TokenExpiredAuthExceptionHandler(exception.getMessage(), response);

            } catch (Exception exception) {
                // Handle other errors
                throw new OtherAuthExceptionHandler(exception.getMessage(), response);
            }
        } else {
            // API path is not login EP, authorization header is not present or doesn't start with 'Bearer ' substring.
            throw new RuntimeException("Refresh token is not present");
        }
    }
}
