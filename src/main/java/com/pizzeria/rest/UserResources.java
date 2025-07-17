package com.pizzeria.rest;

import com.pizzeria.dto.UserDTO;
import com.pizzeria.dto.UserLoginDTO;
import com.pizzeria.model.User;
import com.pizzeria.service.UserService;
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

    // isto ta quase pronto, falta implementar o front end e talvez mexer no uptade para
// caso de algo errado, n testei ainda se vai assim e cria no front end o formulario de admin pra fazer isso daqui
    @GET
    @Path("")
    public List<User> GetAllUser() {
        return userService.getAllUsers();
    }

    @PUT
    @Path("/{email}/roles")
    public User updateUser(@Valid UserLoginDTO user, @PathParam("email") String email) {
        return userService.updateUserRolesByEmail(email, user.roles);
    }


}
