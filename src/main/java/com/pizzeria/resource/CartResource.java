package com.pizzeria.resource;

import com.pizzeria.model.dto.CartDTO;
import com.pizzeria.model.dto.PedidoDTO;
import com.pizzeria.model.entity.Cart;
import com.pizzeria.model.entity.Pedido;
import com.pizzeria.model.entity.User;
import com.pizzeria.service.AuthService;
import com.pizzeria.service.CartService;
import com.pizzeria.service.ProdutoService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/cart")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CartResource {

    @Inject
    CartService cartService;
    @Inject
    AuthService authService;
    @Inject
    ProdutoService produtoService;

    @POST
    @Path("/add/{productID}")
    @RolesAllowed({"ADMIN", "ATENDENTE", "USER"})
    public Response addItemToCart(@PathParam("productID") Long productID) {
        try {
            User user = authService.AutenticateUser();
            if (productID == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Produto não encontrado").build();
            }

            Cart createdCart = cartService.addToCart(productID,user);
            return Response.status(Response.Status.CREATED).entity(createdCart).build();

        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @GET
    @RolesAllowed({"ADMIN","USER", "ATENDENTE"})
    public Response getCartByUserId() {
        User user = authService.AutenticateUser();
        try {
            if (user == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("ID do usuário não pode ser nulo").build();
            }

            CartDTO dto = new CartDTO();
            dto.setUserId(user.getId());
            List<Cart> cartList = cartService.getCart(dto);

            return Response.ok(cartList).build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/delete/{cartId}")
    @RolesAllowed({"ADMIN","USER","ATENDENTE"})
    public Response deleteCartItem(@PathParam("cartId") Long cartId) {
        try {
            if (cartId == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("ID do carrinho não pode ser nulo").build();
            }

            cartService.removeFromCart(cartId);
            return Response.status(Response.Status.NO_CONTENT).build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/checkout")
    @RolesAllowed({"ADMIN","USER", "ATENDENTE"})
    public Response checkout(PedidoDTO pedido) {
        try {
            Pedido novoPedido = cartService.checkout(pedido);
            return Response.status(Response.Status.CREATED)
                    .entity(novoPedido)
                    .build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }

    }
}