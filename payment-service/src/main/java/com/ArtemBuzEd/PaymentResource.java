package com.ArtemBuzEd;

import com.ArtemBuzEd.payment.Payment;
import io.quarkus.logging.Log;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;

@Path("payment")
public class PaymentResource {
    private final AtomicLong id = new AtomicLong();

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("createPayment/{userId}/{registrationId}")
    public Payment createPayment(String userId, Long registrationId) {
        Log.infof("Creating payment for user %s with registration %s", userId, registrationId);
        return new Payment(id.incrementAndGet(), userId, registrationId, LocalDate.now());
    }
}
