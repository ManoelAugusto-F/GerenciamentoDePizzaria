package com.pizzeria.rest;

import com.pizzeria.model.entity.Usuario;
import com.pizzeria.service.UsuarioService;
import com.pizzeria.dto.UsuarioDTO;
import com.pizzeria.dto.UsuarioCriarDTO;
import com.pizzeria.dto.UsuarioAtualizarDTO;
import com.pizzeria.dto.UserLoginDTO;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.WebApplicationException;
import org.jboss.logging.Logger;
import java.util.List;
import java.util.stream.Collectors;
import org.mindrot.jbcrypt.BCrypt;

@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    private static final Logger LOG = Logger.getLogger(UsuarioResource.class);

    @Inject
    UsuarioService usuarioService;
    
    @GET
    @Path("/teste")
    public Response teste() {
        return Response.ok("{\"mensagem\":\"API funcionando!\"}").build();
    }
    
    @GET
    @Path("/listar-todos")
    public Response listarTodosUsuarios() {
        List<Usuario> usuarios = Usuario.listAll();
        return Response.ok(usuarios).build();
    }
    
    @GET
    @Path("/criar-usuarios")
    @Transactional
    public Response criarUsuarios() {
        try {
            // Verificar se já existem usuários
            long count = Usuario.count();
            if (count > 0) {
                return Response.ok("{\"mensagem\":\"Usuários já existem\", \"count\": " + count + "}").build();
            }
            
            // Criar usuários manualmente
            Usuario admin1 = new Usuario();
            admin1.setNomeCompleto("Manoel Augusto");
            admin1.setEmail("manoelaugusto.ferreira@gmail.com");
            admin1.setSenha("12345678"); // Senha simples para teste
            admin1.setPerfil(Usuario.Perfil.ADMIN);
            admin1.setAtivo(true);
            admin1.persist();
            
            Usuario admin2 = new Usuario();
            admin2.setNomeCompleto("Julio Cesar");
            admin2.setEmail("juliojcc.franca@gmail.com");
            admin2.setSenha("12345678"); // Senha simples para teste
            admin2.setPerfil(Usuario.Perfil.ADMIN);
            admin2.setAtivo(true);
            admin2.persist();
            
            return Response.ok("{\"mensagem\":\"Usuários criados com sucesso\"}").build();
        } catch (Exception e) {
            return Response.status(500).entity("{\"erro\":\"" + e.getMessage() + "\"}").build();
        }
    }
    
    @POST
    @RolesAllowed("ADMIN")
    public Response criar(@Valid UsuarioCriarDTO dto) {
        try {
            Usuario novoUsuario = usuarioService.criar(dto);
            UsuarioDTO responseDTO = new UsuarioDTO(
                novoUsuario.id,
                novoUsuario.getNomeCompleto(),
                novoUsuario.getEmail(),
                novoUsuario.getPerfil(),
                novoUsuario.isAtivo()
            );
            return Response.status(Response.Status.CREATED).entity(responseDTO).build();
        } catch (WebApplicationException e) {
            LOG.warnf("Erro na criação de usuário: %s", e.getMessage());
            return Response.status(e.getResponse().getStatus())
                         .entity("{\"erro\":\"" + e.getMessage() + "\"}")
                         .build();
        } catch (Exception e) {
            LOG.errorf(e, "Erro inesperado na criação de usuário");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                         .entity("{\"erro\":\"Erro interno do servidor\"}")
                         .build();
        }
    }
    
    @PUT
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response atualizar(@PathParam("id") Long id, @Valid UsuarioAtualizarDTO dto) {
        try {
            Usuario usuarioAtualizado = usuarioService.atualizar(id, dto);
            UsuarioDTO responseDTO = new UsuarioDTO(
                usuarioAtualizado.id,
                usuarioAtualizado.getNomeCompleto(),
                usuarioAtualizado.getEmail(),
                usuarioAtualizado.getPerfil(),
                usuarioAtualizado.isAtivo()
            );
            return Response.ok(responseDTO).build();
        } catch (WebApplicationException e) {
            LOG.warnf("Erro na atualização de usuário ID %d: %s", id, e.getMessage());
            return Response.status(e.getResponse().getStatus())
                         .entity("{\"erro\":\"" + e.getMessage() + "\"}")
                         .build();
        } catch (Exception e) {
            LOG.errorf(e, "Erro inesperado na atualização de usuário ID: %d", id);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                         .entity("{\"erro\":\"Erro interno do servidor\"}")
                         .build();
        }
    }
    
    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response deletar(@PathParam("id") Long id) {
        try {
            usuarioService.deletar(id);
            return Response.noContent().build();
        } catch (WebApplicationException e) {
            LOG.warnf("Erro na exclusão de usuário ID %d: %s", id, e.getMessage());
            return Response.status(e.getResponse().getStatus())
                         .entity("{\"erro\":\"" + e.getMessage() + "\"}")
                         .build();
        } catch (Exception e) {
            LOG.errorf(e, "Erro inesperado na exclusão de usuário ID: %d", id);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                         .entity("{\"erro\":\"Erro interno do servidor\"}")
                         .build();
        }
    }
    
    @GET
    @RolesAllowed("ADMIN")
    public Response listarTodos() {
        try {
            List<Usuario> usuarios = usuarioService.listarTodos();
            List<UsuarioDTO> dtos = usuarios.stream()
                .map(u -> new UsuarioDTO(u.id, u.getNomeCompleto(), u.getEmail(), u.getPerfil(), u.isAtivo()))
                .collect(Collectors.toList());
            return Response.ok(dtos).build();
        } catch (WebApplicationException e) {
            LOG.warnf("Erro ao listar usuários: %s", e.getMessage());
            return Response.status(e.getResponse().getStatus())
                         .entity("{\"erro\":\"" + e.getMessage() + "\"}")
                         .build();
        } catch (Exception e) {
            LOG.errorf(e, "Erro inesperado ao listar usuários");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                         .entity("{\"erro\":\"Erro interno do servidor\"}")
                         .build();
        }
    }
    
    @GET
    @Path("/ativos")
    @RolesAllowed("ADMIN")
    public Response listarAtivos() {
        try {
            List<Usuario> usuarios = usuarioService.listarAtivos();
            List<UsuarioDTO> dtos = usuarios.stream()
                .map(u -> new UsuarioDTO(u.id, u.getNomeCompleto(), u.getEmail(), u.getPerfil(), u.isAtivo()))
                .collect(Collectors.toList());
            return Response.ok(dtos).build();
        } catch (WebApplicationException e) {
            LOG.warnf("Erro ao listar usuários ativos: %s", e.getMessage());
            return Response.status(e.getResponse().getStatus())
                         .entity("{\"erro\":\"" + e.getMessage() + "\"}")
                         .build();
        } catch (Exception e) {
            LOG.errorf(e, "Erro inesperado ao listar usuários ativos");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                         .entity("{\"erro\":\"Erro interno do servidor\"}")
                         .build();
        }
    }
    
    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "ATENDENTE"})
    public Response buscarPorId(@PathParam("id") Long id) {
        try {
            Usuario usuario = usuarioService.buscarPorId(id);
            UsuarioDTO dto = new UsuarioDTO(
                usuario.id,
                usuario.getNomeCompleto(),
                usuario.getEmail(),
                usuario.getPerfil(),
                usuario.isAtivo()
            );
            return Response.ok(dto).build();
        } catch (WebApplicationException e) {
            LOG.warnf("Erro ao buscar usuário ID %d: %s", id, e.getMessage());
            return Response.status(e.getResponse().getStatus())
                         .entity("{\"erro\":\"" + e.getMessage() + "\"}")
                         .build();
        } catch (Exception e) {
            LOG.errorf(e, "Erro inesperado ao buscar usuário ID: %d", id);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                         .entity("{\"erro\":\"Erro interno do servidor\"}")
                         .build();
        }
    }
    
    @PUT
    @Path("/me")
    @RolesAllowed({"ADMIN", "ATENDENTE", "CLIENTE"})
    public Response atualizarProprioUsuario(@jakarta.ws.rs.core.Context jakarta.ws.rs.core.SecurityContext securityContext, 
                                          @Valid UsuarioAtualizarDTO dto) {
        try {
            Usuario usuarioLogado = (Usuario) securityContext.getUserPrincipal();
            if (usuarioLogado == null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                             .entity("{\"erro\":\"Usuário não autenticado\"}")
                             .build();
            }
            
            Usuario usuario = usuarioService.atualizar(usuarioLogado.id, dto);
            UsuarioDTO responseDTO = new UsuarioDTO(
                usuario.id,
                usuario.getNomeCompleto(),
                usuario.getEmail(),
                usuario.getPerfil(),
                usuario.isAtivo()
            );
            return Response.ok(responseDTO).build();
        } catch (WebApplicationException e) {
            LOG.warnf("Erro na atualização do próprio usuário: %s", e.getMessage());
            return Response.status(e.getResponse().getStatus())
                         .entity("{\"erro\":\"" + e.getMessage() + "\"}")
                         .build();
        } catch (Exception e) {
            LOG.errorf(e, "Erro inesperado na atualização do próprio usuário");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                         .entity("{\"erro\":\"Erro interno do servidor\"}")
                         .build();
        }
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUsuario(com.pizzeria.dto.UserLoginDTO dto) {
        try {
            Usuario usuario = Usuario.find("email", dto.email).firstResult();
            if (usuario == null || !BCrypt.checkpw(dto.password, usuario.getSenha())) {
                return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"erro\":\"Credenciais inválidas\"}")
                    .build();
            }
            if (!usuario.isAtivo()) {
                return Response.status(Response.Status.FORBIDDEN)
                    .entity("{\"erro\":\"Usuário inativo\"}")
                    .build();
            }
            return Response.ok()
                .entity("{\"mensagem\":\"Login realizado com sucesso\"}")
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"erro\":\"Erro interno ao autenticar\"}")
                .build();
        }
    }
} 