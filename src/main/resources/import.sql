-- Usuário administrador
INSERT INTO usuarios (nome_completo, email, senha, perfil, ativo)
VALUES ('Administrador', 'admin@pizzeria.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'ADMIN', true);

-- Usuário Manoel Augusto (ADMIN)
INSERT INTO usuarios (nome_completo, email, senha, perfil, ativo)
VALUES ('Manoel Augusto', 'manoelaugusto.ferreira@gmail.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'ADMIN', true);

-- Usuário Julio Cesar (ADMIN)
INSERT INTO usuarios (nome_completo, email, senha, perfil, ativo)
VALUES ('Julio Cesar', 'juliojcc.franca@gmail.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'ADMIN', true);

-- Usuário atendente
INSERT INTO usuarios (nome_completo, email, senha, perfil, ativo)
VALUES ('Atendente', 'atendente@pizzeria.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'ATENDENTE', true);

-- Usuário cliente
INSERT INTO usuarios (nome_completo, email, senha, perfil, ativo)
VALUES ('Cliente', 'cliente@pizzeria.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'CLIENTE', true);

-- Inserir alguns produtos
INSERT INTO produtos (nome, descricao, preco, tipo, disponivel, imagem_url)
VALUES 
('Margherita', 'Molho de tomate, mussarela, tomate e manjericão', 45.00, 'PIZZA', true, 'margherita.jpg'),
('Pepperoni', 'Molho de tomate, mussarela e pepperoni', 55.00, 'PIZZA', true, 'pepperoni.jpg'),
('Coca-Cola', 'Refrigerante Coca-Cola 350ml', 8.00, 'BEBIDA', true, 'coca-cola.jpg'),
('Guaraná', 'Refrigerante Guaraná 350ml', 8.00, 'BEBIDA', true, 'guarana.jpg'),
('Borda Recheada', 'Borda recheada com catupiry', 10.00, 'EXTRAS', true, 'borda.jpg'); 