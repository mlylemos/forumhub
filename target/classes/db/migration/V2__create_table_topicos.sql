CREATE TABLE topicos (
    id          BIGSERIAL PRIMARY KEY,
    titulo      VARCHAR(200) NOT NULL,
    mensagem    TEXT NOT NULL,
    criado_em   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status      VARCHAR(20) NOT NULL DEFAULT 'ABERTO',
    autor_id    BIGINT NOT NULL,
    curso       VARCHAR(150) NOT NULL,
    ativo       BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_topicos_usuarios FOREIGN KEY (autor_id) REFERENCES usuarios (id)
);

CREATE INDEX idx_topicos_autor ON topicos (autor_id);
CREATE INDEX idx_topicos_curso ON topicos (curso);
CREATE INDEX idx_topicos_status ON topicos (status);
