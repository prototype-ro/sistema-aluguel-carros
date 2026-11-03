CREATE TABLE usuario (
    id BIGSERIAL PRIMARY KEY,
    login VARCHAR(100) NOT NULL UNIQUE,
    senha_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL
);

CREATE TABLE cliente (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL UNIQUE REFERENCES usuario(id),
    rg VARCHAR(20) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    nome VARCHAR(150) NOT NULL,
    endereco VARCHAR(255) NOT NULL,
    profissao VARCHAR(100)
);

CREATE TABLE empregador (
    id BIGSERIAL PRIMARY KEY,
    cliente_id BIGINT NOT NULL REFERENCES cliente(id),
    nome VARCHAR(150) NOT NULL,
    rendimento NUMERIC(12,2) NOT NULL
);

CREATE TABLE agente (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL UNIQUE REFERENCES usuario(id),
    tipo VARCHAR(20) NOT NULL,
    nome VARCHAR(150) NOT NULL
);

CREATE TABLE automovel (
    id BIGSERIAL PRIMARY KEY,
    matricula VARCHAR(30) NOT NULL UNIQUE,
    ano INT NOT NULL,
    marca VARCHAR(80) NOT NULL,
    modelo VARCHAR(80) NOT NULL,
    placa VARCHAR(10) NOT NULL UNIQUE,
    tipo_proprietario VARCHAR(20) NOT NULL,
    proprietario_id BIGINT NOT NULL
);

CREATE TABLE pedido (
    id BIGSERIAL PRIMARY KEY,
    cliente_id BIGINT NOT NULL REFERENCES cliente(id),
    automovel_id BIGINT NOT NULL REFERENCES automovel(id),
    data_pedido DATE NOT NULL,
    status VARCHAR(20) NOT NULL
);

CREATE TABLE contrato (
    id BIGSERIAL PRIMARY KEY,
    pedido_id BIGINT NOT NULL UNIQUE REFERENCES pedido(id),
    data_inicio DATE NOT NULL,
    data_fim DATE NOT NULL,
    valor NUMERIC(12,2) NOT NULL
);

CREATE TABLE contrato_credito (
    id BIGSERIAL PRIMARY KEY,
    contrato_id BIGINT NOT NULL UNIQUE REFERENCES contrato(id),
    agente_id BIGINT NOT NULL REFERENCES agente(id),
    valor_financiado NUMERIC(12,2) NOT NULL,
    parcelas INT NOT NULL
);
