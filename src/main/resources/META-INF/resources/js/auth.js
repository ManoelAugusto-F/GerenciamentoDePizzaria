class Auth {
    constructor() {
        this.token = this.getCookie('token');
        this.user = this.getUserRoleByJtw();
    }

    getCookie(nome) {
        const nameEQ = nome + "=";
        const cookies = document.cookie.split(';');
        for (let i = 0; i < cookies.length; i++) {
            let cookie = cookies[i];
            while (cookie.charAt(0) === ' ') {
                cookie = cookie.substring(1, cookie.length);
            }
            if (cookie.indexOf(nameEQ) === 0) {
                return cookie.substring(nameEQ.length, cookie.length);
            }
        }
        return null;
    }

    getUserRoleByJtw() {
        const token = this.getCookie('token');
        if (!token) return null;

        const payload = this.decodeJWT(token);
        return payload?.groups || null;
    }

    decodeJWT(token) {
        if (!token) return null;

        const parts = token.split('.');
        if (parts.length !== 3) {
            console.error('Token JWT inválido');
            return null;
        }

        try {
            const payload = parts[1];
            const decodedPayload = atob(payload.replace(/-/g, '+').replace(/_/g, '/'));
            return JSON.parse(decodedPayload);
        } catch (e) {
            console.error('Erro ao decodificar JWT:', e);
            return null;
        }
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

    handleLogout() {
        this.removerCookie('token');
        this.clearSession();
        window.location.href = '/';
    }

    removerCookie(nome) {
        document.cookie = `${nome}=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;`;
    }

    setSession(data) {
        this.token = data.token;
        this.user = data.user;
        this.updateUI();
    }

    clearSession() {
        this.token = null;
        this.user = null;
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
        if (!this.getToken()) {
            return false;
        }
        return this.user[0] === 'ADMIN';
    }

    isAttendant() {
        if (!this.getToken()) {
            return false;
        }
        return this.user[0] === 'ATENDENTE';
    }

    getToken() {
        return this.token;
    }
}

const auth = new Auth();

// Captura global de erros do frontend e envia para o backend
window.onerror = function (message, source, lineno, colno, error) {
    const log = {
        message,
        source,
        lineno,
        colno,
        stack: error && error.stack ? error.stack : null
    };
    fetch('/api/logs/frontend', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(log)
    });
};