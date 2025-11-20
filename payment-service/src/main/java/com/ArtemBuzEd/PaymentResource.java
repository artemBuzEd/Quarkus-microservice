package com.ArtemBuzEd;

import com.ArtemBuzEd.payment.Payment;
import com.ArtemBuzEd.payment.PaymentRepository;
import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;

@Path("payment")
public class PaymentResource {

    @Inject
    PaymentRepository paymentRepository;

    public PaymentResource(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @Path("createPayment/{userId}/{registrationId}")
    public Payment createPayment(String userId, Long registrationId) {
        Log.infof("Creating payment for user %s with registration %s", userId, registrationId);
        try{
            Payment payment = new Payment();
            payment.setUserId(userId);
            payment.setRegistrationId(registrationId);
            paymentRepository.persist(payment);
            Log.infof("Successfully created payment for user %s with registration %s", userId, registrationId);
            return payment;
        }catch (Exception e){
            Log.warn("ERROR CREATING PAYMENT!!!");
            Log.error(e);
        }

        return null;
    }
}
