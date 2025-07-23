package com.pizzeria.dao;

import com.pizzeria.model.dto.UserDTO;
import com.pizzeria.model.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;

@ApplicationScoped
public class UserDAO {
    @Inject
    EntityManager entityManager;
    public User updateUserRolesByEmail(String email) {
        User user = User.find("email", email).firstResult();
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        return user;
    }

    public User getUserById(Long id) {
        User user = User.findById(id);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        return user;
    }

    public User findByEmail(String email) {
        User user;
                try{
                    user = User.find("email", email).firstResult();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        return user;
    }

}


