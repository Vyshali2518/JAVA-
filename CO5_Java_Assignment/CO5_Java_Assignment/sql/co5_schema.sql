CREATE DATABASE IF NOT EXISTS co5_assignment;
USE co5_assignment;

CREATE TABLE IF NOT EXISTS bus_pass (
    pass_id VARCHAR(30) PRIMARY KEY,
    reg_no VARCHAR(30) NOT NULL,
    name VARCHAR(100) NOT NULL,
    route_no INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    INDEX idx_bus_pass_route (route_no),
    INDEX idx_bus_pass_reg (reg_no)
);

CREATE TABLE IF NOT EXISTS student (
    student_id INT PRIMARY KEY,
    student_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE,
    department VARCHAR(80)
);

CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(100) PRIMARY KEY,
    pwd VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS wallet (
    wallet_id INT PRIMARY KEY,
    balance DECIMAL(12,2) NOT NULL CHECK (balance >= 0)
);

-- Local demonstration account only. Do not use real credentials.
INSERT INTO users(id, pwd) VALUES ('demo', 'demo123')
ON DUPLICATE KEY UPDATE pwd = VALUES(pwd);

INSERT INTO wallet(wallet_id, balance) VALUES (1, 5000.00), (2, 1000.00)
ON DUPLICATE KEY UPDATE balance = VALUES(balance);
