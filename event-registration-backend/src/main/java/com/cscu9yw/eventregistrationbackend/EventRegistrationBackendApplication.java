package com.cscu9yw.eventregistrationbackend;

import com.cscu9yw.eventregistrationbackend.model.Event;
import com.cscu9yw.eventregistrationbackend.repository.EventRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


import java.time.LocalDateTime;
import java.util.Date;

@SpringBootApplication
public class EventRegistrationBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventRegistrationBackendApplication.class, args);
	}


	// The initDB() @Bean is run automatically on startup, before the
	// @RestController is started.
	// The body could access command line argument args (but doesn't).
	// The fact that initDB() requires a WelcomeService argument tells Spring
	// to auto-configure and pass a WelcomeService instance, which initDB()
	// amends by adding more Welcome instances.
	@Bean
	public CommandLineRunner initDB(EventRepository repository) {
		return (args) -> {
			repository.save(new Event("First Event", LocalDateTime.parse("2022-04-01T10:00:00"), 60, 30));
			repository.save(new Event("Second Event", LocalDateTime.parse("2022-04-02T12:45:00"), 90, 2));
			repository.save(new Event("Third Event", LocalDateTime.parse("2022-04-03T08:30:00"), 120, 45));
		};
	}

}
