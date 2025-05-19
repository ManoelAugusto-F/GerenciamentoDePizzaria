package com.pizzeria.service;

import com.pizzeria.model.entity.Log;
import com.pizzeria.model.entity.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class UsuarioService {

    @Transactional
    public Usuario criar(Usuario usuario) {
        usuario.persist();
        registrarLog(usuario, "CRIAR", "Usuário criado");
        return usuario;
    }
    
    @Transactional
    public Usuario atualizar(Long id, Usuario usuarioAtualizado) {
        Usuario usuario = Usuario.findById(id);
        if (usuario == null) {
            throw new RuntimeException("Usuário não encontrado");
        }
        
        usuario.setNomeCompleto(usuarioAtualizado.getNomeCompleto());
        usuario.setEmail(usuarioAtualizado.getEmail());
        usuario.setPerfil(usuarioAtualizado.getPerfil());
        usuario.setAtivo(usuarioAtualizado.isAtivo());
        
        if (usuarioAtualizado.getSenha() != null && !usuarioAtualizado.getSenha().isEmpty()) {
            usuario.setSenha(usuarioAtualizado.getSenha());
        }
        
        registrarLog(usuario, "ATUALIZAR", "Usuário atualizado");
        return usuario;
    }
    
    @Transactional
    public void deletar(Long id) {
        Usuario usuario = Usuario.findById(id);
        if (usuario == null) {
            throw new RuntimeException("Usuário não encontrado");
        }
        
        registrarLog(usuario, "DELETAR", "Usuário deletado");
        usuario.delete();
    }
    
    public List<Usuario> listarTodos() {
        return Usuario.listAll();
    }
    
    public Usuario buscarPorId(Long id) {
        return Usuario.findById(id);
    }
    
    private void registrarLog(Usuario usuario, String acao, String descricao) {
        Log log = new Log();
        log.setUsuario(usuario);
        log.setAcao(acao);
        log.setDescricao(descricao);
        log.persist();
    }
} 