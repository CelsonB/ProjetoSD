

CREATE DATABASE Projetosd; 
USE Projetosd;
create table candidato (id_candidato int not null AUTO_INCREMENT,  nome char(40) not null, email char(50) not null unique, senha char(50) not null, PRIMARY KEY (id_candidato));
create table empresa (razao_social char(50) not null, email char(50) not null, senha char(50) not null, ramo char(50), descricao char (255) not null, cnpj char(50));

create table Candidato_Competencia (id_candidato_competencia int not null AUTO_INCREMENT, id_competencia int not null, tempo int not null,PRIMARY KEY (id_candidato_competencia)); 
create table competencia (id_competencia int not null AUTO_INCREMENT, competencia char (50) not null, PRIMARY KEY (id_competencia)); 


insert into Competencia (competencia) values ("python");
insert into Competencia (competencia) values ("c#");
insert into Competencia (competencia) values ("c++");