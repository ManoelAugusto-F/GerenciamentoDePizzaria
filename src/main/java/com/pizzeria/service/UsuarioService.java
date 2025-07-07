package com.pizzeria.service;

import com.pizzeria.dao.LogDAO;
import com.pizzeria.dao.UsuarioDAO;
import com.pizzeria.dto.UsuarioCriarDTO;
import com.pizzeria.dto.UsuarioAtualizarDTO;
import com.pizzeria.model.entity.Log;
import com.pizzeria.model.entity.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
import java.util.List;
import java.util.Optional;
import org.mindrot.jbcrypt.BCrypt;

@ApplicationScoped
public class UsuarioService {

    private static final Logger LOG = Logger.getLogger(UsuarioService.class);

    @Inject
    UsuarioDAO usuarioDAO;
    
    @Inject
    LogDAO logDAO;

    @Transactional
    public Usuario criar(UsuarioCriarDTO dto) {
        try {
            // Verificar se email já existe
            if (usuarioDAO.existePorEmail(dto.getEmail())) {
                throw new WebApplicationException("Email já está em uso", Response.Status.CONFLICT);
            }
            
            Usuario usuario = new Usuario();
            usuario.setNomeCompleto(dto.getNomeCompleto());
            usuario.setEmail(dto.getEmail());
            usuario.setSenha(BCrypt.hashpw(dto.getSenha(), BCrypt.gensalt()));
            usuario.setPerfil(dto.getPerfil());
            usuario.setAtivo(dto.isAtivo());
            
            usuarioDAO.persistir(usuario);
            
            registrarLog(usuario, "CRIAR", "Usuário criado com sucesso");
            LOG.infof("Usuário criado: %s (%s)", usuario.getNomeCompleto(), usuario.getEmail());
            
            return usuario;
        } catch (WebApplicationException e) {
            throw e;
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao criar usuário: %s", dto.getEmail());
            throw new WebApplicationException("Erro interno ao criar usuário", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Transactional
    public Usuario atualizar(Long id, UsuarioAtualizarDTO dto) {
        try {
            Optional<Usuario> usuarioOpt = usuarioDAO.buscarPorId(id);
            if (usuarioOpt.isEmpty()) {
                throw new WebApplicationException("Usuário não encontrado", Response.Status.NOT_FOUND);
            }
            
            Usuario usuario = usuarioOpt.get();
            
            // Verificar se email já existe (exceto para o próprio usuário)
            Optional<Usuario> usuarioComEmail = usuarioDAO.buscarPorEmail(dto.getEmail());
            if (usuarioComEmail.isPresent() && !usuarioComEmail.get().id.equals(id)) {
                throw new WebApplicationException("Email já está em uso", Response.Status.CONFLICT);
            }
            
            usuario.setNomeCompleto(dto.getNomeCompleto());
            usuario.setEmail(dto.getEmail());
            usuario.setPerfil(dto.getPerfil());
            usuario.setAtivo(dto.isAtivo());
            
            if (dto.getSenha() != null && !dto.getSenha().isEmpty()) {
                usuario.setSenha(BCrypt.hashpw(dto.getSenha(), BCrypt.gensalt()));
            }
            
            usuarioDAO.atualizar(usuario);
            
            registrarLog(usuario, "ATUALIZAR", "Usuário atualizado com sucesso");
            LOG.infof("Usuário atualizado: %s (%s)", usuario.getNomeCompleto(), usuario.getEmail());
            
            return usuario;
        } catch (WebApplicationException e) {
            throw e;
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao atualizar usuário ID: %d", id);
            throw new WebApplicationException("Erro interno ao atualizar usuário", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Transactional
    public void deletar(Long id) {
        try {
            Optional<Usuario> usuarioOpt = usuarioDAO.buscarPorId(id);
            if (usuarioOpt.isEmpty()) {
                throw new WebApplicationException("Usuário não encontrado", Response.Status.NOT_FOUND);
            }
            
            Usuario usuario = usuarioOpt.get();
            registrarLog(usuario, "DELETAR", "Usuário deletado");
            usuarioDAO.deletar(usuario);
            
            LOG.infof("Usuário deletado: %s (%s)", usuario.getNomeCompleto(), usuario.getEmail());
        } catch (WebApplicationException e) {
            throw e;
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao deletar usuário ID: %d", id);
            throw new WebApplicationException("Erro interno ao deletar usuário", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
    
    public List<Usuario> listarTodos() {
        try {
            return usuarioDAO.listarTodos();
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao listar usuários");
            throw new WebApplicationException("Erro interno ao listar usuários", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
    
    public List<Usuario> listarAtivos() {
        try {
            return usuarioDAO.listarAtivos();
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao listar usuários ativos");
            throw new WebApplicationException("Erro interno ao listar usuários", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
    
    public List<Usuario> listarPorPerfil(Usuario.Perfil perfil) {
        try {
            return usuarioDAO.listarPorPerfil(perfil);
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao listar usuários por perfil: %s", perfil);
            throw new WebApplicationException("Erro interno ao listar usuários", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
    
    public Usuario buscarPorId(Long id) {
        try {
            Optional<Usuario> usuarioOpt = usuarioDAO.buscarPorId(id);
            if (usuarioOpt.isEmpty()) {
                throw new WebApplicationException("Usuário não encontrado", Response.Status.NOT_FOUND);
            }
            return usuarioOpt.get();
        } catch (WebApplicationException e) {
            throw e;
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao buscar usuário ID: %d", id);
            throw new WebApplicationException("Erro interno ao buscar usuário", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
    
    public Usuario buscarPorEmail(String email) {
        try {
            Optional<Usuario> usuarioOpt = usuarioDAO.buscarPorEmail(email);
            if (usuarioOpt.isEmpty()) {
                throw new WebApplicationException("Usuário não encontrado", Response.Status.NOT_FOUND);
            }
            return usuarioOpt.get();
        } catch (WebApplicationException e) {
            throw e;
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao buscar usuário por email: %s", email);
            throw new WebApplicationException("Erro interno ao buscar usuário", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
    
    private void registrarLog(Usuario usuario, String acao, String descricao) {
        try {
            Log log = new Log();
            log.setUsuario(usuario);
            log.setAcao(acao);
            log.setDescricao(descricao);
            logDAO.persistir(log);
        } catch (Exception e) {
            LOG.warnf(e, "Erro ao registrar log para usuário: %s", usuario.getEmail());
        }
    }
} 