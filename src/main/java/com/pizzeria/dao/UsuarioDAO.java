package com.pizzeria.dao;

import com.pizzeria.model.entity.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UsuarioDAO {

    @PersistenceContext
    EntityManager em;

    public void persistir(Usuario usuario) {
        em.persist(usuario);
    }

    public void atualizar(Usuario usuario) {
        em.merge(usuario);
    }

    public void deletar(Usuario usuario) {
        em.remove(usuario);
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return Optional.ofNullable(em.find(Usuario.class, id));
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        TypedQuery<Usuario> query = em.createQuery(
            "SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class);
        query.setParameter("email", email);
        return query.getResultList().stream().findFirst();
    }

    public List<Usuario> listarTodos() {
        TypedQuery<Usuario> query = em.createQuery(
            "SELECT u FROM Usuario u ORDER BY u.nomeCompleto", Usuario.class);
        return query.getResultList();
    }

    public List<Usuario> listarPorPerfil(Usuario.Perfil perfil) {
        TypedQuery<Usuario> query = em.createQuery(
            "SELECT u FROM Usuario u WHERE u.perfil = :perfil ORDER BY u.nomeCompleto", Usuario.class);
        query.setParameter("perfil", perfil);
        return query.getResultList();
    }

    public List<Usuario> listarAtivos() {
        TypedQuery<Usuario> query = em.createQuery(
            "SELECT u FROM Usuario u WHERE u.ativo = true ORDER BY u.nomeCompleto", Usuario.class);
        return query.getResultList();
    }

    public boolean existePorEmail(String email) {
        TypedQuery<Long> query = em.createQuery(
            "SELECT COUNT(u) FROM Usuario u WHERE u.email = :email", Long.class);
        query.setParameter("email", email);
        return query.getSingleResult() > 0;
    }

    public void flush() {
        em.flush();
    }
} 