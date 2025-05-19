package com.pizzeria.rest;

import com.pizzeria.model.entity.Pedido;
import com.pizzeria.model.entity.Usuario;
import com.pizzeria.service.PedidoService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import java.util.List;

@Path("/pedidos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PedidoResource {

    @Inject
    PedidoService pedidoService;
    
    @Context
    SecurityContext securityContext;
    
    @POST
    @RolesAllowed({"CLIENTE", "ATENDENTE"})
    public Response criar(Pedido pedido) {
        try {
            Usuario usuario = (Usuario) securityContext.getUserPrincipal();
            Pedido novoPedido = pedidoService.criar(pedido, usuario);
            return Response.status(Response.Status.CREATED).entity(novoPedido).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                         .entity(e.getMessage())
                         .build();
        }
    }
    
    @PUT
    @Path("/{id}/status")
    @RolesAllowed({"ADMIN", "ATENDENTE"})
    public Response atualizarStatus(@PathParam("id") Long id, Pedido.Status novoStatus) {
        try {
            Usuario usuario = (Usuario) securityContext.getUserPrincipal();
            Pedido pedido = pedidoService.atualizarStatus(id, novoStatus, usuario);
            return Response.ok(pedido).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                         .entity(e.getMessage())
                         .build();
        }
    }
    
    @GET
    @RolesAllowed({"ADMIN", "ATENDENTE"})
    public List<Pedido> listarTodos() {
        return pedidoService.listarTodos();
    }
    
    @GET
    @Path("/cliente/{clienteId}")
    @RolesAllowed({"ADMIN", "ATENDENTE"})
    public List<Pedido> listarPorCliente(@PathParam("clienteId") Long clienteId) {
        return pedidoService.listarPorCliente(clienteId);
    }
    
    @GET
    @Path("/status/{status}")
    @RolesAllowed({"ADMIN", "ATENDENTE"})
    public List<Pedido> listarPorStatus(@PathParam("status") Pedido.Status status) {
        return pedidoService.listarPorStatus(status);
    }
    
    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "ATENDENTE", "CLIENTE"})
    public Response buscarPorId(@PathParam("id") Long id) {
        try {
            Pedido pedido = pedidoService.buscarPorId(id);
            if (pedido == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            
            Usuario usuario = (Usuario) securityContext.getUserPrincipal();
            if (usuario.getPerfil() == Usuario.Perfil.CLIENTE && 
                !pedido.getCliente().getId().equals(usuario.getId())) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
            
            return Response.ok(pedido).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                         .entity(e.getMessage())
                         .build();
        }
    }
} 