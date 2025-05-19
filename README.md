# Sistema de Gerenciamento de Pizzaria

Este é um sistema web completo para gerenciamento de pizzaria, desenvolvido com Java EE (Quarkus) e frontend em HTML/CSS/JavaScript.

## 🚀 Tecnologias Utilizadas

- **Backend:**
  - Java EE 11
  - Quarkus Framework
  - JAX-RS para APIs RESTful
  - Hibernate ORM com Panache
  - PostgreSQL
  - JWT para autenticação

- **Frontend:**
  - HTML5
  - CSS3
  - JavaScript (Vanilla)
  - Bootstrap 5

## 📋 Funcionalidades

### 1. Autenticação e Controle de Acesso
- Login com email e senha
- Três níveis de acesso:
  - Administrador
  - Atendente
  - Cliente

### 2. Gestão de Usuários
- Cadastro, edição e exclusão de usuários
- Atribuição de perfis
- Controle de status (ativo/inativo)

### 3. Gestão de Produtos
- Cadastro de pizzas, bebidas e extras
- Controle de preços e disponibilidade
- Upload de imagens dos produtos

### 4. Gestão de Pedidos
- Criação de pedidos
- Acompanhamento de status
- Histórico de pedidos por cliente
- Cálculo automático de valores

### 5. Logs do Sistema
- Registro de todas as ações
- Rastreamento de alterações
- Auditoria completa

## 🔧 Configuração do Ambiente

### Pré-requisitos
- Java 17 ou superior
- Maven 3.8+
- PostgreSQL 12+
- Node.js 14+ (para desenvolvimento frontend)

### Configuração do Banco de Dados
1. Crie um banco de dados PostgreSQL:
```sql
CREATE DATABASE pizzeria;
```

2. Configure as credenciais no arquivo `application.properties`

### Executando o Projeto
1. Clone o repositório
2. Execute o backend:
```bash
./mvnw quarkus:dev
```

3. Acesse a aplicação em `http://localhost:8080`

## 📚 Documentação da API

A documentação completa da API está disponível em:
- Swagger UI: `http://localhost:8080/q/swagger-ui`
- OpenAPI: `http://localhost:8080/q/openapi`

## 🔐 Segurança

- Autenticação via JWT
- Senhas criptografadas
- Controle de acesso baseado em perfis
- Validação de dados
- Proteção contra CSRF

## 📝 Logs

O sistema mantém logs detalhados de todas as operações:
- Ação realizada
- Usuário responsável
- Data e hora
- Detalhes da operação

## 🤝 Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes. 