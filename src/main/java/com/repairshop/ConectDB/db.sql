CREATE DATABASE IF NOT EXISTS repair_system;
USE repair_system;

DROP TABLE IF EXISTS Repair;
DROP TABLE IF EXISTS Machine;
DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS Role;
DROP TABLE IF EXISTS RepairType;
DROP TABLE IF EXISTS MachineModel;
DROP TABLE IF EXISTS Client;

CREATE TABLE Client (
    id INT AUTO_INCREMENT PRIMARY KEY,
    companyName VARCHAR(100) NOT NULL UNIQUE 
);

CREATE TABLE MachineModel (
    id INT AUTO_INCREMENT PRIMARY KEY,
    brand VARCHAR(100) NOT NULL,       
    yearOfRelease YEAR,               
    countryOfManufacture VARCHAR(100)    
);

CREATE TABLE RepairType (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE, 
    cost DECIMAL(10, 2) NOT NULL,     
    durationDays INT NOT NULL         
);

CREATE TABLE Role (
    id INT AUTO_INCREMENT PRIMARY KEY,
    roleName VARCHAR(100) NOT NULL UNIQUE   
);


CREATE TABLE Users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,  
    passwordHash VARCHAR(255) NOT NULL,
    roleId INT NOT NULL,               
    clientId INT NULL,               
    FOREIGN KEY (roleId) REFERENCES Role(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (clientId) REFERENCES Client(id) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE Machine (
    id INT AUTO_INCREMENT PRIMARY KEY,
    machineModelId INT NOT NULL,       
    clientId INT NOT NULL,             
    FOREIGN KEY (machineModelId) REFERENCES MachineModel(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (clientId) REFERENCES Client(id) ON DELETE RESTRICT ON UPDATE CASCADE
);


CREATE TABLE Repair (
    id INT AUTO_INCREMENT PRIMARY KEY,
    startDate DATE NOT NULL,
    machineId INT NOT NULL, 
    repairTypeId INT NOT NULL,    
    FOREIGN KEY (machineId) REFERENCES Machine(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (repairTypeId) REFERENCES RepairType(id) ON DELETE RESTRICT ON UPDATE CASCADE
);

INSERT INTO Role (roleName) VALUES ('Администратор'), ('Клиент');

INSERT INTO RepairType (name, cost, durationDays) VALUES 
('Плановое ТО', 1000.00, 1), 
('Капитальный ремонт', 20000.00, 30), 
('Срочный ремонт', 5000.00, 3), 
('Мелкий ремонт', 3000.00, 2), 
('Диагностика', 1000.00, 1), 
('Ремонт электроники', 7000.00, 4), 
('Ремонт гидравлики', 8000.00, 5), 
('Замена изношенных деталей', 6000.00, 3), 
('Модернизация', 10000.00, 15);