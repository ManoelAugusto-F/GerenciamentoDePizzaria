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
} 