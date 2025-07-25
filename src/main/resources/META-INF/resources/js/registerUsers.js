document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('create-user-form');
    const cancelButton = document.getElementById('cancel-create-user-btn');
    const profileSelect = document.getElementById('create-profile');

    const userInfo = document.getElementById('userInfo');
    const adminElements = document.querySelectorAll('.admin-only');
    const loginTemplate = document.getElementById('login-template');

    if (!form || !profileSelect) {
        console.error('Elementos essenciais do formulário (form ou select de perfil) não foram encontrados.');
        return;
    }
    async function populateProfiles() {
        try {
            console.warn("Simulação: Usando perfis fixos. Integre com 'api.getProfiles()'.");
            const profiles = ['ADMIN', 'ATENDENTE', 'CLIENTE'];

            profileSelect.innerHTML = '';

            profiles.forEach(profile => {
                const option = document.createElement('option');
                option.value = profile;
                option.textContent = profile.charAt(0).toUpperCase() + profile.slice(1);
                profileSelect.appendChild(option);
            });

        } catch (error) {
            console.error('Falha ao carregar perfis de usuário:', error);
            profileSelect.innerHTML = '<option value="">Erro ao carregar</option>';
            profileSelect.disabled = true;
        }
    }

    async function handleFormSubmit(event) {
        event.preventDefault();
        const formData = new FormData(form);
        const newUser = {
            name: formData.get('name'),
            email: formData.get('email'),
            password: formData.get('password'),
            profile: formData.get('profile'),
            active: formData.get('active') === 'on'
        };

        if (!newUser.name || !newUser.email || !newUser.password || !newUser.profile) {
            alert('Por favor, preencha todos os campos.');
            return;
        }

        try {
            if (typeof api === 'undefined' || typeof api.createUser !== 'function') {
                throw new Error('Função api.createUser() não encontrada.');
            }

            const createdUser = await api.createUser(newUser);

            alert(`Usuário "${createdUser.name}" criado com sucesso!`);
            form.reset();

            if (typeof app !== 'undefined' && typeof app.loadUsersPage === 'function') {
                app.loadUsersPage();
            }
        } catch (error) {
            console.error('Falha ao criar usuário:', error);
            alert(`Erro ao criar usuário: ${error.message}.`);
        }
    }

    form.addEventListener('submit', handleFormSubmit);
    if (cancelButton) {
        cancelButton.addEventListener('click', () => {
            form.reset();
        });
    }
    populateProfiles();
});