package com.ArtemBuzEd.payment;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PaymentRepository implements PanacheRepository<Payment> {
    public List<Payment> findByUserId(String userId) {
        return list("userId", userId);
    }
}
