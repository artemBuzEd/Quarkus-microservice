package com.ArtemBuzEd.registration;

import com.ArtemBuzEd.payment.Payment;
import com.ArtemBuzEd.payment.PaymentRepository;
import io.quarkus.logging.Log;
import io.smallrye.common.annotation.Blocking;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Incoming;

public class PaymentProcessor {

    @Inject
    private PaymentRepository paymentRepository;

    public PaymentProcessor(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Incoming("invoice-processing")
    @Blocking
    @Transactional
    public void processPayment(Invoice invoice) {
        Log.infof("Creating payment for user %s with registration %s", invoice.userId, invoice.registrationId);
        try{
            Payment payment = new Payment();
            payment.setUserId(invoice.userId);
            payment.setRegistrationId(invoice.registrationId);
            payment.setAmount(invoice.price);
            paymentRepository.persist(payment);
            Log.infof("Successfully created payment for user %s with registration %s", invoice.userId, invoice.registrationId);
        }catch (Exception e){
            Log.warn("ERROR CREATING PAYMENT!!!");
            Log.error(e);
        }
    }
}
