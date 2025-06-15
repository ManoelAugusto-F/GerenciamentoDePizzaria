package com.pizzeria.resource;

import com.pizzeria.dto.UserLoginDTO;
import com.pizzeria.dto.UserRegisterDTO;
import com.pizzeria.model.User;
import io.smallrye.jwt.build.Jwt;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.mindrot.jbcrypt.BCrypt;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {
    @POST
    @Path("/register")
    @Transactional
    public Response register(@Valid UserRegisterDTO dto) {
        if (User.find("email", dto.email).firstResult() != null) {
            return Response.status(Response.Status.CONFLICT).entity("Email already registered").build();
        }
        User user = new User();
        user.name = dto.name;
        user.email = dto.email;
        user.password = BCrypt.hashpw(dto.password, BCrypt.gensalt());
        user.roles = "USER";
        user.persist();
        return Response.status(Response.Status.CREATED).build();
    }

    @POST
    @Path("/login")
    public Response login(@Valid UserLoginDTO dto) {
        User user = User.find("email", dto.email).firstResult();
        if (user == null || !BCrypt.checkpw(dto.password, user.password)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
        }
        String token = Jwt.issuer("https://pizzeria.com")
                .upn(user.email)
                .groups(user.roles)
                .sign();
        return Response.ok().entity("{\"token\":\"" + token + "\"}").build();
    }
} 