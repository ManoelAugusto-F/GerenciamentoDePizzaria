// Configuração da API
const API_URL = 'http://localhost:8080/api';

// Funções de autenticação
async function login(email, password) {
    try {
        const response = await fetch(`${API_URL}/auth/login`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({email, password})
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || 'Erro ao fazer login');
        }
        window.location.href = 'index.html';
    } catch (error) {
        showError(error.message);
    }
}

async function register(name, email, password, phone) {
    try {
        const response = await fetch(`${API_URL}/auth/register`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({name, email, password, phone})
        });

        if (!response.ok) {
            throw new Error('Erro ao registrar usuário');
        }

        window.location.href = 'login.html';
    } catch (error) {
        showError('Erro ao registrar: ' + error.message);
    }
}


// Funções de validação
function validateLoginForm() {
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    if (!email || !password) {
        showError('Por favor, preencha todos os campos');
        return false;
    }
    login(email, password).catch(error => {
        showError(error.message);
    });
    return false;
}

function validateRegisterForm() {
    const name = document.getElementById('name').value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const phone = document.getElementById('phone').value;

    if (!name || !email || !password || !phone) {
        showError('Por favor, preencha todos os campos');
        return false;
    }

    register(name, email, password, phone);
    return false;
}

// Funções do carrinho
let cart = [];

async function addToCart(id) {

    await addCarrinRequest(id);
    setTimeout(() => {
        getCartItens();
        updateCart();  // dá tempo pro back terminar de deletar
    }, 200);
}

async function addCarrinRequest(id) {
    const response = fetch(`${API_URL}/cart/add/${id}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${auth.getToken()}`
        }
    });
}

async function getCartItens() {
    try {
        const response = await fetch(`${API_URL}/cart`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${auth.getToken()}`
            }
        });

        if (!response.ok) {
            throw new Error('Erro ao buscar o carrinho');
        }

        const data = await response.json();
        cart = data;


        updateCart();

    } catch (error) {
        console.error('Erro ao buscar itens do carrinho', error);
        cart = [];
    }
}

document.addEventListener('DOMContentLoaded', function () {
    getCartItens();

});

async function removeFromCart(pizzaId) {
    try {
        await removeRequest(pizzaId);
        setTimeout(() => {
            updateCart();  // dá tempo pro back terminar de deletar
        }, 200);      // atualiza os dados do carrinho
                      // renderiza os novos dados
    } catch (error) {
        console.error('Erro ao remover item do carrinho:', error);
    }
}

async function removeRequest(pizzaId) {
    try {
        const response = await fetch(`${API_URL}/cart/delete/${pizzaId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${auth.getToken()}`
            }
        });

        if (!response.ok) {
            throw new Error('Erro ao remover item do carrinho');
        }
    } catch (error) {
        console.error('Erro:', error);
    }
}


function updateCart() {
    const cartItems = document.getElementById('cart-items');
    const cartTotal = document.getElementById('cart-total');

    if (!cartItems || !cartTotal) return;

    cartItems.innerHTML = '';
    let total = 0;

    const groupedItems = {};

    cart.forEach(item => {
        const productId = item.product.id;

        if (!groupedItems[productId]) {
            groupedItems[productId] = {
                product: item.product,
                quantity: 1,
                cartItemIds: [item.id] // guarda o id do item no carrinho
            };
        } else {
            groupedItems[productId].quantity++;
            groupedItems[productId].cartItemIds.push(item.id);
        }
    });

    Object.values(groupedItems).forEach(group => {
        const itemTotal = group.product.preco * group.quantity;
        total += itemTotal;

        // Usa o primeiro item do carrinho daquele produto para remover
        const cartItemIdToRemove = group.cartItemIds[0];

        const li = document.createElement('li');
        li.innerHTML = `
            <span>${group.product.nome} x ${group.quantity}</span>
            <span>R$ ${itemTotal.toFixed(2)}</span>
            <button class="remove-cart-btn" onclick="removeFromCart(${cartItemIdToRemove})">
                <i class="fa-solid fa-square-minus"></i>
            </button>
        `;
        cartItems.appendChild(li);
    });

    cartTotal.textContent = `Total: R$ ${total.toFixed(2)}`;
}


async function checkout() {
    if (cart.length === 0) {
        showError('Adicione itens ao carrinho primeiro');
        return;
    }

    const orderItems = cart.map(item => ({
        pizzaId: item.pizza.id,
        quantity: item.quantity
    }));

    const order = await createOrder(orderItems);
    if (order) {
        cart = [];
        updateCart();
        alert('Pedido realizado com sucesso!');
        window.location.href = 'index.html';
    }
}

// Função auxiliar para mostrar erros
function showError(message) {
    const errorDiv = document.getElementById('error-message');
    if (errorDiv) {
        errorDiv.textContent = message;
        errorDiv.style.display = 'block';
    } else {
        alert(message);
    }
}