package com.pizzeria.dao;

import com.pizzeria.model.entity.Cart;
import com.pizzeria.model.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class CartDAO {
    @Inject
    UserDAO userDAO;
    public List<Cart> GetCart(User user){
        userDAO.getUserById(user.getId());

        if (user == null) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }

        List<Cart> cartList = Cart.list("userId", user.getId());
        if (cartList == null || cartList.isEmpty()) {
            throw new IllegalArgumentException("Carrinho vazio para o usuário: " + user.getName());
        }
        return  cartList;
    }

    public Cart addToCart(Cart cart) {
        if (cart == null || cart.getUser() == null || cart.getProduct() == null) {
            throw new IllegalArgumentException("Carrinho inválido ou incompleto");
        }

        cart.persist();
        return cart;
    }
    public Cart removeFromCart(Long cartId) {
        if (cartId == null) {
            throw new IllegalArgumentException("ID do carrinho não pode ser nulo");
        }

        Cart cart = Cart.findById(cartId);
        if (cart == null) {
            throw new IllegalArgumentException("Carrinho não encontrado com ID: " + cartId);
        }

        cart.delete();
        return cart;
    }

}
