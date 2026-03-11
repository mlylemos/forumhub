# 🗨️ FórumHub API

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java" />
  <img src="https://img.shields.io/badge/Spring%20Boot-3.3.0-brightgreen?style=for-the-badge&logo=spring" />
  <img src="https://img.shields.io/badge/MySQL-8.0-blue?style=for-the-badge&logo=mysql" />
  <img src="https://img.shields.io/badge/JWT-Auth0-purple?style=for-the-badge" />
  <img src="https://img.shields.io/badge/Swagger-OpenAPI%203-85EA2D?style=for-the-badge&logo=swagger" />
  <img src="https://img.shields.io/badge/Status-Concluído-success?style=for-the-badge" />
</p>

> **Alura Challenge Back-End** — Implementação de uma API REST inspirada no fórum da Alura utilizando Spring Boot 3, Spring Security, JWT, JPA, Flyway e MySQL.

---

# 📋 Índice

1. Sobre o Projeto  
2. Funcionalidades  
3. Tecnologias  
4. Arquitetura  
5. Diagrama do Banco de Dados  
6. Regras de Negócio  
7. Pré-requisitos  
8. Instalação e Configuração  
9. Variáveis de Ambiente  
10. Endpoints da API  
11. Autenticação JWT  
12. Testes  
13. Documentação Swagger  

---

# 📖 Sobre o Projeto

O **FórumHub** é uma API REST que replica o funcionamento do fórum da Alura, permitindo que usuários autenticados criem tópicos de discussão relacionados a cursos, respondam perguntas e gerenciem o ciclo de vida das conversas.

O projeto foi desenvolvido como parte do **Challenge Back-End da Alura + Oracle Next Education (ONE)**.

A aplicação implementa as operações de **CRUD (Create, Read, Update, Delete)** para tópicos, usuários e respostas, utilizando boas práticas de desenvolvimento com **Spring Boot**, **arquitetura em camadas**, **validação de dados**, **controle de acesso** e **autenticação via JWT**.

---

# ✅ Funcionalidades

### 🔐 Autenticação

- Login com **email e senha**
- Retorno de **token JWT**
- Autenticação **stateless**

---

### 👤 Usuários

- Cadastro de usuário
- Visualização de perfil
- Atualização do próprio perfil
- Exclusão lógica (**soft delete**)

---

### 💬 Tópicos

- Criar tópico associado ao usuário autenticado
- Listar tópicos com **paginação**
- Ordenação por **data de criação**
- Filtrar por **curso** e **ano**
- Visualizar detalhes de um tópico
- Atualizar título, mensagem, curso ou status
- Exclusão lógica (**soft delete**)
- Validação para evitar **tópicos duplicados**

---

### 💡 Respostas

- Criar resposta em um tópico
- Listar respostas de um tópico
- Atualizar resposta
- Excluir resposta
- Marcar resposta como **solução**

---

### 🔒 Segurança

- Apenas usuários **autenticados** podem acessar rotas protegidas

Rotas públicas:

POST /login  
POST /usuarios  

---

### 📚 Documentação

Documentação automática com **Swagger UI** disponível em:

/swagger-ui.html

---

# 🛠️ Tecnologias

| Camada | Tecnologia |
|------|------|
| Linguagem | Java 17 |
| Framework | Spring Boot 3 |
| Segurança | Spring Security |
| Autenticação | JWT (Auth0) |
| Persistência | Spring Data JPA + Hibernate |
| Banco de dados | MySQL 8 |
| Migrations | Flyway |
| Validação | Jakarta Bean Validation |
| Build | Maven |
| Documentação | SpringDoc OpenAPI |
| Testes | JUnit 5 + Mockito |
| Utilitários | Lombok |

---

# 🏗️ Arquitetura

O projeto segue uma **arquitetura em camadas**, separando responsabilidades:

Controller  
↓  
Service  
↓  
Repository  
↓  
Banco de Dados  

Estrutura do projeto:

```
forumhub/
├── config/
├── controller/
├── dto/
├── entity/
├── exception/
├── filter/
├── repository/
└── service/
```

Essa organização melhora a **manutenção**, **testabilidade** e **separação de responsabilidades**.

---

# 🗄️ Diagrama do Banco de Dados

usuarios  
│  
└───< topicos  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;│  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└───< respostas  

Relações principais:

- Um **usuário** pode criar vários **tópicos**
- Um **tópico** pode ter várias **respostas**
- Cada **resposta** pertence a um **tópico** e a um **usuário**

---

# 📏 Regras de Negócio

- Todos os campos obrigatórios devem ser informados
- Não é permitido criar **tópicos duplicados** (mesmo título e mensagem)
- Apenas **usuários autenticados** podem criar, editar ou excluir tópicos
- O sistema valida a existência do recurso antes de atualizar ou excluir
- Exclusões utilizam **soft delete**

---

# 📦 Pré-requisitos

- Java **17+**
- Maven
- MySQL **8+**
- IntelliJ IDEA (recomendado)
- Postman ou Insomnia para testes

---

# 🚀 Instalação e Configuração

### 1️⃣ Clonar o repositório

```
git clone https://github.com/seu-usuario/forumhub.git
cd forumhub
```

---

### 2️⃣ Criar o banco de dados

```
CREATE DATABASE forumhub;
```

O **Flyway** criará as tabelas automaticamente ao iniciar a aplicação.

---

### 3️⃣ Executar o projeto

```
./mvnw spring-boot:run
```

A aplicação estará disponível em:

```
http://localhost:8080
```

---

# 🔑 Variáveis de Ambiente

| Variável | Descrição |
|------|------|
DB_USERNAME | Usuário do banco |
DB_PASSWORD | Senha do banco |
JWT_SECRET | Chave secreta para gerar tokens |
JWT_EXPIRATION | Tempo de expiração do token |

Exemplo:

```
JWT_SECRET=minha-chave-super-secreta
JWT_EXPIRATION=86400000
```

---

# 📡 Endpoints da API

## 🔓 Públicos

POST /login  
POST /usuarios  

---

## 🔒 Autenticados

### Tópicos

POST /topicos  
GET /topicos  
GET /topicos/{id}  
PUT /topicos/{id}  
DELETE /topicos/{id}  

---

### Respostas

POST /topicos/{id}/respostas  
GET /topicos/{id}/respostas  
PUT /topicos/{id}/respostas/{respostaId}  
DELETE /topicos/{id}/respostas/{respostaId}  

---

### Usuários

GET /usuarios/{id}  
PUT /usuarios/{id}  
DELETE /usuarios/{id}  

---

# 🔐 Autenticação JWT

### Login

```
POST /login
```

Body:

```json
{
  "email": "admin@forumhub.com",
  "senha": "123456"
}
```

Resposta:

```json
{
  "token": "JWT_TOKEN",
  "tipo": "Bearer"
}
```

---

### Usar token nas requisições

Header:

```
Authorization: Bearer SEU_TOKEN
```

---

# 🧪 Testes

Executar testes:

```
./mvnw test
```

Os testes cobrem:

- Cadastro de tópico
- Validação de duplicidade
- Busca por ID
- Atualização
- Exclusão lógica

---

# 📚 Documentação Swagger

Com a aplicação rodando:

```
http://localhost:8080/swagger-ui.html
```

A interface permite **testar os endpoints diretamente pelo navegador**.

---

# 👤 Usuário padrão

Criado automaticamente pela migration:

| Email | Senha |
|------|------|
admin@forumhub.com | 123456 |

---

# 👩‍💻 Autora

Projeto desenvolvido por **Emily Lemos**.

---

# 📝 Licença

Este projeto está sob a licença **MIT**.
