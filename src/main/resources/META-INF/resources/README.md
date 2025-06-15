# Sistema de Pedidos de Pizza

Este é um sistema web para gerenciamento de pedidos de pizza, desenvolvido com HTML, CSS e JavaScript no frontend, e Quarkus no backend.

## Funcionalidades

- Cadastro e login de usuários
- Visualização do cardápio de pizzas
- Filtro de pizzas por categoria
- Busca de pizzas por nome
- Carrinho de compras
- Finalização de pedidos
- Interface responsiva

## Estrutura do Projeto

```
frontend-pizzaria/
├── css/
│   └── styles.css
├── js/
│   └── script.js
├── img/
│   ├── hero-bg.jpg
│   ├── pizza-margherita.jpg
│   ├── pizza-pepperoni.jpg
│   ├── pizza-quatro-queijos.jpg
│   ├── pizza-frango-catupiry.jpg
│   ├── pizza-calabresa.jpg
│   ├── pizza-chocolate.jpg
│   └── pizzaria.jpg
├── index.html
├── cardapio.html
├── login.html
├── cadastro.html
└── README.md
```

## Requisitos

- Navegador web moderno
- Backend Quarkus configurado e rodando

## Instalação

1. Clone o repositório:
```bash
git clone [URL_DO_REPOSITORIO]
```

2. Navegue até a pasta do projeto:
```bash
cd frontend-pizzaria
```

3. Configure o backend:
- Certifique-se de que o backend Quarkus está rodando
- Verifique se as URLs das APIs no arquivo `script.js` estão corretas

4. Abra o arquivo `index.html` em seu navegador ou use um servidor local:
```bash
python -m http.server 8000
```

## Uso

1. Acesse a página inicial em `http://localhost:8000`
2. Navegue pelo cardápio de pizzas
3. Faça login ou cadastre-se para realizar pedidos
4. Adicione pizzas ao carrinho
5. Finalize seu pedido

## Tecnologias Utilizadas

- HTML5
- CSS3
- JavaScript (ES6+)
- Font Awesome (ícones)
- Google Fonts (Poppins)

## Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`)
3. Commit suas mudanças (`git commit -m 'Adiciona nova feature'`)
4. Push para a branch (`git push origin feature/nova-feature`)
5. Abra um Pull Request

## Licença

Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes. 