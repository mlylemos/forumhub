# 🗨️ FórumHub API

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java" />
  <img src="https://img.shields.io/badge/Spring%20Boot-3.3.0-brightgreen?style=for-the-badge&logo=spring" />
  <img src="https://img.shields.io/badge/MySQL-8.0-blue?style=for-the-badge&logo=mysql" />
  <img src="https://img.shields.io/badge/JWT-Auth0-purple?style=for-the-badge" />
  <img src="https://img.shields.io/badge/Swagger-OpenAPI%203-85EA2D?style=for-the-badge&logo=swagger" />
  <img src="https://img.shields.io/badge/Status-Concluído-success?style=for-the-badge" />
</p>

> **Alura Challenge Back End** — Réplica do back-end do fórum da Alura usando Spring Boot 3, JPA, Flyway, Spring Security e JWT.

---

## 📋 Índice

1. [Sobre o Projeto](#-sobre-o-projeto)
2. [Funcionalidades](#-funcionalidades)
3. [Tecnologias](#-tecnologias)
4. [Arquitetura](#-arquitetura)
5. [Diagrama do Banco de Dados](#-diagrama-do-banco-de-dados)
6. [Pré-requisitos](#-pré-requisitos)
7. [Instalação e Configuração](#-instalação-e-configuração)
8. [Variáveis de Ambiente](#-variáveis-de-ambiente)
9. [Endpoints da API](#-endpoints-da-api)
10. [Autenticação JWT](#-autenticação-jwt)
11. [Testes](#-testes)
12. [Documentação Swagger](#-documentação-swagger)

---

## 📖 Sobre o Projeto

O **FórumHub** é uma API REST que replica o comportamento do fórum da Alura, permitindo que alunos criem tópicos de dúvidas associados a cursos, respondam uns aos outros e gerenciem o ciclo de vida das discussões.

O projeto implementa as quatro operações fundamentais de CRUD com regras de negócio, autenticação stateless via JWT e documentação interativa via Swagger UI.

---

## ✅ Funcionalidades

- **Autenticação**
  - Login com e-mail e senha → retorno de token JWT Bearer
- **Usuários**
  - Cadastro, visualização, atualização (self) e exclusão (soft-delete)
- **Tópicos** *(CRUD completo)*
  - Criar tópico associado ao usuário autenticado
  - Listar todos (paginado, ordenado por data ASC)
  - Filtrar por nome do curso e/ou ano de criação
  - Detalhar um tópico pelo ID
  - Atualizar título, mensagem, curso e status
  - Excluir (soft-delete)
  - Validação de duplicidade (mesmo título + mensagem)
- **Respostas**
  - Criar resposta em um tópico
  - Listar respostas de um tópico
  - Atualizar e excluir resposta (apenas o autor)
  - Marcar resposta como solução
- **Segurança**
  - Apenas usuários autenticados acessam as rotas protegidas
  - Rotas públicas: `POST /login` e `POST /usuarios`
- **Documentação**
  - Swagger UI disponível em `/swagger-ui.html`

---

## 🛠️ Tecnologias

| Camada            | Tecnologia                           |
|-------------------|--------------------------------------|
| Linguagem         | Java 17                              |
| Framework         | Spring Boot 3.3                      |
| Segurança         | Spring Security + JWT (Auth0)        |
| Persistência      | Spring Data JPA + Hibernate          |
| Banco de Dados    | MySQL 8                              |
| Migrations        | Flyway                               |
| Validação         | Jakarta Bean Validation              |
| Build             | Maven 4                              |
| Documentação      | SpringDoc OpenAPI (Swagger UI)       |
| Testes            | JUnit 5 + Mockito                    |
| Utilitários       | Lombok                               |

---

## 🏗️ Arquitetura

```
forumhub/
├── config/          # SecurityConfigurations, OpenApiConfig
├── controller/      # AuthController, TopicoController, UsuarioController, RespostaController
├── dto/             # Records (CadastrarTopico, DadosAutenticacao, DadosTokenJWT, ...)
├── entity/          # Usuario, Topico, Resposta, StatusTopico
├── exception/       # Exceções customizadas + GlobalExceptionHandler
├── filter/          # SecurityFilter (validação JWT por requisição)
├── repository/      # Interfaces JpaRepository com queries customizadas
└── service/         # TopicoService, UsuarioService, RespostaService, TokenService
```

O padrão adotado é **Controller → Service → Repository**, mantendo responsabilidades bem separadas e facilitando testes unitários por camada.

---

## 🗄️ Diagrama do Banco de Dados

```
┌────────────────────────┐        ┌──────────────────────────────┐
│       usuarios         │        │           topicos             │
├────────────────────────┤        ├──────────────────────────────┤
│ id          BIGINT  PK │◄───┐   │ id          BIGINT  PK       │
│ nome        VARCHAR    │    │   │ titulo      VARCHAR(200)      │
│ email       VARCHAR UQ │    │   │ mensagem    TEXT              │
│ senha       VARCHAR    │    └───┤ autor_id    BIGINT  FK        │
│ ativo       BOOLEAN    │        │ curso       VARCHAR(150)      │
│ criado_em   DATETIME   │        │ status      ENUM              │
└────────────────────────┘        │ criado_em   DATETIME          │
                                  │ ativo       BOOLEAN           │
                                  └──────────────────────────────┘
                                            │ 1
                                            │
                                            ▼ N
                                  ┌──────────────────────────────┐
                                  │          respostas           │
                                  ├──────────────────────────────┤
                                  │ id          BIGINT  PK       │
                                  │ mensagem    TEXT              │
                                  │ topico_id   BIGINT  FK       │
                                  │ autor_id    BIGINT  FK       │
                                  │ solucao     BOOLEAN          │
                                  │ criado_em   DATETIME         │
                                  │ ativo       BOOLEAN          │
                                  └──────────────────────────────┘
```

---

## 📦 Pré-requisitos

- [Java JDK 17+](https://www.oracle.com/java/technologies/downloads/)
- [Maven 4+](https://maven.apache.org/download.cgi)
- [MySQL 8+](https://dev.mysql.com/downloads/installer/)
- [IntelliJ IDEA](https://www.jetbrains.com/idea/download/) *(recomendado)*
- [Insomnia](https://insomnia.rest/) ou [Postman](https://www.postman.com/) para testes

---

## 🚀 Instalação e Configuração

### 1. Clone o repositório

```bash
git clone https://github.com/seu-usuario/forumhub.git
cd forumhub
```

### 2. Crie o banco de dados no MySQL

```sql
CREATE DATABASE forumhub CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

> O Flyway irá criar as tabelas automaticamente ao iniciar a aplicação.

### 3. Configure as variáveis de ambiente

Crie um arquivo `.env` ou exporte as variáveis no terminal (veja a seção abaixo).

### 4. Execute a aplicação

```bash
./mvnw spring-boot:run
```

A API estará disponível em `http://localhost:8080`.

---

## 🔑 Variáveis de Ambiente

| Variável         | Descrição                          | Padrão (dev)                          |
|------------------|------------------------------------|---------------------------------------|
| `DB_USERNAME`    | Usuário do MySQL                   | `root`                                |
| `DB_PASSWORD`    | Senha do MySQL                     | `root`                                |
| `JWT_SECRET`     | Segredo para assinar os tokens JWT | `minha-chave-secreta-forumhub-2024`   |
| `JWT_EXPIRATION` | Validade do token em ms            | `86400000` (24 h)                     |

> ⚠️ **Em produção**, sempre substitua o `JWT_SECRET` por uma string longa e aleatória.

---

## 📡 Endpoints da API

### 🔓 Públicos

| Método | URI        | Descrição                       |
|--------|------------|---------------------------------|
| POST   | `/login`   | Autenticar e obter token JWT    |
| POST   | `/usuarios`| Cadastrar novo usuário          |

### 🔒 Autenticados (requer `Authorization: Bearer <token>`)

#### Tópicos

| Método | URI              | Descrição                                        |
|--------|------------------|--------------------------------------------------|
| POST   | `/topicos`       | Criar novo tópico                                |
| GET    | `/topicos`       | Listar tópicos (paginado; query: `curso`, `ano`) |
| GET    | `/topicos/{id}`  | Detalhar tópico                                  |
| PUT    | `/topicos/{id}`  | Atualizar tópico                                 |
| DELETE | `/topicos/{id}`  | Excluir tópico (soft-delete)                     |

#### Respostas

| Método | URI                                   | Descrição                    |
|--------|---------------------------------------|------------------------------|
| POST   | `/topicos/{topicoId}/respostas`       | Responder tópico             |
| GET    | `/topicos/{topicoId}/respostas`       | Listar respostas do tópico   |
| PUT    | `/topicos/{topicoId}/respostas/{id}`  | Atualizar resposta           |
| DELETE | `/topicos/{topicoId}/respostas/{id}`  | Excluir resposta             |

#### Usuários

| Método | URI             | Descrição                    |
|--------|-----------------|------------------------------|
| GET    | `/usuarios/{id}`| Detalhar usuário             |
| PUT    | `/usuarios/{id}`| Atualizar próprio perfil     |
| DELETE | `/usuarios/{id}`| Excluir própria conta        |

---

## 🔐 Autenticação JWT

### 1. Fazer login

```http
POST /login
Content-Type: application/json

{
  "email": "admin@forumhub.com",
  "senha": "123456"
}
```

**Resposta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tipo": "Bearer",
  "expiracaoEm": 86400000
}
```

### 2. Usar o token nas requisições

```http
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### 3. Exemplo — criar tópico

```http
POST /topicos
Authorization: Bearer <token>
Content-Type: application/json

{
  "titulo": "Dúvida sobre Spring Security",
  "mensagem": "Como configurar o filtro JWT corretamente no Spring Boot 3?",
  "curso": "Spring Boot 3"
}
```

---

## 🧪 Testes

```bash
./mvnw test
```

Os testes unitários cobrem o `TopicoService` com cenários de:

- Cadastro bem-sucedido
- Rejeição de tópico duplicado (`DuplicateTopicException`)
- Busca por ID inexistente (`ResourceNotFoundException`)
- Soft-delete
- Atualização parcial de campos

---

## 📚 Documentação Swagger

Com a aplicação em execução, acesse:

```
http://localhost:8080/swagger-ui.html
```

A interface permite visualizar e testar todos os endpoints diretamente no navegador. Para rotas protegidas, clique em **Authorize** e informe o token JWT no formato `Bearer <token>`.

---

## 👤 Usuário padrão (seed)

A migration `V1` insere um usuário administrador para testes imediatos:

| Campo | Valor                  |
|-------|------------------------|
| Email | `admin@forumhub.com`   |
| Senha | `123456`               |

---

## 📝 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.
