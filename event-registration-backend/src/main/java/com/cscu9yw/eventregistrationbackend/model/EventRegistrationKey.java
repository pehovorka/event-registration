package com.cscu9yw.eventregistrationbackend.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class EventRegistrationKey implements Serializable {

    @Column(name = "user_uid")
    private String userUid;

    @Column(name = "event_id")
    private Long eventId;

    public EventRegistrationKey(String userUid, Long eventId) {
        this.userUid = userUid;
        this.eventId = eventId;
    }

    protected EventRegistrationKey() {
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventRegistrationKey that = (EventRegistrationKey) o;
        return userUid.equals(that.userUid) && eventId.equals(that.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userUid, eventId);
    }

    @Override
    public String toString() {
        return userUid + "___" + eventId;
    }
}