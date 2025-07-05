package com.pizzeria.dao;

import com.pizzeria.model.entity.Produto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ProdutoDAO {

    @PersistenceContext
    EntityManager em;

    public void persistir(Produto produto) {
        em.persist(produto);
    }

    public void atualizar(Produto produto) {
        em.merge(produto);
    }

    public void deletar(Produto produto) {
        em.remove(produto);
    }

    public Optional<Produto> buscarPorId(Long id) {
        return Optional.ofNullable(em.find(Produto.class, id));
    }

    public List<Produto> listarTodos() {
        TypedQuery<Produto> query = em.createQuery(
            "SELECT p FROM Produto p ORDER BY p.nome", Produto.class);
        return query.getResultList();
    }

    public List<Produto> listarPorTipo(Produto.Tipo tipo) {
        TypedQuery<Produto> query = em.createQuery(
            "SELECT p FROM Produto p WHERE p.tipo = :tipo ORDER BY p.nome", Produto.class);
        query.setParameter("tipo", tipo);
        return query.getResultList();
    }

    public List<Produto> listarDisponiveis() {
        TypedQuery<Produto> query = em.createQuery(
            "SELECT p FROM Produto p WHERE p.disponivel = true ORDER BY p.nome", Produto.class);
        return query.getResultList();
    }

    public List<Produto> listarDisponiveisPorTipo(Produto.Tipo tipo) {
        TypedQuery<Produto> query = em.createQuery(
            "SELECT p FROM Produto p WHERE p.tipo = :tipo AND p.disponivel = true ORDER BY p.nome", Produto.class);
        query.setParameter("tipo", tipo);
        return query.getResultList();
    }

    public boolean existePorNome(String nome) {
        TypedQuery<Long> query = em.createQuery(
            "SELECT COUNT(p) FROM Produto p WHERE p.nome = :nome", Long.class);
        query.setParameter("nome", nome);
        return query.getSingleResult() > 0;
    }
} 