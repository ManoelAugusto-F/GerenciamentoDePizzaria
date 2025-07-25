package com.pizzeria.resource;

import com.pizzeria.model.dto.CartDTO;
import com.pizzeria.model.entity.Cart;
import com.pizzeria.model.entity.Produto;
import com.pizzeria.model.entity.User;
import com.pizzeria.service.CartService;
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

    @POST
    @Path("/add")
    public Response addItemToCart(CartDTO cartDTO) {
        try {
            if (cartDTO == null || cartDTO.getUserId() == null || cartDTO.getProductId() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Dados do carrinho inválidos").build();
            }
            Cart cart = new Cart();

            User user = new User();
            user.setId(cartDTO.getUserId());
            cart.setUser(user);

            Produto produto = new Produto();
            produto.id = cartDTO.getProductId();
            cart.setProduct(produto);


            Cart createdCart = cartService.addToCart(cart);
            return Response.status(Response.Status.CREATED).entity(createdCart).build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/{userId}")
    public Response getCartByUserId(@PathParam("userId") Long userId) {
        try {
            if (userId == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("ID do usuário não pode ser nulo").build();
            }

            CartDTO dto = new CartDTO();
            dto.setUserId(userId);
            List<Cart> cartList = cartService.getCart(dto);

            return Response.ok(cartList).build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{cartId}")
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
}