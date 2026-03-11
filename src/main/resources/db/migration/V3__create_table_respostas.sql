CREATE TABLE respostas (
    id          BIGSERIAL PRIMARY KEY,
    mensagem    TEXT NOT NULL,
    topico_id   BIGINT NOT NULL,
    criado_em   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    autor_id    BIGINT NOT NULL,
    solucao     BOOLEAN NOT NULL DEFAULT FALSE,
    ativo       BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_respostas_topicos  FOREIGN KEY (topico_id) REFERENCES topicos (id),
    CONSTRAINT fk_respostas_usuarios FOREIGN KEY (autor_id)  REFERENCES usuarios (id)
);
