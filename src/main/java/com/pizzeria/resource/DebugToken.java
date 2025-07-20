package com.pizzeria.resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
@Path("/debug")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class DebugToken {
    @Inject
    JsonWebToken jwt;

    @GET
    @Path("/get-token")
    public Response debugToken() {


        if (jwt == null || jwt.getRawToken() == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Token não encontrado").build();
        }

        return Response.ok("Token OK. Usuário: " + jwt.getName()).build();
    }
}
