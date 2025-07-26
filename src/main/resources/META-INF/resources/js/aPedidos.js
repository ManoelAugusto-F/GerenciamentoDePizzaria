let pedidos = []

function onLoadPedidos(id, e) {
    updatePedidosFetch(id, e)
        .then(() => {
            getPedidos().then((res) => {
                pedidos = res.body
                renderPedidos();
            })
        })
        .catch(error => {
            console.error("Erro ao atualizar pedido:", error);
            // aqui você pode colocar um toast ou alerta se quiser
        });
}

function handleStatusChange(event) {
    const select = event.target;
    const id = select.getAttribute('data-id');
    const status = select.value;
    onLoadPedidos(id, status);
}


async function updatePedidosFetch(id, value) {
    try {
        const response = await fetch(`${API_URL}/pedidos/${id}/status`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${auth.getToken()}`
            },
            body: JSON.stringify(value)
        });
        if (!response.ok) {
            const errorText = await response.text();
            console.error(`HTTP ${response.status}: ${errorText}`);
            return;
        }
        await response.json();
    } catch (error) {
        console.error('Erro geral no getByemail:', error);
    }
}

function renderPedidos() {
    const container = document.getElementById('pedidos-container');

    let html = '';

    pedidos.forEach(pedido => {
        html += `
                    <div class="pedido-card">
                        <div class="pedido-header">
                            <span class="pedido-id">Pedido ${pedido.id}</span>
                            <span class="pedido-total">R$ ${pedido.total.toFixed(2)}</span>
                        </div>
                        
                        <div class="status-section">
                            <label class="status-label">Status do Pedido:</label>
                            ${auth.isAttendant() ?
            `<select class="status-select" data-id="${pedido.id}" onchange="handleStatusChange(event)">
                                    <option value="RECEBIDO" ${pedido.status === 'RECEBIDO' ? 'selected' : ''}>Recebido</option>
                                    <option value="PREPARANDO" ${pedido.status === 'PREPARANDO' ? 'selected' : ''}>Preparando</option>
                                    <option value="PRONTO" ${pedido.status === 'PRONTO' ? 'selected' : ''}>Pronto</option>
                                    <option value="ENTREGUE" ${pedido.status === 'ENTREGUE' ? 'selected' : ''}>Entregue</option>
                                </select>`
            :
            `<div class="status-text status-${pedido.status}">${pedido.status}</div>`
        }
                        </div>
                        
                        <div class="itens-section">
                            <div class="itens-title">Itens do Pedido:</div>
                            <ul class="itens-lista">
                                ${pedido.itens.map(item => `
                                    <li class="item">
                                        <div class="item-info">
                                            <div class="item-nome">${item.nomeProduto}</div>
                                            <div class="item-detalhes">Quantidade: ${item.quantity}</div>
                                        </div>
                                        <div class="item-preco">R$ ${item.preco.toFixed(2)}</div>
                                    </li>
                                `).join('')}
                            </ul>
                        </div>
                    </div>
                `;
    });

    container.innerHTML = html;
}


async function getPedidos() {
    try {
        const response = await fetch(`${API_URL}/pedidos`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${auth.getToken()}`
            }
        });

        if (!response.ok) {
            throw new Error('Erro ao buscar o carrinho');
        }

        const data = await response.json();
        pedidos = data;
    } catch (error) {
        console.error('Erro ao buscar itens do carrinho', error);
        pedidos = [];

    }
}


document.addEventListener('DOMContentLoaded', async function () {
    if (!auth.isAuthenticated() && !auth.isAttendant()) {
        window.location.href = '/error.html';
    }
    await getPedidos();
    if (pedidos.length > 0) {
        renderPedidos();
    } else {
        console.log('Nenhum pedido encontrado');
    }
});