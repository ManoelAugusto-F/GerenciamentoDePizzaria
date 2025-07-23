package com.pizzeria.resource;

import com.pizzeria.model.dto.UserDTO;
import com.pizzeria.model.entity.User;
import com.pizzeria.service.AuthService;
import com.pizzeria.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;


@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class UserResources {
    @Inject
    UserService userService;
    @Inject
    AuthService authService;

    @GET
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
    public Response updateUser(@Valid UserDTO dto, @PathParam("email") String email) {
        User user = userService.updateUserRolesByEmail(email, dto.getRoles());
        if (email == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Usuário não encontrado.").build();
        }
        return Response.ok(user).build();
    }

    @PUT
    @Path("/update/self")
    @RolesAllowed({"ADMIN", "USER", "ATENDENTE"})
    @Transactional
    public Response updateUserSelf(@Valid UserDTO userDTO) {

        if (userDTO == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Usuário não encontrado.").build();
        }
        User updated = userService.updateUserData(userDTO);
        return Response.ok(updated).build();
    }

    @GET
    @Path("/id/{id}")
    @RolesAllowed({"ADMIN", "USER", "ATENDENTE"})
    public Response getUserById(@PathParam("id") Long id) {
        User user = userService.getUserById(id);
        if (id == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Usuário não encontrado.").build();
        }
        return Response.ok(user).build();
    }

    @GET
    @Path("/email/{email}")
    @RolesAllowed({"ADMIN", "USER", "ATENDENTE"})
    public Response getUserById(@PathParam("email") String email) {
        User user = userService.findByEmail(email);
        if (email == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Usuário não encontrado.").build();
        }
        return Response.ok(user).build();
    }


}