const API_URL = 'http://localhost:8080/api';
const imagemInput = document.getElementById("imagem");
const form = document.getElementById("form");

// Recupera ID do produto da URL
let productId = null;

document.addEventListener('DOMContentLoaded', function () {
    productId = new URLSearchParams(window.location.search).get('id');
    if (productId) {
        findProductsById(productId).then(products => {
            console.log(products);
        });
    }
});

async function findProductsById(productId) {
    try {
        const response = await fetch(`${API_URL}/produtos/${productId}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${auth.getToken()}`
            }
        });
        const data = await response.json();

        // Preenche o formulário com os dados
        document.getElementById("nome").value = data.nome;
        document.getElementById("descricao").value = data.descricao;
        document.getElementById("preco").value = parseFloat(data.preco);
        document.getElementById("tipo").value = data.tipo;
        document.getElementById("disponivel").checked = data.disponivel;

    } catch (error) {
        console.error("Erro ao buscar produto:", error);
    }
}

form.addEventListener("submit", async function (e) {
    e.preventDefault();

    // Pega os valores atualizados do formulário
    const nome = document.getElementById("nome").value;
    const descricao = document.getElementById("descricao").value;
    const preco = parseFloat(document.getElementById("preco").value);
    const tipo = document.getElementById("tipo").value;
    const disponivel = document.getElementById("disponivel").checked;

    const imagemFile = imagemInput.files[0];

    if (!imagemFile) {
        alert("Por favor, selecione uma imagem.");
        return;
    }

    const reader = new FileReader();

    reader.onloadend = async function () {
        const imagemBase64 = reader.result.split(',')[1];

        const produto = {
            nome,
            descricao,
            preco,
            tipo,
            disponivel,
            imagemBase64
        };

        console.log("Produto a ser enviado:", produto);

        try {
            const response = await fetch(`${API_URL}/produtos/${productId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${auth.getToken()}`
                },
                credentials: 'include',
                body: JSON.stringify(produto)
            });

            if (!response.ok) {
                const erro = await response.json();
                throw new Error(erro.message || "Erro ao atualizar produto.");
            }

            alert("Produto atualizado com sucesso!");
            form.reset();
        } catch (error) {
            console.error("Erro ao atualizar produto:", error);
            alert("Erro: " + error.message);
        }
    };

    reader.readAsDataURL(imagemFile);
});
