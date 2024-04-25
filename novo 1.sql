

CREATE DATABASE Projeto-sd; 

create table candidato (id_candidato int not null AUTO_INCREMENT, nome char(40) not null, email char(50) not null, senha char(50) not null, PRIMARY KEY (id_candidato));

ALTER TABLE candidato AUTO_INCREMENT = 1;