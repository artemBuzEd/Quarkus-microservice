package com.ArtemBuzEd.payment;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private Long registrationId;
    private LocalDate CreatedAt = LocalDate.now();

    public void setUserId(String userId){
        this.userId = userId;
    }

    public void setRegistrationId(Long registrationId){
        this.registrationId = registrationId;
    }
}
