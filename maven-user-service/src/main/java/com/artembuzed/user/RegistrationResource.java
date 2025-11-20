package com.artembuzed.user;

import com.artembuzed.model.Registration;
import io.quarkus.logging.Log;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.Collection;

@Path("/")
public class RegistrationResource {
    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance index(
                String name
        );

        public static native TemplateInstance register(
           String userId
        );

        public static native TemplateInstance allregistrations(
                Collection<Registration> registrations
        );
    }

    @Inject
    SecurityContext securityContext;

    @RestClient
    RegistrationClient registrationClient;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance index() {
        return Templates.index(securityContext.getUserPrincipal().getName());
    }

//    @GET
//    @Produces(MediaType.TEXT_HTML)
//    @Path("/registration/create")
//    public TemplateInstance register() {
//        Log.info("Registering user in RegistrationResource");
//        return Templates.register(securityContext.getUserPrincipal().getName());
//    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/allRegistrations")
    public TemplateInstance allregistrations() {
        Log.info("All registrations in RegistrationResource");
        Collection<Registration> registrations = registrationClient.getAllRegistrations();
        return Templates.allregistrations(registrations);

    }

    @POST
    @Path("/register")
    @Produces(MediaType.TEXT_HTML)
    public RestResponse<TemplateInstance> createRegistration(
            @RestForm String eventId
    ) {
        Log.info("Create registration in RegistrationResource");
        String userId = securityContext.getUserPrincipal().getName();
        Registration reg = new Registration();
        reg.userId = userId;
        reg.eventId = eventId;
        reg.status = "Registered";
        Log.info("Registration created: " + reg);
        registrationClient.createRegistration(reg);

        return RestResponse.ResponseBuilder
                .ok(allregistrations())
                .header("HX-Trigger-After-Swap",
                        "update-available-registrations-list")
                .build();
    }
}
