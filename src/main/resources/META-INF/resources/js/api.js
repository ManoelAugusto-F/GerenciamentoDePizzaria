class Api {
    constructor() {
        this.baseUrl = '/api';
    }

    async request(endpoint, options = {}) {
        const token = auth.getToken();
        const headers = {
            'Content-Type': 'application/json',
            ...(token && { 'Authorization': `Bearer ${token}` }),
            ...options.headers
        };

        try {
            const response = await fetch(`${this.baseUrl}${endpoint}`, {
                ...options,
                headers
            });

            if (!response.ok) {
                throw new Error('Erro na requisição');
            }

            return await response.json();
        } catch (error) {
            console.error('Erro na API:', error);
            throw error;
        }
    }

    // Produtos
    async getProducts() {
        return this.request('/products');
    }

    async createProduct(product) {
        return this.request('/products', {
            method: 'POST',
            body: JSON.stringify(product)
        });
    }

    async updateProduct(id, product) {
        return this.request(`/products/${id}`, {
            method: 'PUT',
            body: JSON.stringify(product)
        });
    }

    async deleteProduct(id) {
        return this.request(`/products/${id}`, {
            method: 'DELETE'
        });
    }

    // Pedidos
    async getOrders() {
        return this.request('/orders');
    }

    async createOrder(order) {
        return this.request('/orders', {
            method: 'POST',
            body: JSON.stringify(order)
        });
    }

    async updateOrderStatus(id, status) {
        return this.request(`/orders/${id}/status`, {
            method: 'PUT',
            body: JSON.stringify({ status })
        });
    }

    // Usuários
    async getUsers() {
        return this.request('/users');
    }

    async createUser(user) {
        return this.request('/users', {
            method: 'POST',
            body: JSON.stringify(user)
        });
    }

    async updateUser(id, user) {
        return this.request(`/users/${id}`, {
            method: 'PUT',
            body: JSON.stringify(user)
        });
    }

    async deleteUser(id) {
        return this.request(`/users/${id}`, {
            method: 'DELETE'
        });
    }
}

const api = new Api(); 