const API_URL = 'http://localhost:8080/api';
let allUsers = [];
let filteredUsers = [];
let currentPage = 1;
const pageSize = 15;
const searchForm = document.getElementById('searchForm')
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
    //alterar posteriormente para o do teu backend
    const select = ['Admin', 'User', 'Atendente'];

    paginatedUsers.forEach(user => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${user.name}</td>
            <td>${user.email}</td>
            <td>
             <select name="roles" id="roleFilter" onchange="changeUserRole('${user.email}', '${user.roles}')">
                   ${select.map(role => `
                    <option value="${role}" ${role.toUpperCase() === user.roles ? 'selected' : ''}>${role}</option>
                `).join('')}
                </select>
</td>
        `;
        tbody.appendChild(tr);
    });

    // Atualiza info de página
    const totalPages = Math.ceil(users.length / pageSize);
    document.getElementById('pageInfo').textContent = `Página ${currentPage} de ${totalPages}`;
    document.getElementById('prevPage').disabled = currentPage === 1;
    document.getElementById('nextPage').disabled = currentPage >= totalPages;
}

const changeUserRole = async (email, currentRoles) => {
    const newRole = document.getElementById('roleFilter').value;
    if (newRole === currentRoles) {
        return; // Nenhuma alteração necessária
    }

    try {
        const token = localStorage.getItem('token');
        const response = await fetch(`${API_URL}/users/${email}/roles`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({ roles: newRole.toUpperCase() })
        });

        if (!response.ok) {
            throw new Error('Erro ao atualizar o papel do usuário');
        }

        alert('Papel do usuário atualizado com sucesso!');
        fetchUsers(); // Recarrega a lista de usuários
    } catch (error) {
        showError(error.message);
    }
}

function applyFilters() {
    const searchEmail = document.getElementById('searchEmail').value.toLowerCase();
    const selectedRole = document.getElementById('roleFilter').value;

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
        const token = localStorage.getItem('token');
        const response = await fetch(`${API_URL}/users`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
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
    alert(message); // Pode substituir por div de erro, se quiser.
}

window.onload = () => {
    fetchUsers();

    // document.getElementById('searchForm').addEventListener('input', applyFilters);
    document.getElementById('roleFilter').addEventListener('change', applyFilters);
};




