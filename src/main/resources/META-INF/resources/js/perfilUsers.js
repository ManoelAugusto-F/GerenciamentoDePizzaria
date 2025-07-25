const profileForm = document.getElementById('formUser');
const nomeInput = document.getElementById('nome');
const emailInput = document.getElementById('email');
const senhaInput = document.getElementById('senha');
const token = auth.getToken();
const confirmarSenhaInput = document.getElementById('confirmar-senha');
const API_URL = 'http://localhost:8080/api/users';
let user = {
    nome: 'Seu nome completo',
    email: 'seu@email.com',
}
async function getByemail() {
    const id = auth.GetIdByJwt();
    try {
        const response = await fetch(`${API_URL}/id/${id}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        });
        if (!response.ok) {
            const errorText = await response.text();
            console.error(`HTTP ${response.status}: ${errorText}`);
            return;
        }
        const data = await response.json();
        user = {
            id: data.id,
            name: data.name,
            email: data.email,
        }
        nomeInput.placeholder = user.name;
        emailInput.placeholder = user.email;
    } catch (error) {
        console.error('Erro geral no getByemail:', error);
    }
}
document.addEventListener('DOMContentLoaded', function () {
    if(!auth.getToken()){
        window.location.href = '/login.html';
    }
    getByemail().then(r => console.log(r));
});
    profileForm.addEventListener('submit', function (event) {
        event.preventDefault();
        const novaSenha = senhaInput.value;
        const confirmarSenha = confirmarSenhaInput.value;
        if( !(nomeInput && emailInput) || !(senhaInput && confirmarSenhaInput)) {
            alert('preencha o formulario')
            return;
        }
        const dadosParaAtualizar = {
            id:user.id,
            name: nomeInput.value.trim()  ,
            email: emailInput.value.trim() ,
        };
        if (novaSenha || confirmarSenha) {
            if (novaSenha !== confirmarSenha) {
                alert('As senhas não coincidem. Por favor, verifique e tente novamente.');
                return; // Interrompe o envio
            }
            if (novaSenha.length < 8) {
                alert('A nova senha deve ter pelo menos 8 caracteres.');
                return;
            }
            dadosParaAtualizar.password = novaSenha;
        }
        fetch(`${API_URL}/update/self`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(dadosParaAtualizar)
        })
            .then(response => {
                if (!response.ok) {
                    return response.text().then(text => {
                        throw new Error(text || 'Falha na comunicação com o servidor.')
                    });
                }
                return response.json();
            })
            .then(data => {
                alert('Perfil atualizado com sucesso!');
                console.log('data', data);
            })
            .catch(error => {
                console.error('Erro ao atualizar o perfil:', error);
                alert(`Ocorreu um erro: ${error.message}`);
            });
    });