package com.artembuzed.user;

import com.artembuzed.model.Registration;
import io.quarkus.oidc.token.propagation.common.AccessToken;
import jakarta.ws.rs.*;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.Collection;

@RegisterRestClient(baseUri = "http://localhost:8081")
@AccessToken
@Path("registration")
public interface RegistrationClient {
    @GET
    @Path("getAllRegistrations")
    Collection<Registration> getAllRegistrations();

    @POST
    @Path("create")
    Registration createRegistration(Registration registration);

    @GET
    @Path("getRegistrationById/{id}")
    Registration getRegistration(@PathParam("id") Long id);
}
