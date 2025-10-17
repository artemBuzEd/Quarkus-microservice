package com.artembuzed.registration;

import java.util.Date;

public class Registration {
    public Long id;
    public final String userId;
    public final String status;
    public final String eventId;
    public final Date registeredAt = new Date();

    public Registration(String userId, String status, String eventId) {
        this.userId = userId;
        this.status = status;
        this.eventId = eventId;
    }

    public boolean idIsValid(Long id) {
        return this.id.equals(id);
    }

    public boolean statusIsRegistered() {
        return this.status.equals("Registered");
    }
}
