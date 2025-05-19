class App {
    constructor() {
        this.currentPage = 'home';
        this.setupEventListeners();
        this.loadPage();
    }

    setupEventListeners() {
        document.querySelectorAll('[data-page]').forEach(link => {
            link.addEventListener('click', (e) => {
                e.preventDefault();
                const page = e.target.dataset.page;
                this.navigateTo(page);
            });
        });
    }

    async navigateTo(page) {
        this.currentPage = page;
        await this.loadPage();
    }

    async loadPage() {
        const content = document.getElementById('content');
        if (!content) return;

        if (!auth.isAuthenticated() && this.currentPage !== 'home') {
            this.currentPage = 'home';
        }

        switch (this.currentPage) {
            case 'home':
                await this.loadHomePage();
                break;
            case 'menu':
                await this.loadMenuPage();
                break;
            case 'orders':
                await this.loadOrdersPage();
                break;
            case 'users':
                if (auth.isAdmin()) {
                    await this.loadUsersPage();
                }
                break;
            case 'products':
                if (auth.isAdmin()) {
                    await this.loadProductsPage();
                }
                break;
        }
    }

    async loadHomePage() {
        const content = document.getElementById('content');
        const template = document.getElementById('login-template');
        
        if (auth.isAuthenticated()) {
            content.innerHTML = `
                <div class="text-center">
                    <h1>Bem-vindo ao Sistema de Gerenciamento de Pizzaria</h1>
                    <p class="lead">Escolha uma opção no menu acima para começar.</p>
                </div>
            `;
        } else {
            content.innerHTML = template.innerHTML;
        }
    }

    async loadMenuPage() {
        const content = document.getElementById('content');
        const template = document.getElementById('menu-template');
        content.innerHTML = template.innerHTML;

        try {
            const products = await api.getProducts();
            const menuItems = document.getElementById('menuItems');
            
            menuItems.innerHTML = products.map(product => `
                <div class="col-md-4 mb-4">
                    <div class="card menu-item">
                        <img src="${product.imageUrl}" class="card-img-top" alt="${product.name}">
                        <div class="card-body">
                            <h5 class="card-title">${product.name}</h5>
                            <p class="card-text">${product.description}</p>
                            <p class="card-text">
                                <strong>R$ ${product.price.toFixed(2)}</strong>
                            </p>
                            <button class="btn btn-primary" onclick="app.addToCart(${product.id})">
                                Adicionar ao Carrinho
                            </button>
                        </div>
                    </div>
                </div>
            `).join('');
        } catch (error) {
            content.innerHTML = '<div class="alert alert-danger">Erro ao carregar o cardápio</div>';
        }
    }

    async loadOrdersPage() {
        const content = document.getElementById('content');
        const template = document.getElementById('orders-template');
        content.innerHTML = template.innerHTML;

        try {
            const orders = await api.getOrders();
            const ordersList = document.getElementById('ordersList');
            
            ordersList.innerHTML = orders.map(order => `
                <tr>
                    <td>#${order.id}</td>
                    <td>${new Date(order.createdAt).toLocaleString()}</td>
                    <td>
                        <span class="order-status status-${order.status.toLowerCase()}">
                            ${order.status}
                        </span>
                    </td>
                    <td>R$ ${order.totalAmount.toFixed(2)}</td>
                    <td class="action-buttons">
                        <button class="btn btn-sm btn-info" onclick="app.viewOrder(${order.id})">
                            Detalhes
                        </button>
                    </td>
                </tr>
            `).join('');
        } catch (error) {
            content.innerHTML = '<div class="alert alert-danger">Erro ao carregar os pedidos</div>';
        }
    }

    async loadUsersPage() {
        const content = document.getElementById('content');
        content.innerHTML = `
            <h2>Gerenciamento de Usuários</h2>
            <button class="btn btn-primary mb-3" onclick="app.showNewUserForm()">
                Novo Usuário
            </button>
            <div class="table-responsive">
                <table class="table">
                    <thead>
                        <tr>
                            <th>Nome</th>
                            <th>Email</th>
                            <th>Perfil</th>
                            <th>Status</th>
                            <th>Ações</th>
                        </tr>
                    </thead>
                    <tbody id="usersList"></tbody>
                </table>
            </div>
        `;

        try {
            const users = await api.getUsers();
            const usersList = document.getElementById('usersList');
            
            usersList.innerHTML = users.map(user => `
                <tr>
                    <td>${user.name}</td>
                    <td>${user.email}</td>
                    <td>${user.profile}</td>
                    <td>
                        <span class="badge ${user.active ? 'bg-success' : 'bg-danger'}">
                            ${user.active ? 'Ativo' : 'Inativo'}
                        </span>
                    </td>
                    <td class="action-buttons">
                        <button class="btn btn-sm btn-warning" onclick="app.editUser(${user.id})">
                            Editar
                        </button>
                        <button class="btn btn-sm btn-danger" onclick="app.deleteUser(${user.id})">
                            Excluir
                        </button>
                    </td>
                </tr>
            `).join('');
        } catch (error) {
            content.innerHTML = '<div class="alert alert-danger">Erro ao carregar usuários</div>';
        }
    }

    async loadProductsPage() {
        const content = document.getElementById('content');
        content.innerHTML = `
            <h2>Gerenciamento de Produtos</h2>
            <button class="btn btn-primary mb-3" onclick="app.showNewProductForm()">
                Novo Produto
            </button>
            <div class="table-responsive">
                <table class="table">
                    <thead>
                        <tr>
                            <th>Nome</th>
                            <th>Descrição</th>
                            <th>Preço</th>
                            <th>Tipo</th>
                            <th>Status</th>
                            <th>Ações</th>
                        </tr>
                    </thead>
                    <tbody id="productsList"></tbody>
                </table>
            </div>
        `;

        try {
            const products = await api.getProducts();
            const productsList = document.getElementById('productsList');
            
            productsList.innerHTML = products.map(product => `
                <tr>
                    <td>${product.name}</td>
                    <td>${product.description}</td>
                    <td>R$ ${product.price.toFixed(2)}</td>
                    <td>${product.type}</td>
                    <td>
                        <span class="badge ${product.available ? 'bg-success' : 'bg-danger'}">
                            ${product.available ? 'Disponível' : 'Indisponível'}
                        </span>
                    </td>
                    <td class="action-buttons">
                        <button class="btn btn-sm btn-warning" onclick="app.editProduct(${product.id})">
                            Editar
                        </button>
                        <button class="btn btn-sm btn-danger" onclick="app.deleteProduct(${product.id})">
                            Excluir
                        </button>
                    </td>
                </tr>
            `).join('');
        } catch (error) {
            content.innerHTML = '<div class="alert alert-danger">Erro ao carregar produtos</div>';
        }
    }

    // Métodos auxiliares
    addToCart(productId) {
        // Implementar lógica do carrinho
        alert('Funcionalidade em desenvolvimento');
    }

    viewOrder(orderId) {
        // Implementar visualização detalhada do pedido
        alert('Funcionalidade em desenvolvimento');
    }

    showNewUserForm() {
        // Implementar formulário de novo usuário
        alert('Funcionalidade em desenvolvimento');
    }

    editUser(userId) {
        // Implementar edição de usuário
        alert('Funcionalidade em desenvolvimento');
    }

    async deleteUser(userId) {
        if (confirm('Tem certeza que deseja excluir este usuário?')) {
            try {
                await api.deleteUser(userId);
                await this.loadUsersPage();
            } catch (error) {
                alert('Erro ao excluir usuário');
            }
        }
    }

    showNewProductForm() {
        // Implementar formulário de novo produto
        alert('Funcionalidade em desenvolvimento');
    }

    editProduct(productId) {
        // Implementar edição de produto
        alert('Funcionalidade em desenvolvimento');
    }

    async deleteProduct(productId) {
        if (confirm('Tem certeza que deseja excluir este produto?')) {
            try {
                await api.deleteProduct(productId);
                await this.loadProductsPage();
            } catch (error) {
                alert('Erro ao excluir produto');
            }
        }
    }
}

const app = new App(); 