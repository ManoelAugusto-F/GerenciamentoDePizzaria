package com.pizzeria.resource;

import com.pizzeria.dto.OrderCheckoutDTO;
import com.pizzeria.dto.OrderItemDTO;
import com.pizzeria.model.Order;
import com.pizzeria.model.OrderItem;
import com.pizzeria.model.Pizza;
import com.pizzeria.model.User;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Path("/orders")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Authenticated
public class OrderResource {
    @POST
    @Transactional
    public Response checkout(@Valid OrderCheckoutDTO dto, @Context SecurityContext ctx) {
        Principal principal = ctx.getUserPrincipal();
        if (principal == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        User user = User.find("email", principal.getName()).firstResult();
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        List<OrderItem> items = new ArrayList<>();
        double total = 0;
        for (OrderItemDTO itemDTO : dto.items) {
            Pizza pizza = Pizza.findById(itemDTO.pizzaId);
            if (pizza == null || !pizza.available) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid pizza").build();
            }
            OrderItem item = new OrderItem();
            item.pizza = pizza;
            item.quantity = itemDTO.quantity;
            item.price = pizza.price * itemDTO.quantity;
            items.add(item);
            total += item.price;
        }
        Order order = new Order();
        order.user = user;
        order.items = items;
        order.total = total;
        order.status = "PAID"; // Simulação de pagamento
        order.persist();
        for (OrderItem item : items) {
            item.order = order;
            item.persist();
        }
        return Response.ok(order).build();
    }
} 