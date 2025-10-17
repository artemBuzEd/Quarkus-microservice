package com.artembuzed.rest;

import com.artembuzed.CheckUserExistsRequest;
import com.artembuzed.CheckUserExistsRequestOrBuilder;
import com.artembuzed.CheckUserExistsResponse;
import com.artembuzed.UserService;
import com.artembuzed.payment.Payment;
import com.artembuzed.payment.PaymentClient;
import com.artembuzed.registration.Registration;
import com.artembuzed.registration.RegistrationRepository;
import io.quarkus.grpc.GrpcClient;
import io.quarkus.logging.Log;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.time.Duration;
import java.util.Collection;

@Path("registration")
@Produces(MediaType.APPLICATION_JSON)
public class RegistrationResource {
    private final RegistrationRepository registrationRepository;
    private final PaymentClient paymentClient;

    @GrpcClient("userservice")
    UserService userService;

    public RegistrationResource(RegistrationRepository registrationRepository,
                                @RestClient PaymentClient paymentClient) {
        this.registrationRepository = registrationRepository;
        this.paymentClient = paymentClient;
    }

    @GET
    @Path("getAllRegistrations")
    public Collection<Registration> getAllRegistrations() {
        return registrationRepository.findAll();
    }

    @GET
    @Path("getRegistrationById/{id}")
    public Registration getRegistrationById(@PathParam("id") Long id) {
        return registrationRepository.findById(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Registration create(Registration registration) {
        CheckUserExistsRequest req = CheckUserExistsRequest.newBuilder()
                .setUserId(registration.userId)
                .build();

        CheckUserExistsResponse resp = userService
                .checkUserExists(req)
                .await().atMost(Duration.ofSeconds(3));

        if(!resp.getExists())
            throw new BadRequestException("User does not exist");

        Registration reg = registrationRepository.save(registration);

        if(registration.statusIsRegistered()){
            Payment payment = paymentClient.createPayment(registration.userId, registration.id);
            Log.info("Successfully created payment" + payment);
        }
        return reg;
    }
}
