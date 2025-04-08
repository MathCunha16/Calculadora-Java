CREATE TABLE historico_operacoes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    operacao TEXT NOT NULL,          
    resultado VARCHAR(255),         
    data_hora DATETIME NOT NULL, 
    status_operacao VARCHAR(20) NOT NULL    
);