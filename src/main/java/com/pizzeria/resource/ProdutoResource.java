package com.pizzeria.resource;

import com.pizzeria.model.dto.ProdutoDTO;
import com.pizzeria.model.entity.Produto;
import com.pizzeria.model.entity.User;
import com.pizzeria.service.AuthService;
import com.pizzeria.service.ImageSaveService;
import com.pizzeria.service.ProdutoService;
import com.pizzeria.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
import java.util.List;

@Path("/produtos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class ProdutoResource {

    private static final Logger LOG = Logger.getLogger(ProdutoResource.class);

    @Inject
    ImageSaveService imageSaveService;
    @Inject
    UserService userService;
    @Inject
    ProdutoService produtoService;
    @Inject
    AuthService authService;

    @POST
    @RolesAllowed({"ADMIN"})
    public Response criar(ProdutoDTO dto) {
        try {
           User usuario = authService.AutenticateUser();
            Produto produto = new Produto();
            produto.setNome(dto.getNome());
            produto.setDescricao(dto.getDescricao());
            produto.setPreco(dto.getPreco());
            produto.setTipo(dto.getTipo());
            produto.setDisponivel(dto.isDisponivel());

            if (dto.getImagemBase64() != null && !dto.getImagemBase64().isEmpty()) {
                try {
                    String base64 = dto.getImagemBase64();
                    String path = imageSaveService.saveImageFromBase64(
                            base64,
                            "produto_" + System.currentTimeMillis() + ".png"
                    );
                    produto.setImagemUrl(path);
                } catch (Exception e) {
                    throw new RuntimeException("Erro ao salvar a imagem: " + e.getMessage());
                }
            }
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
    public Response atualizar(@PathParam("id") Long id, Produto produto) {
        try {
            User usuario =  authService.AutenticateUser();
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
    public Response deletar(@PathParam("id") Long id) {
        try {
            User usuario = authService.AutenticateUser();
            produtoService.deletar(id, usuario);
            return Response.noContent().build();
        } catch (RuntimeException e) {
            LOG.errorf(e, "Erro ao deletar produto ID: %d", id);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"erro\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    @Transactional
    @GET
    @RolesAllowed({"USER", "ADMIN","ATENDENTE"})
    public Response listarTodos() {
        User usuario = authService.AutenticateUser();
        try {
            List<Produto> produtos = produtoService.listarTodos(usuario);
            return Response.ok(produtos).build();
        } catch (RuntimeException e) {
            LOG.errorf(e, "Erro ao listar produtos");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"erro\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    @GET
    @Path("/{id}")
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
    public Response ativar(@PathParam("id") Long id) {
        try {
            User usuario = authService.AutenticateUser();
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
    public Response desativar(@PathParam("id") Long id) {
        try {
            User usuario = authService.AutenticateUser();
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