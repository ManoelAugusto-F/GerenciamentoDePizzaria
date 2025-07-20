package com.pizzeria.dao;

import com.pizzeria.model.entity.User;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserDAO {

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
//    public User getUserByEmail(String email) {
//        User user = User.find("email", email).firstResult();
//        if (user == null) {
//            throw new IllegalArgumentException("User not found");
//        }
//        return user;
//    }
}
