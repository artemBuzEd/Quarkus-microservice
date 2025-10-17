package com.artembuzed.payment;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("payment")
@RegisterRestClient(baseUri = "http://localhost:8082")
public interface PaymentClient {
    @POST
    @Path("createPayment/{userId}/{registrationId}")
    Payment createPayment(String userId, Long registrationId);
}
