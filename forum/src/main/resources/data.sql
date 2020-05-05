INSERT INTO USUARIO(nome, email, senha) VALUES('Aluno', 'aluno@email.com', '$2a$10$Z023MkDCM3SW8Ny8.I8D8eWNmV1LFzsSu3wuY5YlQ7Gcctfv1cJD6');

INSERT INTO CATEGORIA(descricao) values('WEB');
INSERT INTO CATEGORIA(descricao) values('Programação');
INSERT INTO CATEGORIA(descricao) values('Front-end');

INSERT INTO CURSO(nome, categoria_id) VALUES('Spring Boot', 2);
INSERT INTO CURSO(nome, categoria_id) VALUES('HTML 5', 3);

INSERT INTO TOPICO(titulo, mensagem, data_criacao, status, autor_id, curso_id) VALUES('Dúvida', 'Erro ao criar projeto', '2019-05-05 18:00:00', 'NAO_RESPONDIDO', 1, 1);
INSERT INTO TOPICO(titulo, mensagem, data_criacao, status, autor_id, curso_id) VALUES('Dúvida 2', 'Projeto não compila', '2019-05-05 19:00:00', 'NAO_RESPONDIDO', 1, 1);
INSERT INTO TOPICO(titulo, mensagem, data_criacao, status, autor_id, curso_id) VALUES('Dúvida 3', 'Tag HTML', '2019-05-05 20:00:00', 'NAO_RESPONDIDO', 1, 2);