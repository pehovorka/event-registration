package com.cscu9yw.eventregistrationbackend;

import com.cscu9yw.eventregistrationbackend.model.Admin;
import com.cscu9yw.eventregistrationbackend.model.Event;
import com.cscu9yw.eventregistrationbackend.repository.EventRepository;
import com.cscu9yw.eventregistrationbackend.service.AdminService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.security.OAuthFlows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

import static io.swagger.v3.oas.annotations.enums.SecuritySchemeIn.HEADER;

@SpringBootApplication
public class EventRegistrationBackendApplication {

    @Autowired
    private Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(EventRegistrationBackendApplication.class, args);
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner initDB(EventRepository eventRepository, AdminService adminService) {
        return (args) -> {
            eventRepository.save(new Event("First Event", LocalDateTime.parse("2022-04-01T10:00:00"), 60, 5));
            eventRepository.save(new Event("Second Event", LocalDateTime.parse("2022-04-02T12:45:00"), 90, 2));
            eventRepository.save(new Event("Third Event", LocalDateTime.parse("2022-04-03T08:30:00"), 120, 8));
            eventRepository.save(new Event("Spring Boot for n00bs", LocalDateTime.parse("2022-04-02T10:00:00"), 240, 10));
            eventRepository.save(new Event("What's new in Next.js 12", LocalDateTime.parse("2022-04-06T10:00:00"), 120, 50));
            adminService.saveAdmin(new Admin(environment.getProperty("admin.username"), environment.getProperty("admin.password")));
        };
    }

    // API docs config
    @Configuration
    @OpenAPIDefinition(info = @Info(title = "Event registration API", version = "v1"))
    @SecurityScheme(
            name = "bearerAuth",
            type = SecuritySchemeType.HTTP,
            scheme = "bearer",
            bearerFormat = "JWT",
            in = HEADER
    )
    public static class OpenApi30Config {
    }

}
