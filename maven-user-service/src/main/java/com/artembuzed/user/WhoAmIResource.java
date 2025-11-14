package com.artembuzed.user;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;

import java.security.Security;

@Path("/whoami")
public class WhoAmIResource {

    @Inject
    Template user;

    @Inject
    SecurityContext securityContext;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getUser() {
        String userId = securityContext.getUserPrincipal() != null ? securityContext.getUserPrincipal().getName() : null;
        return user.data("name", userId);
    }
}
