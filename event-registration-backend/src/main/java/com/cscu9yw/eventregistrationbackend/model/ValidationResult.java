package com.cscu9yw.eventregistrationbackend.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ValidationResult {
    private List<EventRegistration> unchangedRegistrations = new ArrayList<>();
    private List<EventRegistration> updatedRegistrations = new ArrayList<>();
    private List<Map<String, Object>> deletedRegistrations = new ArrayList<>();

    public ValidationResult() {
    }

    public List<EventRegistration> getUnchangedRegistrations() {
        return unchangedRegistrations;
    }

    public void setUnchangedRegistrations(List<EventRegistration> unchangedRegistrations) {
        this.unchangedRegistrations = unchangedRegistrations;
    }

    public List<EventRegistration> getUpdatedRegistrations() {
        return updatedRegistrations;
    }

    public void setUpdatedRegistrations(List<EventRegistration> updatedRegistrations) {
        this.updatedRegistrations = updatedRegistrations;
    }

    public List<Map<String, Object>> getDeletedRegistrations() {
        return deletedRegistrations;
    }

    public void setDeletedRegistrations(List<Map<String, Object>> deletedRegistrations) {
        this.deletedRegistrations = deletedRegistrations;
    }
}
