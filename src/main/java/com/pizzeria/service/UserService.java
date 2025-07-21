package com.pizzeria.service;

import com.pizzeria.dao.UserDAO;
import com.pizzeria.model.entity.User;
import io.quarkus.security.runtime.SecurityIdentityAssociation;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@ApplicationScoped
public class UserService {
    @Inject
    UserDAO userDAO;
    public List<User> getAllUsers() {
        return User.listAll();
    }

    @Transactional
    public User updateUserRolesByEmail(String email, String roles) {

        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email must not be null or empty");
        }

        User user = userDAO.updateUserRolesByEmail(email);

        if (user == null) {
            throw new IllegalArgumentException("Usuario não encontrado " + email);
        }
        user.setRoles(roles);
        return user;
    }


    public User getUserById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID must not be null");
        }
        User user = userDAO.getUserById(id);
        if (user == null) {
            throw new IllegalArgumentException("Usuario não encontrado com ID: " + id);
        }
        return user;
    }

    public User findByEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email must not be null or empty");
        }
        User user = userDAO.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("Usuario não encontrado com email: " + email);
        }
        return user;
    }

}
