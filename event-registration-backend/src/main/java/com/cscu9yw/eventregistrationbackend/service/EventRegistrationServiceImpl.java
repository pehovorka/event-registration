package com.cscu9yw.eventregistrationbackend.service;

import com.cscu9yw.eventregistrationbackend.model.*;
import com.cscu9yw.eventregistrationbackend.repository.EventRegistrationRepository;
import com.cscu9yw.eventregistrationbackend.repository.EventRepository;
import com.cscu9yw.eventregistrationbackend.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Component
public class EventRegistrationServiceImpl implements EventRegistrationService {

    private final EventRegistrationRepository eventRegistrationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public EventRegistrationServiceImpl(EventRegistrationRepository eventRegistrationRepository, EventRepository eventRepository, UserRepository userRepository) {
        this.eventRegistrationRepository = eventRegistrationRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    public EventRegistration register(Long eventId, String userUid) {
        Event event = eventRepository
                .findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found: " + eventId));
        User user = userRepository
                .findByUid(userUid)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userUid));

        EventRegistration registration = eventRegistrationRepository.save(new EventRegistration(user, event, LocalDateTime.now()));
        eventRepository.save(event);
        return registration;
    }

    @Transactional
    public void deleteRegistration(Long eventId, String userUid) {
        EventRegistrationKey key = new EventRegistrationKey(userUid, eventId);
        eventRegistrationRepository.deleteById(key);
    }

    ;

    public boolean userExists(String userUid) {
        return userRepository.existsByUid(userUid);
    }

    public boolean eventExists(Long eventId) {
        return eventRepository.existsById(eventId);
    }


    public boolean registrationExists(String userUid, Long eventId) {
        EventRegistrationKey key = new EventRegistrationKey(userUid, eventId);
        return eventRegistrationRepository.existsById(key);
    }

    public boolean eventIsFull(Long eventId) {
        return eventRepository
                .findById(eventId)
                .map((event) -> event.getRegistered() == event.getCapacity())
                .orElseThrow(() -> new EntityNotFoundException("Event with id: " + eventId + " not found!"));
    }


    @Bean
    private WebClient externalUserService() {
        return WebClient.create("https://pmaier.eu.pythonanywhere.com/user");
    }

    public ValidationResult validateUsers(Event event) {
        final Duration REQUEST_TIMEOUT = Duration.ofSeconds(15);
        ValidationResult validationResult = new ValidationResult();

        Set<EventRegistration> registrations = event.getRegistrations();

        for (EventRegistration registration : registrations) {
            try {
                RandomUserResponse externalUserResponse = externalUserService()
                        .get()
                        .uri("/" + registration.getUser().getUid())
                        .accept(MediaType.APPLICATION_JSON).retrieve()
                        .bodyToMono(RandomUserResponse.class).block(REQUEST_TIMEOUT);
                if (externalUserResponse == null) throw new RuntimeException("User is null");
                if (externalUserResponse.getUser().getInterests().equals(registration.getUser().getInterests())) {
                    // User's interests are the same
                    List<EventRegistration> unchangedRegistrations = validationResult.getUnchangedRegistrations();
                    unchangedRegistrations.add(registration);
                    validationResult.setUnchangedRegistrations(unchangedRegistrations);
                } else {
                    // User's interests are changed
                    registration.getUser().setInterests(externalUserResponse.getUser().getInterests());
                    userRepository.save(registration.getUser());

                    List<EventRegistration> updatedRegistrations = validationResult.getUpdatedRegistrations();
                    updatedRegistrations.add(registration);
                    validationResult.setUpdatedRegistrations(updatedRegistrations);
                }
            } catch (WebClientResponseException.NotFound error) {
                // User is deleted

                List<Map<String, Object>> deletedRegistrations = validationResult.getDeletedRegistrations();

                try {
                    // Serialize object to JSON and create a new Map. Original nested object cannot be used because
                    // it is being deleted.

                    ObjectMapper objectMapper = new ObjectMapper()
                            .registerModule(new JavaTimeModule())
                            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

                    String deletedRegistration = objectMapper.writeValueAsString(registration);
                    Map<String, Object> clonedDeletedRegistration = objectMapper.readValue(deletedRegistration
                            , new TypeReference<>() {
                            });
                    deletedRegistrations.add(clonedDeletedRegistration);
                    validationResult.setDeletedRegistrations(deletedRegistrations);
                } catch (JsonProcessingException jsonProcessingException) {
                    System.out.println("Error while serializing to JSON: " + jsonProcessingException.getMessage());
                }

                // Delete all user's registrations
                eventRegistrationRepository.deleteAll(registration.getUser().getRegistrations());
                // Delete user
                userRepository.delete(registration.getUser());

            } catch (Exception error) {
                throw new RuntimeException(error.getMessage());
            }
        }
        return validationResult;
    }
}
