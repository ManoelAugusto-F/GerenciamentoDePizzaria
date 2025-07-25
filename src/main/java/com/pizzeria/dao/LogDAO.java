package com.pizzeria.dao;

import com.pizzeria.model.entity.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class LogDAO {

    @PersistenceContext
    EntityManager em;

    public void persistir(Log log) {
        em.persist(log);
    }

    public void atualizar(Log log) {
        em.merge(log);
    }

    public void deletar(Log log) {
        em.remove(log);
    }

    public Optional<Log> buscarPorId(Long id) {
        return Optional.ofNullable(em.find(Log.class, id));
    }

    public List<Log> listarTodos() {
        TypedQuery<Log> query = em.createQuery(
            "SELECT l FROM Log l ORDER BY l.dataHora DESC", Log.class);
        return query.getResultList();
    }

    public List<Log> listarPorUsuario(Long usuarioId) {
        TypedQuery<Log> query = em.createQuery(
            "SELECT l FROM Log l WHERE l.usuario.id = :usuarioId ORDER BY l.dataHora DESC", Log.class);
        query.setParameter("usuarioId", usuarioId);
        return query.getResultList();
    }

    public List<Log> listarPorAcao(String acao) {
        TypedQuery<Log> query = em.createQuery(
            "SELECT l FROM Log l WHERE l.acao = :acao ORDER BY l.dataHora DESC", Log.class);
        query.setParameter("acao", acao);
        return query.getResultList();
    }

    public List<Log> listarPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        TypedQuery<Log> query = em.createQuery(
            "SELECT l FROM Log l WHERE l.dataHora BETWEEN :dataInicio AND :dataFim ORDER BY l.dataHora DESC", Log.class);
        query.setParameter("dataInicio", dataInicio);
        query.setParameter("dataFim", dataFim);
        return query.getResultList();
    }

    public List<Log> listarComFiltros(Long usuarioId, String acao, LocalDateTime dataInicio, LocalDateTime dataFim) {
        StringBuilder jpql = new StringBuilder("SELECT l FROM Log l WHERE 1=1");
        
        if (usuarioId != null) {
            jpql.append(" AND l.usuario.id = :usuarioId");
        }
        if (acao != null && !acao.isEmpty()) {
            jpql.append(" AND l.acao = :acao");
        }
        if (dataInicio != null) {
            jpql.append(" AND l.dataHora >= :dataInicio");
        }
        if (dataFim != null) {
            jpql.append(" AND l.dataHora <= :dataFim");
        }
        
        jpql.append(" ORDER BY l.dataHora DESC");
        TypedQuery<Log> query = em.createQuery(jpql.toString(), Log.class);
        
        if (usuarioId != null) {
            query.setParameter("usuarioId", usuarioId);
        }
        if (acao != null && !acao.isEmpty()) {
            query.setParameter("acao", acao);
        }
        if (dataInicio != null) {
            query.setParameter("dataInicio", dataInicio);
        }
        if (dataFim != null) {
            query.setParameter("dataFim", dataFim);
        }
        return query.getResultList();
    }
} 