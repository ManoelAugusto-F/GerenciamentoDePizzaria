package com.pizzeria.resource;

import com.pizzeria.model.dto.UserDTO;
import com.pizzeria.model.entity.User;
import com.pizzeria.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;


@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResources {
    @Inject
    UserService userService;

    @GET
    @Path("")
     @RolesAllowed({"ADMIN"})
    public List<User> GetAllUser() {
        return userService.getAllUsers();
    }

    @PUT
    @Path("/{email}/roles")
    @RolesAllowed({"ADMIN"})
    public User updateUser(@Valid UserDTO user, @PathParam("email") String email) {
        return userService.updateUserRolesByEmail(email, user.roles);
    }
}
