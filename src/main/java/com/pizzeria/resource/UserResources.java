package com.pizzeria.resource;

import com.pizzeria.model.dto.UserDTO;
import com.pizzeria.model.entity.User;
import com.pizzeria.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;


@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class UserResources {
    @Inject
    UserService userService;

    @GET
    @Path("")
    @RolesAllowed({"ADMIN"})
    public Response getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();

            if (users == null || users.isEmpty()) {
                return Response.status(Response.Status.NO_CONTENT).build(); // 204
            }

            return Response.ok(users).build();
        } catch (Exception e) {
            e.printStackTrace();

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar usuários").build(); // 500
        }
    }

    @PUT
    @Path("/{email}/roles")
     @RolesAllowed({"ADMIN"})
    public User updateUser(@Valid UserDTO user, @PathParam("email") String email) {
        return userService.updateUserRolesByEmail(email, user.roles);
    }

    @GET
    @Path("/{id}")
    public User getUserById(@PathParam("id") Long id) {
        return userService.getUserById(id);
    }

}
