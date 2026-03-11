CREATE TABLE usuarios (
    id          BIGSERIAL PRIMARY KEY,
    nome        VARCHAR(100) NOT NULL,
    email       VARCHAR(150) NOT NULL UNIQUE,
    senha       VARCHAR(255) NOT NULL,
    ativo       BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO usuarios (nome, email, senha, ativo) VALUES
('Admin', 'admin@forumhub.com', '$2a$10$Y50UaMFOxteibQEYLrwuHeehHYfcoafCopUazP12.rqB41bsolF5.', TRUE);
