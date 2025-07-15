package com.pizzeria.dao;

import com.pizzeria.model.entity.Pedido;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PedidoDAO {

    @PersistenceContext
    EntityManager em;

    public void persistir(Pedido pedido) {
        em.persist(pedido);
    }

    public void atualizar(Pedido pedido) {
        em.merge(pedido);
    }

    public void deletar(Pedido pedido) {
        em.remove(pedido);
    }

    public Optional<Pedido> buscarPorId(Long id) {
        return Optional.ofNullable(em.find(Pedido.class, id));
    }

    public List<Pedido> listarTodos() {
        TypedQuery<Pedido> query = em.createQuery(
            "SELECT p FROM Pedido p ORDER BY p.dataPedido DESC", Pedido.class);
        return query.getResultList();
    }

    public List<Pedido> listarPorCliente(Long clienteId) {
        TypedQuery<Pedido> query = em.createQuery(
            "SELECT p FROM Pedido p WHERE p.cliente.id = :clienteId ORDER BY p.dataPedido DESC", Pedido.class);
        query.setParameter("clienteId", clienteId);
        return query.getResultList();
    }

    public List<Pedido> listarPorStatus(Pedido.Status status) {
        TypedQuery<Pedido> query = em.createQuery(
            "SELECT p FROM Pedido p WHERE p.status = :status ORDER BY p.dataPedido DESC", Pedido.class);
        query.setParameter("status", status);
        return query.getResultList();
    }

    public List<Pedido> listarPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        TypedQuery<Pedido> query = em.createQuery(
            "SELECT p FROM Pedido p WHERE p.dataPedido BETWEEN :dataInicio AND :dataFim ORDER BY p.dataPedido DESC", Pedido.class);
        query.setParameter("dataInicio", dataInicio);
        query.setParameter("dataFim", dataFim);
        return query.getResultList();
    }

    public List<Pedido> listarPorAtendente(Long atendenteId) {
        TypedQuery<Pedido> query = em.createQuery(
            "SELECT p FROM Pedido p WHERE p.atendente.id = :atendenteId ORDER BY p.dataPedido DESC", Pedido.class);
        query.setParameter("atendenteId", atendenteId);
        return query.getResultList();
    }

    public Long contarPorStatus(Pedido.Status status) {
        TypedQuery<Long> query = em.createQuery(
            "SELECT COUNT(p) FROM Pedido p WHERE p.status = :status", Long.class);
        query.setParameter("status", status);
        return query.getSingleResult();
    }
} 