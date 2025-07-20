// Incompleto, falta corrigir a estrutura para que realizei o update no banco de dados.

document.addEventListener('DOMContentLoaded', async () => {
    const token = getCookie('token');
    if (!token) {
        alert('Você precisa estar logado.');
        window.location.href = '/login.html';
        return;
    }

    try {
        // Busca os dados do usuário logado
        const res = await fetch('/api/users/', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (!res.ok) {
            throw new Error('Erro ao buscar dados do usuário');
        }

        const userData = await res.json();

        // Preenche os campos com os dados do usuário
        document.getElementById('${user.email}').value = userData.nome || '';
        document.getElementById('${user.email}').value = userData.email || '';

    } catch (err) {
        alert('Erro ao carregar dados do usuário.');
        console.error(err);
    }

    const form = document.querySelector('form');
    form.addEventListener('submit', async function (e) {
        e.preventDefault();

        const nome = document.getElementById('nome').value.trim();
        const email = document.getElementById('email').value.trim();
        const senha = document.getElementById('senha').value;
        const confirmarSenha = document.getElementById('confirmar-senha').value;

        if (senha !== confirmarSenha) {
            alert('⚠️ As senhas não coincidem!');
            return;
        }

        const payload = {
            nome,
            email,
            senha
        };

        try {
            const res = await fetch('/api/usuario/atualizar', {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(payload)
            });

            if (!res.ok) {
                const errorData = await res.json();
                alert(`Erro: ${errorData.message || 'Falha ao atualizar usuário'}`);
                return;
            }

            alert('✅ Dados atualizados com sucesso!');
            form.reset();

        } catch (err) {
            alert('Erro inesperado. Tente novamente mais tarde.');
            console.error(err);
        }
    });
});

function getCookie(nome) {
    const nameEQ = nome + "=";
    const cookies = document.cookie.split(';');
    for (let c of cookies) {
        while (c.charAt(0) === ' ') c = c.substring(1);
        if (c.indexOf(nameEQ) === 0) return c.substring(nameEQ.length);
    }
    return null;
}
