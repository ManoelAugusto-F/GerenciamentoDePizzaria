package com.pizzeria.service;

import com.pizzeria.Enum.Tipo;
import com.pizzeria.model.entity.Log;
import com.pizzeria.model.entity.Produto;
import com.pizzeria.model.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class ProdutoService {
    @Inject
    LogService logService;

    @Transactional
    public Produto criar(Produto produto, User usuario) {
        produto.persist();
        logService.registrarLog(usuario, "CRIAR", "Produto criado: " + produto.getNome());
        return produto;
    }
    
    @Transactional
    public Produto atualizar(Long id, Produto produtoAtualizado, User usuario) {
        Produto produto = Produto.findById(id);
        if (produto == null) {
            throw new RuntimeException("Produto não encontrado");
        }
        
        produto.setNome(produtoAtualizado.getNome());
        produto.setDescricao(produtoAtualizado.getDescricao());
        produto.setPreco(produtoAtualizado.getPreco());
        produto.setTipo(produtoAtualizado.getTipo());
        produto.setDisponivel(produtoAtualizado.isDisponivel());
        produto.setImagemUrl(produtoAtualizado.getImagemUrl());

        logService.registrarLog(usuario, "ATUALIZAR", "Produto atualizado: " + produto.getNome());
        return produto;
    }
    
    @Transactional
    public void deletar(Long id, User usuario) {
        Produto produto = Produto.findById(id);
        if (produto == null) {
            throw new RuntimeException("Produto não encontrado");
        }

        logService.registrarLog(usuario, "DELETAR", "Produto deletado: " + usuario.getName());
        produto.delete();
    }
    
   public List<Produto> listarTodos(User usuario) {
            logService.registrarLog(usuario, "Listar", "Produto listado: " + "todos");
            return Produto.listAll();
    }
    
    public List<Produto> listarPorTipo(Tipo tipo) {
        return Produto.list("tipo", tipo);
    }
    
    public Produto buscarPorId(Long id) {
        return Produto.findById(id);
    }

    // NOVOS MÉTODOS
    public List<Produto> listarDisponiveis() {
        return Produto.list("disponivel", true);
    }

    @Transactional
    public Produto ativar(Long id, User usuario) {
        Produto produto = Produto.findById(id);
        if (produto == null) {
            throw new RuntimeException("Produto não encontrado");
        }
        produto.setDisponivel(true);
        logService.registrarLog(usuario, "ATIVAR", "Produto ativado: " + produto.getNome());
        return produto;
    }

    @Transactional
    public Produto desativar(Long id, User usuario) {
        Produto produto = Produto.findById(id);
        if (produto == null) {
            throw new RuntimeException("Produto não encontrado");
        }
        produto.setDisponivel(false);
        logService.registrarLog(usuario, "DESATIVAR", "Produto desativado: " + produto.getNome());
        return produto;
    }


} 