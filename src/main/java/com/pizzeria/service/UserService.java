package com.pizzeria.service;

import com.pizzeria.dao.UserDAO;
import com.pizzeria.model.entity.User;
import io.quarkus.security.runtime.SecurityIdentityAssociation;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

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
}
