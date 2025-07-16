// Configuração da API
const API_URL = 'http://localhost:8080/api';

// Funções de autenticação
async function login(email, password) {
    try {
        const response = await fetch(`${API_URL}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password })
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
            body: JSON.stringify({ name, email, password, phone })
        });

        if (!response.ok) {
            throw new Error('Erro ao registrar usuário');
        }

        window.location.href = 'login.html';
    } catch (error) {
        showError('Erro ao registrar: ' + error.message);
    }
}

// Funções do carrinho
async function getPizzas() {
    try {
        const response = await fetch(`${API_URL}/pizzas`);
        if (!response.ok) {
            throw new Error('Erro ao carregar pizzas');
        }
        return await response.json();
    } catch (error) {
        showError('Erro ao carregar menu: ' + error.message);
        return [];
    }
}

async function createOrder(items) {
    try {
        const token = localStorage.getItem('token');
        if (!token) {
            throw new Error('Usuário não autenticado');
        }

        const response = await fetch(`${API_URL}/orders`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({ items })
        });

        if (!response.ok) {
            throw new Error('Erro ao criar pedido');
        }

        return await response.json();
    } catch (error) {
        showError('Erro ao finalizar pedido: ' + error.message);
        return null;
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

function addToCart(pizza) {
    const existingItem = cart.find(item => item.pizza.id === pizza.id);
    if (existingItem) {
        existingItem.quantity++;
    } else {
        cart.push({
            pizza: pizza,
            quantity: 1
        });
    }
    updateCart();
}

function removeFromCart(pizzaId) {
    cart = cart.filter(item => item.pizza.id !== pizzaId);
    updateCart();
}

function updateCart() {
    const cartItems = document.getElementById('cart-items');
    const cartTotal = document.getElementById('cart-total');
    
    if (!cartItems || !cartTotal) return;

    cartItems.innerHTML = '';
    let total = 0;

    cart.forEach(item => {
        const itemTotal = item.pizza.price * item.quantity;
        total += itemTotal;

        const li = document.createElement('li');
        li.innerHTML = `
            <span>${item.pizza.name} x ${item.quantity}</span>
            <span>R$ ${itemTotal.toFixed(2)}</span>
            <button onclick="removeFromCart(${item.pizza.id})">Remover</button>
        `;
        cartItems.appendChild(li);
    });

    cartTotal.textContent = `Total: R$ ${total.toFixed(2)}`;
}

async function loadMenu() {
    const pizzas = await getPizzas();
    const menuContainer = document.getElementById('menu-items');
    
    if (!menuContainer) return;

    pizzas.forEach(pizza => {
        const div = document.createElement('div');
        div.className = 'menu-item';
        div.innerHTML = `
            <img src="${pizza.imageUrl || 'img/pizza-placeholder.jpg'}" alt="${pizza.name}">
            <h3>${pizza.name}</h3>
            <p>${pizza.description}</p>
            <span class="price">R$ ${pizza.price.toFixed(2)}</span>
            <button onclick="addToCart(${JSON.stringify(pizza)})">Adicionar ao Carrinho</button>
        `;
        menuContainer.appendChild(div);
    });
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

// Inicialização
document.addEventListener('DOMContentLoaded', () => {
    const menuPage = document.getElementById('menu-items');
    if (menuPage) {
        loadMenu();
    }
}); 