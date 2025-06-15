package com.pizzeria.resource;

import com.pizzeria.model.Pizza;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/pizzas")
@Produces(MediaType.APPLICATION_JSON)
public class PizzaResource {
    @GET
    public List<Pizza> list() {
        return Pizza.listAll();
    }
} 