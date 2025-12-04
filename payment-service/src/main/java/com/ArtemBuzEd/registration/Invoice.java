package com.ArtemBuzEd.registration;

public class Invoice {
    public long registrationId;
    public String userId;
    public String status;
    public double price;

    public Invoice(long registrationId, String userId, String status, double price) {
        this.registrationId = registrationId;
        this.userId = userId;
        this.status = status;
        this.price = price;
    }

    public Invoice() {}
}
