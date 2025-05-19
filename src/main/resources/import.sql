-- Inserir usuário administrador
INSERT INTO usuarios (nome_completo, email, senha, perfil, ativo)
VALUES ('Administrador', 'admin@pizzeria.com', 'admin123', 'ADMIN', true);

-- Inserir alguns produtos
INSERT INTO produtos (nome, descricao, preco, tipo, disponivel, imagem_url)
VALUES 
('Margherita', 'Molho de tomate, mussarela, tomate e manjericão', 45.00, 'PIZZA', true, 'margherita.jpg'),
('Pepperoni', 'Molho de tomate, mussarela e pepperoni', 55.00, 'PIZZA', true, 'pepperoni.jpg'),
('Coca-Cola', 'Refrigerante Coca-Cola 350ml', 8.00, 'BEBIDA', true, 'coca-cola.jpg'),
('Guaraná', 'Refrigerante Guaraná 350ml', 8.00, 'BEBIDA', true, 'guarana.jpg'),
('Borda Recheada', 'Borda recheada com catupiry', 10.00, 'EXTRAS', true, 'borda.jpg'); 