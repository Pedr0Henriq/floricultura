CREATE TABLE Vendedor(
    id_vendedor int NOT NULL AUTO_INCREMENT,
    nome varchar(30) NOT NULL,
    usuario varchar(15) UNIQUE NOT NULL,
    cpf varchar(11) UNIQUE NOT NULL,
    senha varchar(15) NOT NULL,

    PRIMARY KEY (id_vendedor)
);
CREATE TABLE Cliente(
    id_cliente int NOT NULL AUTO_INCREMENT,
    senha varchar(15) NOT NULL,
    nome varchar(30) NOT NULL,
    cpf varchar(11) UNIQUE NOT NULL,
    rua text,
    numero smallint,
    email text,
    
    PRIMARY KEY (id_cliente)
);

CREATE TABLE Compra(
    id_compra int NOT NULL AUTO_INCREMENT,
    forma_pagamento text NOT NULL,
    data date NOT NULL,
    valor int NOT NULL CHECK (valor >= 0),
    id_vendedor int,
    id_carrinho int,
    id_cliente int,

    PRIMARY KEY  (id_compra),
    FOREIGN KEY (id_vendedor) REFERENCES Vendedor(id_vendedor) ON UPDATE CASCADE,
    FOREIGN KEY (id_carrinho) REFERENCES Compra(id_compra) ON UPDATE CASCADE,
    FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente) ON UPDATE CASCADE
);

CREATE TABLE produto(
    id_produto int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nome varchar(30) NOT NULL,
    quantidade_estoque int NOT NULL CHECK (quantidade_estoque >= 0),
    preco double NOT NULL CHECK (preco >= 0.0)
);

CREATE TABLE carrinho(
    id_carrinho int NOT NULL AUTO_INCREMENT,
    id_cliente int,
    id_compra int,

    PRIMARY KEY (id_carrinho),
    FOREIGN KEY (id_compra) REFERENCES compra(id_compra) ON UPDATE CASCADE
);

CREATE TABLE carrinho_produto(
    id_carrinho int,
    id_produto int,
    quantidade int CHECK (quantidade > 0),
    PRIMARY KEY (id_carrinho, id_produto),
    FOREIGN KEY (id_carrinho) REFERENCES carrinho(id_carrinho) ON UPDATE CASCADE,
    FOREIGN KEY (id_produto) REFERENCES produto(id_produto) ON UPDATE CASCADE
);