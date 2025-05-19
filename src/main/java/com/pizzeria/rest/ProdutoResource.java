package com.pizzeria.rest;

import com.pizzeria.model.entity.Produto;
import com.pizzeria.model.entity.Usuario;
import com.pizzeria.service.ProdutoService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import java.util.List;

@Path("/produtos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProdutoResource {

    @Inject
    ProdutoService produtoService;
    
    @Context
    SecurityContext securityContext;
    
    @POST
    @RolesAllowed("ADMIN")
    public Response criar(Produto produto) {
        try {
            Usuario usuario = (Usuario) securityContext.getUserPrincipal();
            Produto novoProduto = produtoService.criar(produto, usuario);
            return Response.status(Response.Status.CREATED).entity(novoProduto).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                         .entity(e.getMessage())
                         .build();
        }
    }
    
    @PUT
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response atualizar(@PathParam("id") Long id, Produto produto) {
        try {
            Usuario usuario = (Usuario) securityContext.getUserPrincipal();
            Produto produtoAtualizado = produtoService.atualizar(id, produto, usuario);
            return Response.ok(produtoAtualizado).build();
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
            Usuario usuario = (Usuario) securityContext.getUserPrincipal();
            produtoService.deletar(id, usuario);
            return Response.noContent().build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                         .entity(e.getMessage())
                         .build();
        }
    }
    
    @GET
    public List<Produto> listarTodos() {
        return produtoService.listarTodos();
    }
    
    @GET
    @Path("/tipo/{tipo}")
    public List<Produto> listarPorTipo(@PathParam("tipo") Produto.Tipo tipo) {
        return produtoService.listarPorTipo(tipo);
    }
    
    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        try {
            Produto produto = produtoService.buscarPorId(id);
            if (produto == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(produto).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                         .entity(e.getMessage())
                         .build();
        }
    }
} 