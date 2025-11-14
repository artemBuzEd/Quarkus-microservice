package com.artembuzed.registration;

import java.util.Date;

public class Registration {
    public Long id;
    public String userId;
    public String status;
    public String eventId;
    public Date registeredAt = new Date();

    public Registration() {
        this.registeredAt = new Date();
    }
    public Registration(String status, String eventId, String userId) {
        this.userId = userId;
        this.status = status;
        this.eventId = eventId;
        this.registeredAt = new Date();
    }

    public boolean idIsValid(Long id) {
        return this.id.equals(id);
    }

    public boolean statusIsRegistered() {
        return this.status.equals("Registered");
    }
}
