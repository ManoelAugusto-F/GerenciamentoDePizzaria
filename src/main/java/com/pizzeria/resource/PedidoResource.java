package com.pizzeria.resource;

import com.pizzeria.Enum.StatusPedido;
import com.pizzeria.model.dto.Pedido.PedidoResponseDTO;
import com.pizzeria.model.entity.Pedido;
import com.pizzeria.model.entity.User;
import com.pizzeria.service.AuthService;
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
    @Inject
    AuthService authService;

    @POST
    @RolesAllowed({"ADMIN", "ATENDENTE", "USER"})
    public Response criar(Pedido pedido) {
        try {
            User usuario = authService.AutenticateUser();
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
    public Response atualizarStatus(@PathParam("id") Long id, StatusPedido novoStatus) {
        try {
            User usuario = authService.AutenticateUser();
            PedidoResponseDTO pedido = pedidoService.atualizarStatus(id, novoStatus, usuario);
            return Response.ok(pedido).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    @RolesAllowed({"ADMIN", "ATENDENTE", "USER"})
    public List<PedidoResponseDTO> listarTodos() {
        return pedidoService.listarTodos();
    }

}
