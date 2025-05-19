package com.pizzeria.rest;

import com.pizzeria.dto.AuthDTO;
import com.pizzeria.dto.AuthResponseDTO;
import com.pizzeria.service.AuthService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    AuthService authService;
    
    @POST
    @Path("/login")
    public Response login(AuthDTO authDTO) {
        try {
            AuthResponseDTO response = authService.authenticate(authDTO);
            return Response.ok(response).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                         .entity(e.getMessage())
                         .build();
        }
    }
} 