const API_URL = 'http://localhost:8080/api';
let allUsers = [];
let filteredUsers = [];
let currentPage = 1;
const pageSize = 15;

function renderUsers(users) {
    const tbody = document.querySelector('#userTable tbody');
    tbody.innerHTML = '';

    const start = (currentPage - 1) * pageSize;
    const end = start + pageSize;
    const paginatedUsers = users.slice(start, end);

    if (paginatedUsers.length === 0) {
        tbody.innerHTML = `<tr><td colspan="3" style="text-align:center;">Nenhum usuário encontrado.</td></tr>`;
        return;
    }

    const rolesOptions = ['ADMIN', 'USER', 'ATENDENTE'];

    paginatedUsers.forEach(user => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${user.name}</td>
            <td>${user.email}</td>
            <td>
                <select class="roleFilter" id="roleFilter-${user.email}">
                    ${rolesOptions.map(role => `
                        <option value="${role}" ${role === user.roles ? 'selected' : ''}>${role}</option>
                    `).join('')}
                </select>
            </td>
        `;
        tbody.appendChild(tr);

        // Adiciona o event listener aqui, já que o elemento acabou de ser criado
        tr.querySelector('select.roleFilter').addEventListener('change', (e) => changeUserRole(e.target, user.email));
    });

    // Atualiza info de página
    const totalPages = Math.ceil(users.length / pageSize);
    document.getElementById('pageInfo').textContent = `Página ${currentPage} de ${totalPages}`;
    document.getElementById('prevPage').disabled = currentPage === 1;
    document.getElementById('nextPage').disabled = currentPage >= totalPages;
}



async function changeUserRole(element, email) {
    const newRole = element.value;

    try {
        const token = auth.getToken();
        if (!token) throw new Error('Usuário não autenticado');

        const response = await fetch(`${API_URL}/users/${email}/roles`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`,
            },
            credentials: 'include',
            body: JSON.stringify({ roles: newRole }),
        });

        if (!response.ok) {
            throw new Error('Erro ao atualizar o papel do usuário');
        }

        alert('Papel do usuário atualizado com sucesso!');
        fetchUsers(); // Atualiza a lista
    } catch (error) {
        showError(error.message);
    }
}

function applyFilters() {
    const searchEmail = document.getElementById('searchEmail').value.toLowerCase();

    filteredUsers = allUsers.filter(user =>
        user.email.toLowerCase().includes(searchEmail)
    );

    currentPage = 1;
    renderUsers(filteredUsers);
}

function changePage(direction) {
    currentPage += direction;
    renderUsers(filteredUsers);
}

function onSubmitSearch(e) {
    e.preventDefault();
    applyFilters();
    return false;
}

async function fetchUsers() {
    try {
        const token = auth.getToken();
        if (!token) throw new Error('Token inválido ou não autenticado');
        const response = await fetch(`${API_URL}/users`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`,
            },
            credentials: 'include',
        });

        if (!response.ok) {
            throw new Error('Erro ao buscar usuários');
        }

        allUsers = await response.json();
        filteredUsers = [...allUsers];
        renderUsers(filteredUsers);
    } catch (error) {
        showError(error.message);
    }
}

function showError(message) {
    alert(message);
}

window.onload = () => {
    fetchUsers();
    document.getElementById('prevPage').addEventListener('click', () => changePage(-1));
    document.getElementById('nextPage').addEventListener('click', () => changePage(1));
};
