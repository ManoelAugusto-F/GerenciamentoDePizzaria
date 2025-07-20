package com.pizzeria.resource;

import com.pizzeria.model.entity.Pedido;
import com.pizzeria.model.entity.User;
import com.pizzeria.service.PedidoService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
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
    public Response criar(Pedido pedido) {
        try {
            User usuario = (User) securityContext.getUserPrincipal();
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
    public Response atualizarStatus(@PathParam("id") Long id, Pedido.Status novoStatus) {
        try {
            User usuario = (User) securityContext.getUserPrincipal();
            Pedido pedido = pedidoService.atualizarStatus(id, novoStatus, usuario);
            return Response.ok(pedido).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                         .entity(e.getMessage())
                         .build();
        }
    }
    
    @GET
    public List<Pedido> listarTodos() {
        return pedidoService.listarTodos();
    }
    
    @GET
    @Path("/cliente/{clienteId}")
    public List<Pedido> listarPorCliente(@PathParam("clienteId") Long clienteId) {
        return pedidoService.listarPorCliente(clienteId);
    }
    
    /**
     * Listar pedidos por status (painel do atendente)
     * Somente ADMIN e ATENDENTE
     */
    @GET
    @Path("/status/{status}")
    public List<Pedido> listarPorStatus(@PathParam("status") Pedido.Status status) {
        return pedidoService.listarPorStatus(status);
    }
    
    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        try {
            Pedido pedido = pedidoService.buscarPorId(id);
            if (pedido == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            User usuario = (User) securityContext.getUserPrincipal();
            if (usuario.getRoles() == "USER" &&
                    !(pedido.getCliente().getId() == usuario.getId())) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
            
            return Response.ok(pedido).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                         .entity(e.getMessage())
                         .build();
        }
    }
    
    @GET
    @Path("/me")
    public List<Pedido> listarMeusPedidos(@jakarta.ws.rs.core.Context jakarta.ws.rs.core.SecurityContext securityContext) {
        User usuario = (User) securityContext.getUserPrincipal();
        if (usuario == null) {
            throw new WebApplicationException("Não autenticado", Response.Status.UNAUTHORIZED);
        }
        return pedidoService.listarPorCliente(usuario.getId());
    }

} 