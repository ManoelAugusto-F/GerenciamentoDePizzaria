# Sistema de Gerenciamento de Pizzaria

Este é um sistema web completo para gerenciamento de pizzaria, desenvolvido com Java EE (Quarkus) e frontend em HTML/CSS/JavaScript.

## 🚀 Tecnologias Utilizadas

- **Backend:**
  - Java EE 17
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

### 2. Gestão de Usuários (Administrador)
- Cadastro, edição e exclusão de usuários
- Atribuição de perfis
- Controle de status (ativo/inativo)

### 3. Gestão de Produtos (Administrador)
- Cadastro de pizzas, bebidas e extras
- Controle de preços e disponibilidade
- Upload de imagens dos produtos

### 4. Gestão de Pedidos
- Criação de pedidos (Cliente, Atendente)
- Acompanhamento de status (Atendente, Admin)
- Histórico de pedidos por cliente
- Cálculo automático de valores

### 5. Logs do Sistema (Administrador)
- Registro de todas as ações
- Rastreamento de alterações
- Auditoria completa

## 🧑‍💻 Áreas do Sistema

- **Administrador:**
  - Gerenciamento de usuários, produtos, pedidos e logs
- **Atendente:**
  - Painel de pedidos em tempo real, atualização de status
- **Cliente:**
  - Catálogo, realização de pedidos, acompanhamento, histórico, perfil


## 🔧 Configuração do Ambiente

### Pré-requisitos
- Java 17 ou superior
- Maven 3.8+
- PostgreSQL 12+
- Quarkus CLI (opcional, mas recomendado)
- Docker e Docker Compose (para containerização)
- Node.js 14+ (para desenvolvimento frontend)

### Configuração do Banco de Dados
1. Crie um banco de dados PostgreSQL:
```sql
CREATE DATABASE pizzeria;
```
2. Configure as credenciais no arquivo `application.properties` ou `application.yaml`.

### Executando o Projeto
1. Clone o repositório
2. Execute o backend:
```bash
./mvnw quarkus:dev
```
3. Acesse a aplicação em `http://localhost:8080`

### Frontend
- O frontend está disponível em `/src/main/resources/META-INF/resources/`.
- Acesse as páginas principais:
  - `/index.html` (página inicial)
  - `/login.html` (login)
  - `/cadastro.html` (cadastro de cliente)
  - Áreas específicas são exibidas conforme o papel do usuário após login.

## 📚 Documentação da API

A documentação completa da API está disponível em:
- Swagger UI: `http://localhost:8080/q/swagger-ui`
- OpenAPI: `http://localhost:8080/q/openapi`

## 🔐 Segurança

- Autenticação via JWT
- Senhas criptografadas (BCrypt)
- Controle de acesso baseado em perfis
- Validação de dados com Bean Validation
- Proteção contra CSRF
- Prevenção de SQL Injection
- Arquitetura em camadas com separação de responsabilidades

## 📝 Logs

O sistema mantém logs detalhados de todas as operações:
- Ação realizada
- Usuário responsável
- Data e hora
- Detalhes da operação
- Auditoria completa em banco de dados
- Logging estruturado com diferentes níveis

## 🏗️ Arquitetura

O sistema implementa uma arquitetura em camadas robusta e segura:

- **DTOs**: Objetos de transferência com validação Bean Validation
- **DAOs**: Camada de acesso a dados com EntityManager e queries parametrizadas
- **Services**: Lógica de negócio com controle transacional
- **Controllers**: Endpoints REST com tratamento de exceções
- **Entities**: Mapeamento JPA com relacionamentos

Para mais detalhes sobre a arquitetura, consulte o arquivo [ARQUITETURA_PERSISTENCIA.md](ARQUITETURA_PERSISTENCIA.md).

## 🤝 Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes. 
 