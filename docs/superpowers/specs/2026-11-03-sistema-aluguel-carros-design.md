# Sistema de Aluguel de Carros — Design

Data: 2026-11-03
Referência: LAB02 — DIAW, PUC Minas (João Paulo Carneiro Aramuni)

## Contexto

Sistema web de gestão de aluguel de automóveis. Clientes criam/consultam/modificam/cancelam
pedidos de aluguel; agentes (empresas e bancos) avaliam e modificam pedidos. Requer login
obrigatório. Stack definida pelo enunciado: Java 25 + Spring Boot 4.0.x (Maven) no back-end,
React + Vite no front-end, Supabase (PostgreSQL) como banco.

## Estrutura do repositório

```
Lab2/
  backend/   Spring Boot 4, Maven, Java 25, arquitetura MVC + REST
  frontend/  React + Vite
  docs/
    uml/                    diagramas PlantUML (casos de uso, classes, pacotes), user stories
    superpowers/specs/      este documento e specs futuras
  README.md                 template do professor
```

## Arquitetura

Monorepo. Backend expõe API REST (JSON). Frontend consome via axios/fetch. CORS liberado em
dev (`localhost:5173` → `localhost:8080`). Autenticação JWT stateless: login retorna token,
frontend envia `Authorization: Bearer <token>` em cada requisição. Spring Security filtra
endpoints por role (`CLIENTE` / `AGENTE`).

## Modelo de dados

- **Usuario**: id, login, senhaHash, role (`CLIENTE` | `AGENTE`)
- **Cliente** (1:1 Usuario): RG, CPF, nome, endereço, profissão
- **Empregador** (N:1 Cliente, máx. 3 por cliente, validado em serviço): nome, rendimento
- **Agente** (1:1 Usuario): tipo (`EMPRESA` | `BANCO`), nome/razão social
- **Automovel**: matrícula, ano, marca, modelo, placa; proprietário polimórfico
  (`tipoProprietario`: CLIENTE/EMPRESA/BANCO + `proprietarioId`)
- **Pedido**: cliente, automóvel, status (`INTRODUZIDO` → `EM_ANALISE` → `APROVADO`/`REJEITADO`
  → `EM_EXECUCAO` → `CONCLUIDO`/`CANCELADO`), dataPedido
- **Contrato** (1:1 Pedido): dataInicio, dataFim, valor
- **ContratoCredito** (1:1 Contrato, opcional): banco agente, valorFinanciado, parcelas

Contrato e ContratoCredito são modelados nos diagramas e no schema, criados automaticamente
quando um pedido é aprovado pelo agente. CRUD dedicado para eles fica fora do MVP (anotado
como extensão futura).

## Pacotes do backend (visão lógica)

- `controller` — endpoints REST
- `service` — regras de negócio
- `repository` — Spring Data JPA
- `model` — entidades JPA
- `dto` — objetos de entrada/saída da API
- `security` — filtro JWT, configuração Spring Security
- `config` — CORS, beans gerais

## Autenticação

- `POST /auth/registrar` — cria Usuario + Cliente ou Usuario + Agente (payload indica role)
- `POST /auth/login` — valida credenciais, retorna JWT
- Endpoints protegidos exigem token válido; role checada via `@PreAuthorize` ou filtro de rota

## Escopo funcional (protótipo)

- CRUD completo de Cliente (próprio perfil, dados de identificação/profissão/empregadores)
- Cliente: criar pedido, listar/consultar próprios pedidos, modificar (enquanto não avaliado),
  cancelar
- Agente: listar pedidos pendentes de avaliação, avaliar (aprovar/rejeitar), modificar
- Automóvel: CRUD simples, necessário para vincular a pedidos
- Ao aprovar um pedido, o serviço cria automaticamente o Contrato associado

## Frontend

Páginas: Login/Registro, Dashboard (lista pedidos conforme role do usuário logado), Formulário
de Pedido, Formulário de Cliente (CRUD de perfil), Lista de Automóveis. Rotas protegidas por
autenticação e por role. Token JWT guardado em memória/localStorage, enviado em todo request.

## Diagramas UML (docs/uml, formato PlantUML, versionado em texto)

- Diagrama de Casos de Uso (atores: Cliente, Agente)
- Histórias de Usuário (formato "como `<ator>` quero `<ação>` para `<objetivo>`")
- Diagrama de Classes (entidades listadas acima, com relações e multiplicidades)
- Diagrama de Pacotes (visão lógica do backend listada acima)

Diagramas serão revisados ao longo do desenvolvimento conforme feedback em aula, conforme
pedido pelo enunciado.

## Testes

Testes unitários JUnit nos services para os fluxos críticos: criar pedido, avaliar pedido
(aprovar/rejeitar), CRUD de cliente.

## Fora de escopo (futuro)

- CRUD dedicado de Contrato/ContratoCredito
- Notificações, upload de documentos
- Deploy em produção (Supabase hospedado será usado já em dev, via projeto criado com o
  usuário logando manualmente no Chrome controlado pelo agente)
