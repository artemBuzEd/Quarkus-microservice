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
import jakarta.ws.rs.core.SecurityContext;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.time.Duration;
import java.util.Collection;
import java.util.stream.Collectors;

@Path("registration")
@Produces(MediaType.APPLICATION_JSON)
public class RegistrationResource {
    private final RegistrationRepository registrationRepository;
    private final PaymentClient paymentClient;
    private final SecurityContext securityContext;

    @GrpcClient("userservice")
    UserService userService;

    public RegistrationResource(RegistrationRepository registrationRepository,
                                @RestClient PaymentClient paymentClient,
                                SecurityContext securityContext) {
        this.registrationRepository = registrationRepository;
        this.paymentClient = paymentClient;
        this.securityContext = securityContext;
    }

    @GET
    @Path("getAllRegistrations")
    public Collection<Registration> getAllRegistrations() {
        String userId = securityContext.getUserPrincipal() != null ? securityContext.getUserPrincipal().getName() : null;

        return registrationRepository.findAll().stream().filter(reservation -> userId == null ||
                userId.equals(reservation.userId))
                .collect(Collectors.toList());
    }

    @GET
    @Path("getRegistrationById/{id}")
    public Registration getRegistrationById(@PathParam("id") Long id) {
        return registrationRepository.findById(id);
    }

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    public Registration create(Registration registration) {
// 1. GET THE AUTHENTICATED USER. This is the only trusted source of identity.
        // The security context is populated from the JWT token propagated by the User Service.
        String authenticatedUserId = securityContext.getUserPrincipal() != null ?
                securityContext.getUserPrincipal().getName() : null;


        Log.info("Incoming registration object:");
        Log.info("  - userId: " + registration.userId);
        Log.info("  - eventId: " + registration.eventId);
        Log.info("  - status: " + registration.status);
        Log.info("  - registeredAt: " + registration.registeredAt);


        // 2. FAIL IF NO USER IS LOGGED IN.
        // We should not allow anonymous registrations.
        if (authenticatedUserId == null) {
            Log.error("Attempt to create registration without an authenticated user.");
            throw new NotAuthorizedException("User must be logged in to create a registration.");
        }

        Registration registrationToSave = new Registration(registration.status, registration.eventId, authenticatedUserId);

        Log.info("Attempting to save registration for user '" + registrationToSave.userId + "' with eventId '" + registrationToSave.eventId + "'");

        Registration savedRegistration = registrationRepository.save(registrationToSave);

        Log.info("Successfully saved registration with ID: " + savedRegistration.id + " for user: " + savedRegistration.userId);

        // Continue with payment logic...
        if(savedRegistration.statusIsRegistered()){
            Payment payment = paymentClient.createPayment(savedRegistration.userId, savedRegistration.id);
            Log.info("Successfully created payment" + payment);
        }

        return savedRegistration;
    }
}
