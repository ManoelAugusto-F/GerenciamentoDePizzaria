package com.pizzeria.rest;

import com.pizzeria.model.entity.Usuario;
import com.pizzeria.service.UsuarioService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    @Inject
    UsuarioService usuarioService;
    
    @POST
    @RolesAllowed("ADMIN")
    public Response criar(Usuario usuario) {
        try {
            Usuario novoUsuario = usuarioService.criar(usuario);
            return Response.status(Response.Status.CREATED).entity(novoUsuario).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                         .entity(e.getMessage())
                         .build();
        }
    }
    
    @PUT
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response atualizar(@PathParam("id") Long id, Usuario usuario) {
        try {
            Usuario usuarioAtualizado = usuarioService.atualizar(id, usuario);
            return Response.ok(usuarioAtualizado).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                         .entity(e.getMessage())
                         .build();
        }
    }
    
    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response deletar(@PathParam("id") Long id) {
        try {
            usuarioService.deletar(id);
            return Response.noContent().build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                         .entity(e.getMessage())
                         .build();
        }
    }
    
    @GET
    @RolesAllowed("ADMIN")
    public List<Usuario> listarTodos() {
        return usuarioService.listarTodos();
    }
    
    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "ATENDENTE"})
    public Response buscarPorId(@PathParam("id") Long id) {
        try {
            Usuario usuario = usuarioService.buscarPorId(id);
            if (usuario == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(usuario).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                         .entity(e.getMessage())
                         .build();
        }
    }
} 