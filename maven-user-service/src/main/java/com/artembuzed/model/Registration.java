package com.artembuzed.model;

import java.util.Date;

public class Registration {
    public Long id;
    public String userId;
    public String status;
    public String eventId;
    public Date registeredAt;

    public Long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getStatus() {
        return status;
    }

    public String getEventId() {
        return eventId;
    }

    public Date getRegisteredAt() {
        return registeredAt;
    }
}
