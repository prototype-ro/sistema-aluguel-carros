# 🏷️ Sistema de Aluguel de Carros 👨‍💻

> [!NOTE]
> Sistema web de gestão de aluguel de automóveis: clientes criam, consultam, modificam e cancelam pedidos de aluguel; agentes (empresas e bancos) avaliam e aprovam esses pedidos.

Projeto desenvolvido para o **Laboratório 02** da disciplina **DIAW** (Desenvolvimento e Integração de Aplicações Web), 2º período de Engenharia de Software, PUC Minas — Prof. Dr. João Paulo Carneiro Aramuni.

---

## 📚 Índice
- [Sobre o Projeto](#-sobre-o-projeto)
- [Funcionalidades Principais](#-funcionalidades-principais)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Arquitetura](#-arquitetura)
- [Instalação e Execução](#-instalação-e-execução)
  - [Pré-requisitos](#pré-requisitos)
  - [Variáveis de Ambiente](#-variáveis-de-ambiente)
  - [Instalação de Dependências](#-instalação-de-dependências)
  - [Como Executar a Aplicação](#-como-executar-a-aplicação)
- [Estrutura de Pastas](#-estrutura-de-pastas)
- [Modelagem (UML)](#-modelagem-uml)
- [Testes](#-testes)
- [Documentações utilizadas](#-documentações-utilizadas)
- [Autores](#-autores)
- [Licença](#-licença)

---

## 📝 Sobre o Projeto

O sistema apoia a gestão de aluguéis de automóveis, permitindo efetuar, cancelar e modificar
pedidos através da Internet. O enunciado completo da disciplina descreve dois tipos de atores:

- **Clientes** (usuários individuais): introduzem, modificam, consultam e cancelam pedidos de
  aluguel.
- **Agentes** (empresas e bancos): avaliam (aprovam/rejeitam) e modificam pedidos.

Após a introdução de um pedido, ele é analisado do ponto de vista financeiro pelo agente; em
caso de parecer positivo, é colocado à sua consideração para execução do contrato — o que o
sistema modela criando automaticamente um **Contrato** associado ao pedido aprovado.

---

## ✨ Funcionalidades Principais

- 🔐 **Autenticação JWT:** login obrigatório para clientes e agentes, acesso restrito a
  usuários cadastrados.
- 👤 **CRUD de Cliente:** cadastro e edição de dados de identificação (RG, CPF, nome,
  endereço), profissão e até 3 empregadores com seus rendimentos.
- 🚗 **CRUD de Automóvel:** cadastro de automóveis (matrícula, ano, marca, modelo, placa) por
  agentes, vinculados a um proprietário (cliente, empresa ou banco).
- 📋 **Ciclo de vida do Pedido:** criação, consulta, modificação e cancelamento pelo cliente;
  listagem de pendentes e avaliação (aprovar/rejeitar) pelo agente.
- 📄 **Contrato automático:** ao aprovar um pedido, o sistema cria automaticamente o contrato
  associado.

---

## 🛠 Tecnologias Utilizadas

### 💻 Front-end

* **Framework/Biblioteca:** React 19
* **Linguagem:** JavaScript (JSX)
* **Build Tool:** Vite
* **Roteamento:** React Router DOM
* **Cliente HTTP:** Axios

### 🖥️ Back-end

* **Linguagem/Runtime:** Java 25 (JDK)
* **Framework:** Spring Boot 4.0.x (Web, Data JPA, Security, Validation)
* **Banco de Dados:** PostgreSQL via Supabase
* **Migrações:** Flyway
* **ORM:** Hibernate / JPA
* **Autenticação:** JWT (io.jsonwebtoken) + Spring Security
* **Build:** Apache Maven

### 🧪 Testes

* **Back-end:** JUnit 5 + Mockito (testes unitários de serviço)

---

## 🏗 Arquitetura

Monorepo com `backend/` (API REST em arquitetura MVC) e `frontend/` (SPA). O front-end consome
a API via Axios, enviando o token JWT obtido no login em todas as requisições autenticadas.
CORS liberado entre `localhost:5173` e `localhost:8080` em desenvolvimento.

**Pacotes do back-end (visão lógica):**

- `controller` — endpoints REST
- `service` — regras de negócio
- `repository` — Spring Data JPA
- `model` — entidades JPA
- `dto` — objetos de entrada/saída da API
- `security` — filtro JWT, `UserDetailsService`
- `config` — Spring Security, CORS

**Modelo de dados principal:** Usuario (login + role), Cliente, Empregador, Agente, Automovel,
Pedido, Contrato e ContratoCredito — ver diagrama de classes em [`docs/uml/diagrama-classes.puml`](docs/uml/diagrama-classes.puml).

---

## 🔧 Instalação e Execução

### Pré-requisitos

* **Java JDK 25**
* **Node.js** (LTS) e **npm**
* Conta **Supabase** (PostgreSQL gerenciado) — ou um Postgres local equivalente

### 🔑 Variáveis de Ambiente

#### Back-end (`backend/.env`, carregado no shell antes de rodar)

| Variável | Descrição | Exemplo |
| :--- | :--- | :--- |
| `SUPABASE_DB_URL` | URL JDBC de conexão com o Postgres (Supabase). | `jdbc:postgresql://aws-0-us-east-2.pooler.supabase.com:5432/postgres` |
| `SUPABASE_DB_USER` | Usuário do banco. | `postgres.<project-ref>` |
| `SUPABASE_DB_PASSWORD` | Senha do banco. | `(gerada no Supabase)` |
| `JWT_SECRET` | Chave secreta para assinatura dos tokens JWT. | `(string longa e aleatória)` |

Veja `backend/.env.example` para o template.

#### Front-end (`frontend/.env`)

| Variável | Descrição | Exemplo |
| :--- | :--- | :--- |
| `VITE_API_URL` | URL base da API do back-end. | `http://localhost:8080` |

Veja `frontend/.env.example` para o template.

### 📦 Instalação de Dependências

```bash
# Front-end
cd frontend
npm install
cd ..

# Back-end (Maven Wrapper resolve as dependências no build)
cd backend
./mvnw -q compile
cd ..
```

### ⚡ Como Executar a Aplicação

Em dois terminais separados, com as variáveis de ambiente do back-end exportadas:

**Terminal 1 — Back-end:**

```bash
cd backend
set -a && source .env && set +a   # carrega as variáveis do .env
./mvnw spring-boot:run
```
🚀 API disponível em **http://localhost:8080**.

**Terminal 2 — Front-end:**

```bash
cd frontend
npm run dev
```
🎨 Aplicação disponível em **http://localhost:5173**.

---

## 📂 Estrutura de Pastas

```
.
├── backend/                     # Spring Boot (MVC + REST)
│   ├── .env.example
│   ├── src/main/java/br/puc/aluguel/
│   │   ├── controller/           # Endpoints REST
│   │   ├── service/              # Regras de negócio
│   │   ├── repository/           # Spring Data JPA
│   │   ├── model/                # Entidades JPA
│   │   ├── dto/                  # Objetos de entrada/saída
│   │   ├── security/             # JWT, UserDetailsService
│   │   └── config/                # Security, CORS
│   ├── src/main/resources/
│   │   ├── application.yml
│   │   └── db/migration/         # Migrações Flyway
│   └── src/test/java/            # Testes unitários (JUnit + Mockito)
│
├── frontend/                    # React + Vite
│   ├── .env.example
│   └── src/
│       ├── api/                  # Cliente Axios
│       ├── context/               # AuthContext (JWT, role)
│       ├── pages/                 # Login, Registro, Dashboard, Perfil, Pedido, Automóveis
│       └── routes/                # ProtectedRoute
│
├── docs/
│   └── uml/                      # Diagramas PlantUML + user stories
│
└── README.md
```

---

## 📐 Modelagem (UML)

Diagramas em formato PlantUML, versionados como texto em [`docs/uml/`](docs/uml/):

- [Diagrama de Casos de Uso](docs/uml/casos-de-uso.puml) — atores Cliente e Agente
- [Histórias de Usuário](docs/uml/user-stories.md)
- [Diagrama de Classes](docs/uml/diagrama-classes.puml)
- [Diagrama de Pacotes (Visão Lógica)](docs/uml/diagrama-pacotes.puml)

---

## 🧪 Testes

Testes unitários do back-end (JUnit 5 + Mockito), cobrindo os fluxos críticos: autenticação,
CRUD de cliente e ciclo de vida do pedido (criar, avaliar, cancelar).

```bash
cd backend
./mvnw test
```

---

## 🔗 Documentações utilizadas

* 📖 [Documentação Oficial do **React**](https://react.dev/reference/react)
* 📖 [Guia de Configuração do **Vite**](https://vitejs.dev/config/)
* 📖 [Documentação Oficial do **Spring Boot**](https://docs.spring.io/spring-boot/docs/current/reference/html/)
* 📖 [Documentação do **Spring Security**](https://docs.spring.io/spring-security/reference/)
* 📖 [Documentação do **Supabase**](https://supabase.com/docs)
* 📖 [Flyway Documentation](https://documentation.red-gate.com/fd)

---

## 👥 Autores

| 👤 Nome | :octocat: GitHub |
|---------|-----------------|
| Rodrigo | https://github.com/prototype-ro |

---

## 📄 Licença

Projeto acadêmico desenvolvido para fins educacionais — PUC Minas, disciplina DIAW.
