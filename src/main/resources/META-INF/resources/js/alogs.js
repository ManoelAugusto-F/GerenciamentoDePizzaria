class LogsManager {
    constructor() {
        this.logs = [];
        this.filteredLogs = [];
        this.currentPage = 1;
        this.itemsPerPage = 10;
        this.sortColumn = 'dataHora';
        this.sortDirection = 'desc';
        this.filters = {
            usuarioId: null,
            acao: '',
            dataInicio: '',
            dataFim: ''
        };
        this.init();
    }
    init() {
        console.debug('UsuarioId', this.filters.usuarioId);
        console.debug('ação', this.filters.acao);
        console.debug('Data Início', this.filters.dataInicio);
        console.debug('Data Fim', this.filters.dataFim);
        this.loadLogs();
    }

    // Carregar logs da API com dados completos do usuário
    async loadLogs() {
        try {
            this.showLoading(true);
            // Verificar autenticação
            if (typeof auth === 'undefined') {
                console.error('Objeto auth não encontrado!');
                this.showNotification('Sistema de autenticação não encontrado', 'error');
                return;
            }
            const token = auth.getToken();
            if (!token) {
                console.error('Token não encontrado');
                this.showNotification('Você precisa estar logado para ver os logs', 'error');
                return;
            }
            // Construir URL com parâmetros de filtro
            const params = new URLSearchParams();
            if (this.filters.usuarioId) {
                params.append('usuarioId', this.filters.usuarioId);
            }
            if (this.filters.acao) {
                params.append('acao', this.filters.acao);
            }
            if (this.filters.dataInicio) {
                params.append('dataInicio', this.filters.dataInicio);

            }
            if (this.filters.dataFim) {
                params.append('dataFim', this.filters.dataFim);
            }
            const url = `http://localhost:8080/api/logs`;
            const response = await fetch(url, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                }
            });
            if (!response.ok) {
                console.error('Resposta não OK:', response.status, response.statusText);
                // Tentar ler o corpo da resposta para mais detalhes
                try {
                    const errorText = await response.text();
                    console.error('Corpo da resposta de erro:', errorText);
                    this.showNotification(`Erro HTTP ${response.status}: ${errorText}`, 'error');
                } catch (e) {
                    console.error('Não foi possível ler o corpo da resposta de erro');
                    this.showNotification(`Erro HTTP ${response.status}: ${response.statusText}`, 'error');
                }
                return;
            }
            const responseData = await response.json();
            if (!Array.isArray(responseData)) {
                console.error('Resposta não é um array:', responseData);
                this.showNotification('Formato de resposta inválido da API', 'error');
                return;
            }
            this.logs = responseData;
            this.processLogs();
            this.updateSummary();
            this.renderTable();
            this.updatePagination();
            this.populateUserFilter();
        } catch (error) {
            console.error('=== ERRO NO CARREGAMENTO ===');
            console.error('Erro completo:', error);
            console.error('Stack trace:', error.stack);
            this.showNotification('Erro ao carregar logs: ' + error.message, 'error');
            this.logs = [];
            this.processLogs();
            this.renderTable();
        } finally {
            this.showLoading(false);
        }
    }

    // Processar logs recebidos da API (agora com dados completos do usuário)
    processLogs() {
        try {
            // Os dados já vêm no formato correto do DTO, apenas mapear para o formato do frontend
            this.logs = this.logs.map((log, index) => {
                const processedLog = {
                    id: log.id,
                    user: log.usuario.name,
                    datetime: log.dataHora,
                    action: log.acao,
                    description: log.descricao,
                    details: log.detalhes,
                    userID:log.usuario.id,
                    userEmail:log.usuario.email
                };
                return processedLog;
            });
            this.applyClientSideFilters();
        } catch (error) {
            console.error('Erro ao processar logs:', error);
            this.showNotification('Erro ao processar dados dos logs', 'error');
        }
    }
    // Aplicar filtros no lado do cliente (para ordenação e paginação)
    applyClientSideFilters() {
        this.filteredLogs = this.logs.filter(log => {
            const matchUsuario = !this.filters.usuarioId || log.userID.toString() === this.filters.usuarioId;
            const matchAcao = !this.filters.acao || log.action.includes(this.filters.acao);
            const matchDataInicio = !this.filters.dataInicio || new Date(log.datetime) >= new Date(this.filters.dataInicio);
            const matchDataFim = !this.filters.dataFim || new Date(log.datetime) <= new Date(this.filters.dataFim);
            return matchUsuario && matchAcao && matchDataInicio && matchDataFim;
        });
        // Reordena os logs conforme o sort atual
        this.filteredLogs.sort((a, b) => {
            const dir = this.sortDirection === 'asc' ? 1 : -1;
            if (a[this.sortColumn] < b[this.sortColumn]) return -1 * dir;
            if (a[this.sortColumn] > b[this.sortColumn]) return 1 * dir;
            return 0;
        });
    }

    populateUserFilter() {
        const userFilter = document.getElementById('filterUser');
        // Evita recriar o select se ele já existir
        if (document.getElementById('filterUserSelect')) return;
        const allUsers = this.logs
            .filter(log => log.userEmail && log.userID)
            .map(log => ({
                id: log.userID,
                email: log.userEmail || ''
            }));
        const uniqueUsers = allUsers.filter((user, index, self) =>
            index === self.findIndex(u => u.id === user.id)
        );
        const label = document.createElement('label');
        label.setAttribute('for', 'filterUserSelect');
        label.textContent = 'Usuário:';
        userFilter.appendChild(label);
        const select = document.createElement('select');
        select.id = 'filterUserSelect';
        const defaultOption = document.createElement('option');
        defaultOption.value = 'Todos';
        defaultOption.textContent = 'Todos os usuários';
        select.appendChild(defaultOption);
        uniqueUsers.forEach(user => {
            const option = document.createElement('option');
            option.value = user.id;
            option.textContent = user.email ? user.email : `ID: ${user.id}`;
            select.appendChild(option);
        });
        select.addEventListener('change', (e) => {
            const id = e.target.value;
            this.filters.usuarioId = id === 'Todos' ? null : id;
            this.currentPage = 1;
            this.applyClientSideFilters();
            this.renderTable();
            this.updatePagination();
        });
        userFilter.appendChild(select);
    }

    sortTable(column) {
        if (this.sortColumn === column) {
            this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
        } else {
            this.sortColumn = column;
            this.sortDirection = 'asc';
        }
        this.applyClientSideFilters();
        this.renderTable();
        this.updateSortIcons();
    }

    updateSortIcons() {
        // Resetar todos os ícones
        document.querySelectorAll('.sort-icon').forEach(icon => {
            icon.className = 'fas fa-sort sort-icon';
        });
        // Atualizar o ícone da coluna atual
        const currentHeader = document.querySelector(`th[onclick="sortTable('${this.sortColumn}')"] .sort-icon`);
        if (currentHeader) {
            currentHeader.className = `fas fa-sort-${this.sortDirection === 'asc' ? 'up' : 'down'} sort-icon`;
        }
    }

    renderTable() {
        const tbody = document.getElementById('logsBody');
        if (!tbody) {
            console.error('Elemento logsBody não encontrado!');
            return;
        }
        const startIndex = (this.currentPage - 1) * this.itemsPerPage;
        const endIndex = startIndex + this.itemsPerPage;
        const pageData = this.filteredLogs.slice(startIndex, endIndex);
        if (pageData.length === 0) {
            tbody.innerHTML = `
                <tr class="no-data">
                    <td colspan="5">
                        <div class="no-data-message">
                            <i class="fas fa-inbox"></i>
                            <p>Nenhum log encontrado</p>
                            <small>Tente ajustar o filtro para ver mais resultados</small>
                        </div>
                    </td>
                </tr>
            `;
            return;
        }

        const tableHTML = pageData.map(log =>
            `
            <tr>
                <td>
                    <div class="user-info">
                        <i class="fas fa-user"></i>
                        <span class="user-name">${log.user}</span>
                    </div>
                </td>
                <td>
                    <i class="fas fa-clock"></i>
                    ${this.formatDateTime(log.datetime)}
                </td>
                <td>
                    <span class="action-${log.action}">
                        <i class="fas ${this.getActionIcon(log.action)}"></i>
                        ${this.getActionLabel(log.action)}
                    </span>
                </td>
                <td>${log.description}</td>
                <td class="actions-column">
                    <button class="action-btn" onclick="logsManager.showLogDetails(${log.id})" title="Ver detalhes">
                        <i class="fas fa-eye"></i>
                    </button>
                </td>
            </tr>
        `
        ).join('');
        tbody.innerHTML = tableHTML;
        this.updateSortIcons();
    }

    formatDateTime(datetime) {
        if (!datetime) return 'Data inválida';
        try {
            const date = new Date(datetime);
            return date.toLocaleString('pt-BR', {
                day: '2-digit',
                month: '2-digit',
                year: 'numeric',
                hour: '2-digit',
                minute: '2-digit'
            });
        } catch (error) {
            console.error('Erro ao formatar data:', datetime, error);
            return 'Data inválida';
        }
    }

    getActionIcon(action) {
        const icons = {
            login: 'fa-sign-in-alt',
            logout: 'fa-sign-out-alt',
            update: 'fa-edit',
            delete: 'fa-trash',
            create: 'fa-plus',
            view: 'fa-eye'
        };
        return icons[action] || 'fa-cog';
    }

    getActionLabel(action) {
        const labels = {
            login: 'Login',
            logout: 'Logout',
            update: 'Atualização',
            delete: 'Exclusão',
            create: 'CRIAR',
            view: 'Visualização'
        };
        return labels[action] || action;
    }

    updateSummary() {
        const total = this.logs.length;
        const updates = this.logs.filter(log => log.action === 'ATUALIZAR').length;
        const deletes = this.logs.filter(log => log.action === 'DELETAR').length;
        const criar = this.logs.filter(log => log.action === 'CRIAR').length;

        const elementMap = {
            totalLogs: total,
            totalUpdates: updates,
            totalDeletes: deletes,
            totalCriar: criar,
        };

        for (const [id, value] of Object.entries(elementMap)) {
            const el = document.getElementById(id);
            if (el) el.innerText = String(value);
        }
    }

    updatePagination() {
        const totalPages = Math.ceil(this.filteredLogs.length / this.itemsPerPage);
        const startItem = (this.currentPage - 1) * this.itemsPerPage + 1;
        const endItem = Math.min(this.currentPage * this.itemsPerPage, this.filteredLogs.length);
        const paginaAtualElement = document.getElementById('paginaAtual');
        const paginationInfoElement = document.getElementById('paginationInfo');
        const anteriorBtn = document.getElementById('anteriorBtn');
        const proximaBtn = document.getElementById('proximaBtn');
        if (paginaAtualElement) {
            paginaAtualElement.textContent = `Página ${this.currentPage} de ${totalPages}`;
        }
        if (paginationInfoElement) {
            paginationInfoElement.textContent = `Mostrando ${startItem}-${endItem} de ${this.filteredLogs.length} registros`;
        }
        if (anteriorBtn) {
            anteriorBtn.disabled = this.currentPage <= 1;
        }
        if (proximaBtn) {
            proximaBtn.disabled = this.currentPage >= totalPages;
        }
    }

    changePage(direction) {
        const totalPages = Math.ceil(this.filteredLogs.length / this.itemsPerPage)
        if (direction === -1 && this.currentPage > 1) {
            this.currentPage--;
        } else if (direction === 1 && this.currentPage < totalPages) {
            this.currentPage++;
        }
        this.renderTable();
        this.updatePagination();
    }

    async showLogDetails(logId) {
        try {
            const token = auth.getToken();
            if (!token) {
                this.showNotification('Token não encontrado', 'error');
                return;
            }
            const response = await fetch(`/logs/${logId}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                }
            });
            if (!response.ok) {
                throw new Error(`Erro HTTP: ${response.status}`);
            }
            const log = await response.json();
            // aqui ele cria os objeto de criação da pagina
            const logFormatted = {
                id: log.id,
                datetime: log.dataHora,
                action: log.acao,
                description: log.descricao,
                details:logs.detalhes,
                usuario:{
                    name:logs.usuario.name,
                    email:logs.usuario.email,
                    id:logs.usuario.id
                }
            };
            const modal = document.getElementById('logModal');
            const modalBody = document.getElementById('logModalBody');
            if (!modal || !modalBody) {
                console.error('Elementos do modal não encontrados');
                return;
            }
            modalBody.innerHTML = `
                <div class="log-detail-section">
                    <h4><i class="fas fa-info-circle"></i> Informações Básicas</h4>
                    <div class="detail-grid">
                        <div class="detail-item">
                            <strong>ID:</strong>
                            <span>${logFormatted.id}</span>
                        </div>
                        <div class="detail-item">
                            <strong>Data/Hora:</strong>
                            <span>${this.formatDateTime(logFormatted.datetime)}</span>
                        </div>
                        <div class="detail-item">
                            <strong>Ação:</strong>
                            <span class="action-${logFormatted.action}">
                                <i class="fas ${this.getActionIcon(logFormatted.action)}"></i>
                                ${this.getActionLabel(logFormatted.action)}
                            </span>
                        </div>
                        <div class="detail-item full-width">
                            <strong>Descrição:</strong>
                            <span>${logFormatted.description}</span>
                        </div>
                    </div>
                </div>
                <div class="log-detail-section">
                    <h4><i class="fas fa-user"></i> Informações do Usuário</h4>
                    <div class="detail-grid">
                        <div class="detail-item">
                            <strong>ID do Usuário:</strong>
                            <span>${logFormatted.usuarioId || 'N/A'}</span>
                        </div>
                        <div class="detail-item">
                            <strong>Nome:</strong>
                            <span>${logFormatted.usuarioNome || 'N/A'}</span>
                        </div>
                        <div class="detail-item">
                            <strong>Email:</strong>
                            <span>${logFormatted.usuarioEmail || 'N/A'}</span>
                        </div>
                        <div class="detail-item">
                            <strong>Perfil:</strong>
                            <span>${logFormatted.usuarioPerfil || 'N/A'}</span>
                        </div>
                    </div>
                </div>
                <div class="log-detail-section">
                      <h4><i class="fas fa-cogs"></i> Detalhes Técnicos</h4>
                      <div class="detail-code">
                        ${
                            logFormatted.details
                            ? `<pre><code>${JSON.stringify(logFormatted.details, null, 2)}</code></pre>`
                            : `<p>Vazio</p>`
                        }
                      </div>
                </div>
                `;
            modal.style.display = 'block';
            document.body.style.overflow = 'hidden';
        } catch (error) {
            console.error('Erro ao carregar detalhes do log:', error);
            this.showNotification('Erro ao carregar detalhes do log: ' + error.message, 'error');
        }
    }

    closeLogModal() {
        const modal = document.getElementById('logModal');
        if (modal) {
            modal.style.display = 'none';
            document.body.style.overflow = 'auto';
        }
    }

    async clearFilters() {
        const filterUserSelect = document.getElementById('filterUserSelect');
        const filterAction = document.getElementById('filterAction');
        const filterDate = document.getElementById('filterDate');
        if (filterUserSelect) filterUserSelect.value = 'Todos';
        if (filterAction) filterAction.value = '';
        if (filterDate) filterDate.value = '';
        this.filters = { usuarioId: null, acao: '', dataInicio: '', dataFim: '' };
        this.currentPage = 1;
        // Atualiza os filtros e re-renderiza
        this.applyClientSideFilters();
        this.renderTable();
        this.updatePagination();
        this.updateSummary();
    }

    async refreshLogs() {
        this.showNotification('Atualizando logs...', 'info');
        await this.loadLogs();
        this.showNotification('Logs atualizados com sucesso!', 'success');
    }

    exportLogs() {
        const dataToExport = this.filteredLogs.map(log => ({
            ID: log.id,
            'Usuário': log.user,
            'Email': log.usuarioEmail || '',
            'Perfil': log.usuarioPerfil || '',
            'Data/Hora': log.datetime,
            'Ação': this.getActionLabel(log.action),
            'Descrição': log.description
        }));
        const csv = this.convertToCSV(dataToExport);
        const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
        const link = document.createElement('a');
        if (link.download !== undefined) {
            const url = URL.createObjectURL(blob);
            link.setAttribute('href', url);
            link.setAttribute('download', `logs_${new Date().toISOString().split('T')[0]}.csv`);
            link.style.visibility = 'hidden';
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
        }
        this.showNotification('Logs exportados com sucesso!', 'success');
    }

    convertToCSV(data) {
        if (!data.length) return '';
        const headers = Object.keys(data[0]);
        const csvContent = [
            headers.join(','),
            ...data.map(row =>
                headers.map(header => {
                    const value = row[header];
                    return typeof value === 'string' && value.includes(',')
                        ? `"${value}"`
                        : value;
                }).join(',')
            )
        ].join('\n');
        return csvContent;
    }

    showLoading(show) {
        const logsSection = document.querySelector('#logs-container');
        if (logsSection) {
            if (show) {
                logsSection.classList.add('loading');
            } else {
                logsSection.classList.remove('loading');
            }
        }
    }

    showNotification(message, type = 'info') {
        // Criar elemento de notificação
        const notification = document.createElement('div');
        notification.className = `notification notification-${type}`;
        notification.innerHTML = `
            <i class="fas ${type === 'success' ? 'fa-check-circle' : type === 'error' ? 'fa-exclamation-circle' : 'fa-info-circle'}"></i>
            <span>${message}</span>
            <button onclick="this.parentElement.remove()">
                <i class="fas fa-times"></i>
            </button>
        `;

        // Adicionar estilos se não existirem
        if (!document.querySelector('#notification-styles')) {
            const style = document.createElement('style');
            style.id = 'notification-styles';
            style.textContent = `
                .notification {
                    position: fixed;
                    top: 20px;
                    right: 20px;
                    background: white;
                    padding: 15px 20px;
                    border-radius: 8px;
                    box-shadow: 0 4px 12px rgba(0,0,0,0.15);
                    display: flex;
                    align-items: center;
                    gap: 10px;
                    z-index: 3000;
                    animation: slideInRight 0.3s ease;
                    max-width: 400px;
                }
                .notification-success {
                    border-left: 4px solid #28a745;
                    color: #28a745;
                }
                .notification-error {
                    border-left: 4px solid #dc3545;
                    color: #dc3545;
                }
                .notification-info {
                    border-left: 4px solid #17a2b8;
                    color: #17a2b8;
                }
                .notification button {
                    background: none;
                    border: none;
                    cursor: pointer;
                    opacity: 0.7;
                    margin-left: auto;
                }
                @keyframes slideInRight {
                    from { transform: translateX(100%); opacity: 0; }
                    to { transform: translateX(0); opacity: 1; }
                }
                /* Estilos para informações do usuário na tabela */
                .user-info {
                    display: flex;
                    align-items: center;
                    gap: 10px;
                }
                .user-details {
                    display: flex;
                    flex-direction: column;
                    gap: 2px;
                }
                .user-name {
                    font-weight: 600;
                    color: var(--secondary-color);
                }
                .user-email {
                    font-size: 0.85rem;
                    color: var(--gray);
                }
                .user-role {
                    font-size: 0.75rem;
                    background: var(--primary-color);
                    color: white;
                    padding: 2px 6px;
                    border-radius: 10px;
                    display: inline-block;
                    width: fit-content;
                }
                @media (max-width: 768px) {
                    .user-email, .user-role {
                        display: none;
                    }
                }
            `;
            document.head.appendChild(style);
        }

        document.body.appendChild(notification);
        setTimeout(() => {
            if (notification.parentElement) {
                notification.remove();
            }
        }, 5000);
    }
}

let logsManager;

function initializeLogs() {
    try {
        logsManager = new LogsManager();
    } catch (error) {
        console.error('Erro ao criar LogsManager:', error);
    }
}

function changePage(direction) {
    if (logsManager) {
        logsManager.changePage(direction);
    } else {
        console.error('logsManager não está inicializado');
    }
}

function sortTable(column) {
    if (logsManager) {
        logsManager.sortTable(column);
    } else {
        console.error('logsManager não está inicializado');
    }
}

function clearFilters() {
    if (logsManager) {
        logsManager.clearFilters();
    } else {
        console.error('logsManager não está inicializado');
    }
}

function refreshLogs() {
    if (logsManager) {
        logsManager.refreshLogs();
    } else {
        console.error('logsManager não está inicializado');
    }
}

function exportLogs() {
    if (logsManager) {
        logsManager.exportLogs();
    } else {
        console.error('logsManager não está inicializado');
    }
}

function closeLogModal() {
    if (logsManager) {
        logsManager.closeLogModal();
    } else {
        console.error('logsManager não está inicializado');
    }
}

// CSS adicional para detalhes do modal
const modalStyles = `
    .log-detail-section {
        margin-bottom: 25px;
    }
    .log-detail-section h4 {
        color: var(--secondary-color);
        margin-bottom: 15px;
        padding-bottom: 8px;
        border-bottom: 2px solid var(--light-gray);
        display: flex;
        align-items: center;
        gap: 8px;
    }
    .detail-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
        gap: 15px;
    }
    .detail-item {
        display: flex;
        flex-direction: column;
        gap: 5px;
    }
    .detail-item.full-width {
        grid-column: 1 / -1;
    }
    .detail-item strong {
        color: var(--gray);
        font-size: 0.9rem;
        font-weight: 600;
    }
    .detail-item span {
        color: var(--secondary-color);
        font-weight: 500;
    }
    .detail-code {
        background-color: #f8f9fa;
        border: 1px solid var(--light-gray);
        border-radius: 6px;
        padding: 15px;
        overflow-x: auto;
    }
    .detail-code pre {
        margin: 0;
        font-family: 'Courier New', monospace;
        font-size: 0.85rem;
        line-height: 1.4;
        color: var(--secondary-color);
    }
    @media (max-width: 768px) {
        .detail-grid {
            grid-template-columns: 1fr;
        }
    }
`;

const styleSheet = document.createElement('style');
styleSheet.textContent = modalStyles;
document.head.appendChild(styleSheet);