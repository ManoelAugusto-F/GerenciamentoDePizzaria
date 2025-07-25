package com.pizzeria.service;

import com.pizzeria.Enum.StatusPedido;
import com.pizzeria.dao.CartDAO;
import com.pizzeria.dao.PedidoDAO;
import com.pizzeria.dao.UserDAO;
import com.pizzeria.model.dto.CartDTO;
import com.pizzeria.model.dto.PedidoDTO;
import com.pizzeria.model.dto.ProdutoItem;
import com.pizzeria.model.entity.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class CartService {

    @Inject
    CartDAO cartDAO;

    @Inject
    UserDAO userDAO;
    @Inject
    ProdutoService produtoService;
    @Inject
    PedidoService pedidoService;
    @Inject
    AuthService authService;
    @Inject
    PedidoDAO pedidoDAO;

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

    public void removeFromUserID(long userId) {
        cartDAO.deleteByUserId(userId);
    }

    @Transactional
    public Pedido checkout(PedidoDTO dto) {
        User user = authService.AutenticateUser();
        Pedido pedido = new Pedido();
        pedido.setCliente(user);
        pedido.setStatus(StatusPedido.PREPARANDO);
        pedido.setTotal(dto.getTotal());

        List<ItemPedido> itens = new ArrayList<>();

        for (ProdutoItem prodDTO : dto.getProdutos()) {
            Produto produto = produtoService.buscarPorId(prodDTO.getProductId());
            if (produto == null) {
                throw new IllegalArgumentException("Produto com ID " + prodDTO.getProductId() + " não encontrado.");
            }

            ItemPedido item = new ItemPedido();
            item.setProduto(produto);
            item.setPedido(pedido);
            item.setQuantity(prodDTO.getQuantity());
            item.setPreco(prodDTO.getPreco());

            itens.add(item);
        }

        pedido.setItens(itens);
        Pedido pedidoSalvo = pedidoService.criar(pedido, user);

        removeFromUserID(user.getId());

        return pedidoSalvo;
    }

}
