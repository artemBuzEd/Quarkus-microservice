package com.artembuzed.registration.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

import java.util.Date;

@Entity
public class Registration extends PanacheEntity {
    public String userId;
    public String status;
    public String eventId;
    public Date registeredAt = new Date();


    public boolean idIsValid(Long id) {
        return this.id.equals(id);
    }

    public boolean statusIsRegistered() {
        return this.status.equals("Registered");
    }

    @Override
    public String toString() {
        return "Registration{" +
                "Id=" + id +
                ", userId='" + userId + '\'' +
                ", status=" + status +
                ", eventId=" + eventId +
                ", registeredAt=" + registeredAt +
                '}';
    }
}
