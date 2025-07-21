document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById("form");
    const API_URL = 'http://localhost:8080/api';
    if (!form) {
        console.error("Formulário não encontrado!");
        return;
    }

    form.addEventListener("submit", async function (e) {
        e.preventDefault();

        const nome = document.getElementById("nome").value;
        const descricao = document.getElementById("descricao").value;
        const preco = parseFloat(document.getElementById("preco").value);
        const tipo = document.getElementById("tipo").value;
        const disponivel = document.getElementById("disponivel").checked;
        const imagemInput = document.getElementById("imagem");
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
                imagemBase64: imagemBase64
            };

            console.log("Produto a ser enviado:", produto);

            try {
                const response = await fetch(`${API_URL}/produtos`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${auth.getToken()}`
                    },
                    credentials: 'include',
                    body: JSON.stringify(produto)
                });

                if (!response.ok) {
                    const erro = await response.json();
                    throw new Error(erro.message || "Erro ao cadastrar produto.");
                }

                alert("Produto cadastrado com sucesso!");
                form.reset();
            } catch (error) {
                console.error("Erro ao cadastrar produto:", error);
                alert("Erro: " + error.message);
            }
        };

        reader.readAsDataURL(imagemFile);
    });
});