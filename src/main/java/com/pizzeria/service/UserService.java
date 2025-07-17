package com.pizzeria.service;

import com.pizzeria.model.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class UserService {

    public List<User> getAllUsers() {
        return User.listAll(); // Panache magic
    }

    @Transactional
    public User updateUserRolesByEmail(String email, String roles) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email must not be null or empty");
        }

        User user = User.find("email", email).firstResult();
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        user.roles = roles;
        return user;
    }
}
