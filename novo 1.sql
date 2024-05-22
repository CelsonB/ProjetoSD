

CREATE DATABASE Projetosd; 
USE Projetosd;
create table candidato ( nome char(40) not null, email char(50) not null, senha char(50) not null, PRIMARY KEY (email));
create table empresa (razao_social char(50) not null, email char(50) not null, senha char(50) not null, ramo char(50), descricao char (255) not null);