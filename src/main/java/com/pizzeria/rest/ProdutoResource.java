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
import org.jboss.logging.Logger;
import java.util.List;

@Path("/produtos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProdutoResource {

    private static final Logger LOG = Logger.getLogger(ProdutoResource.class);

    @Inject
    ProdutoService produtoService;
    
    @Context
    SecurityContext securityContext;
    
    @GET
    @Path("/teste")
    public Response teste() {
        return Response.ok("{\"mensagem\":\"API de Produtos funcionando!\"}").build();
    }
    
    @POST
    @RolesAllowed("ADMIN")
    public Response criar(Produto produto) {
        try {
            Usuario usuario = (Usuario) securityContext.getUserPrincipal();
            Produto novoProduto = produtoService.criar(produto, usuario);
            return Response.status(Response.Status.CREATED).entity(novoProduto).build();
        } catch (RuntimeException e) {
            LOG.errorf(e, "Erro ao criar produto");
            return Response.status(Response.Status.BAD_REQUEST)
                         .entity("{\"erro\":\"" + e.getMessage() + "\"}")
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
            LOG.errorf(e, "Erro ao atualizar produto ID: %d", id);
            return Response.status(Response.Status.BAD_REQUEST)
                         .entity("{\"erro\":\"" + e.getMessage() + "\"}")
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
            LOG.errorf(e, "Erro ao deletar produto ID: %d", id);
            return Response.status(Response.Status.BAD_REQUEST)
                         .entity("{\"erro\":\"" + e.getMessage() + "\"}")
                         .build();
        }
    }
    
    @GET
    @RolesAllowed({"ADMIN", "ATENDENTE"})
    public Response listarTodos() {
        try {
            List<Produto> produtos = produtoService.listarTodos();
            return Response.ok(produtos).build();
        } catch (RuntimeException e) {
            LOG.errorf(e, "Erro ao listar produtos");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                         .entity("{\"erro\":\"" + e.getMessage() + "\"}")
                         .build();
        }
    }
    
    @GET
    @Path("/disponiveis")
    @RolesAllowed({"ADMIN", "ATENDENTE", "CLIENTE"})
    public Response listarDisponiveis() {
        try {
            List<Produto> produtos = produtoService.listarDisponiveis();
            return Response.ok(produtos).build();
        } catch (RuntimeException e) {
            LOG.errorf(e, "Erro ao listar produtos disponíveis");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                         .entity("{\"erro\":\"" + e.getMessage() + "\"}")
                         .build();
        }
    }
    
    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "ATENDENTE", "CLIENTE"})
    public Response buscarPorId(@PathParam("id") Long id) {
        try {
            Produto produto = produtoService.buscarPorId(id);
            return Response.ok(produto).build();
        } catch (RuntimeException e) {
            LOG.errorf(e, "Erro ao buscar produto ID: %d", id);
            return Response.status(Response.Status.NOT_FOUND)
                         .entity("{\"erro\":\"" + e.getMessage() + "\"}")
                         .build();
        }
    }
    
    @PUT
    @Path("/{id}/ativar")
    @RolesAllowed("ADMIN")
    public Response ativar(@PathParam("id") Long id) {
        try {
            Usuario usuario = (Usuario) securityContext.getUserPrincipal();
            Produto produto = produtoService.ativar(id, usuario);
            return Response.ok(produto).build();
        } catch (RuntimeException e) {
            LOG.errorf(e, "Erro ao ativar produto ID: %d", id);
            return Response.status(Response.Status.BAD_REQUEST)
                         .entity("{\"erro\":\"" + e.getMessage() + "\"}")
                         .build();
        }
    }
    
    @PUT
    @Path("/{id}/desativar")
    @RolesAllowed("ADMIN")
    public Response desativar(@PathParam("id") Long id) {
        try {
            Usuario usuario = (Usuario) securityContext.getUserPrincipal();
            Produto produto = produtoService.desativar(id, usuario);
            return Response.ok(produto).build();
        } catch (RuntimeException e) {
            LOG.errorf(e, "Erro ao desativar produto ID: %d", id);
            return Response.status(Response.Status.BAD_REQUEST)
                         .entity("{\"erro\":\"" + e.getMessage() + "\"}")
                         .build();
        }
    }
} 