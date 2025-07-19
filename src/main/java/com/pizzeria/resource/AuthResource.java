package com.pizzeria.resource;
import com.pizzeria.model.dto.UserDTO;
import com.pizzeria.model.entity.User;
import com.pizzeria.service.CookieService;
import com.pizzeria.service.TokenService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import org.mindrot.jbcrypt.BCrypt;


import java.util.Map;


@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {
    @Inject
    TokenService tokenService;
    @Inject
    CookieService cookieService;
    @POST
    @Path("/register")
    @Transactional
    public Response register(@Valid UserDTO dto) {
        if (User.find("email", dto.email).firstResult() != null) {
            return Response.status(Response.Status.CONFLICT).entity("Email already registered").build();
        }
        User user = new User();
        user.setName(dto.name);
        user.setEmail(dto.email);
        user.setPassword(BCrypt.hashpw(dto.password, BCrypt.gensalt()));
        user.setRoles("USER"); // Default role for new users
        user.persist();
        return Response.status(Response.Status.CREATED).build();
    }

    @POST
    @Path("/login")
    public Response login(@Valid UserDTO dto) {
        User user;

        try {
            user = User.find("email", dto.email).firstResult();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
                    .build();
        }

        if (user == null || !BCrypt.checkpw(dto.password, user.getPassword())) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Email ou senha inválidos.")
                    .build();
        }

        System.out.println("Usuário encontrado: " + user.getName());


        String token = tokenService.generateToken(user.getName(), user.getEmail(), user.getRoles());
        NewCookie jwtCookie = cookieService.generateJwtCookie(token);
        return Response.ok()
                .entity(Map.of("message", "Login com sucesso"))
                .cookie(jwtCookie).header("Location", "/index.html")
                .build();
    }


}