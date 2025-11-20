package com.artembuzed.rest;
import com.artembuzed.UserService;
import com.artembuzed.payment.Payment;
import com.artembuzed.payment.PaymentClient;
import com.artembuzed.registration.entity.Registration;
import io.quarkus.grpc.GrpcClient;
import io.quarkus.logging.Log;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.Collection;
import java.util.stream.Collectors;

@Path("registration")
@Produces(MediaType.APPLICATION_JSON)
public class RegistrationResource {
    private final PaymentClient paymentClient;
    private final SecurityContext securityContext;

    @GrpcClient("userservice")
    UserService userService;

    public RegistrationResource(@RestClient PaymentClient paymentClient,
                                SecurityContext securityContext) {
        this.paymentClient = paymentClient;
        this.securityContext = securityContext;
    }

    @GET
    @Path("getAllRegistrations")
    public Collection<Registration> getAllRegistrations() {
        String userId = securityContext.getUserPrincipal() != null ? securityContext.getUserPrincipal().getName() : null;

        return Registration.<Registration>streamAll().filter(registration -> userId == null ||
                userId.equals(registration.userId))
                .collect(Collectors.toList());
    }

    @GET
    @Path("getRegistrationById/{id}")
    public Registration getRegistrationById(@PathParam("id") Long id) {
        return Registration.findById(id);
    }

    @POST
    @Path("/create")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    public Registration create(Registration registration) {
        String authenticatedUserId = securityContext.getUserPrincipal() != null ?
                securityContext.getUserPrincipal().getName() : null;

        registration.persist();

        Log.info("Incoming registration object:");
        Log.info("  - userId: " + registration.userId);
        Log.info("  - eventId: " + registration.eventId);
        Log.info("  - status: " + registration.status);
        Log.info("  - registeredAt: " + registration.registeredAt);

        if (authenticatedUserId == null) {
            Log.error("Attempt to create registration without an authenticated user.");
            throw new NotAuthorizedException("User must be logged in to create a registration.");
        }

        registration.userId = authenticatedUserId;

        Log.info("Successfully saved registration with ID: " + registration.id + " for user: " + authenticatedUserId);

        if(registration.statusIsRegistered()){
            Payment payment = paymentClient.createPayment(registration.userId, registration.id);
            Log.info("Successfully created payment" + payment);
        }

        return registration;
    }
}
