class Auth {
    constructor() {

        this.token = localStorage.getItem('token');
        this.user = JSON.parse(localStorage.getItem('user'));
        this.setupEventListeners();
    }

    setupEventListeners() {
        const loginForm = document.getElementById('loginForm');
        const logoutBtn = document.getElementById('logoutBtn');

        if (loginForm) {
            loginForm.addEventListener('submit', (e) => this.handleLogin(e));
        }

        if (logoutBtn) {
            logoutBtn.addEventListener('click', () => this.handleLogout());
        }
    }

    async handleLogin(e) {
        e.preventDefault();
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;

        try {
            const response = await fetch('/api/usuarios/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ email, password })
            });

            const data = await response.json();
            if (!response.ok) {
                throw new Error(data.erro || 'Credenciais inválidas');
            }

            // Como o novo endpoint retorna apenas mensagem, você pode buscar os dados do usuário após o login
            // ou ajustar o backend para retornar o usuário e perfil. Por enquanto, só mostra mensagem de sucesso.
            alert(data.mensagem || 'Login realizado com sucesso!');
            window.location.href = '/'; // Redireciona para a página inicial ou dashboard
        } catch (error) {
            alert(error.message);
        }
    }

    handleLogout() {
        this.clearSession();
        window.location.href = '/';
    }

    setSession(data) {
        this.token = data.token;
        this.user = data.user;
        localStorage.setItem('token', data.token);
        localStorage.setItem('user', JSON.stringify(data.user));
        this.updateUI();
    }

    clearSession() {
        this.token = null;
        this.user = null;
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        this.updateUI();
    }

    updateUI() {
        const userInfo = document.getElementById('userInfo');
        const adminElements = document.querySelectorAll('.admin-only');
        const loginTemplate = document.getElementById('login-template');

        if (this.isAuthenticated()) {
            if (userInfo) {
                userInfo.textContent = `Olá, ${this.user.nomeCompleto} (${this.user.perfil})`;
            }
            adminElements.forEach(el => {
                el.style.display = this.isAdmin() ? 'block' : 'none';
            });
            if (loginTemplate) {
                loginTemplate.style.display = 'none';
            }
        } else {
            if (userInfo) {
                userInfo.textContent = '';
            }
            adminElements.forEach(el => {
                el.style.display = 'none';
            });
            if (loginTemplate) {
                loginTemplate.style.display = 'block';
            }
        }
    }

    isAuthenticated() {
        return !!this.token;
    }

    isAdmin() {
        return this.user?.profile === 'ADMIN';
    }

    getToken() {
        return this.token;
    }
}

const auth = new Auth();

// Captura global de erros no frontend e envia para o backend
window.onerror = function(message, source, lineno, colno, error) {
    const log = {
        message,
        source,
        lineno,
        colno,
        stack: error && error.stack ? error.stack : null
    };
    fetch('/api/logs/frontend', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(log)
    });
}; 