package com.pizzeria.service;

import com.pizzeria.dao.CartDAO;
import com.pizzeria.dao.UserDAO;
import com.pizzeria.model.dto.CartDTO;
import com.pizzeria.model.entity.Cart;
import com.pizzeria.model.entity.Produto;
import com.pizzeria.model.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

import java.util.List;

@ApplicationScoped
public class CartService {

    @Inject
    CartDAO cartDAO;

    @Inject
    UserDAO userDAO;
    @Inject
    ProdutoService produtoService;

    public List<Cart> getCart(CartDTO dto) {
        if (dto == null || dto.getUserId() == null) {
            throw new IllegalArgumentException("ID do usuário é obrigatório");
        }

        User user = userDAO.getUserById(dto.getUserId());
        if (user == null) {
            throw new IllegalArgumentException("Usuário não encontrado com ID: " + dto.getUserId());
        }

        return cartDAO.GetCart(user);
    }
    @Transactional
    public Cart addToCart(long productId, User user) {
        Produto produto = produtoService.buscarPorId(productId);
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setProduct(produto);
        return cartDAO.addToCart(cart);
    }
    @Transactional
    public Cart removeFromCart(Long cartId) {
        if (cartId == null) {
            throw new IllegalArgumentException("ID do carrinho não pode ser nulo");
        }

        return cartDAO.removeFromCart(cartId);
    }
}
