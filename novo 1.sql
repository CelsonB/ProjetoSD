

CREATE DATABASE Projetosd; 
USE Projetosd;
create table candidato (id_candidato int not null AUTO_INCREMENT,  nome char(40) not null, email char(50) not null unique, senha char(50) not null, PRIMARY KEY (id_candidato));

create table empresa (id_empresa int not null AUTO_INCREMENT ,razao_social char(50) not null, email char(50) not null, senha char(50) not null, ramo char(50), descricao char (255) not null, cnpj char(50),PRIMARY KEY (id_empresa));

create table Candidato_Competencia (id_candidato_competencia int not null AUTO_INCREMENT, id_candidato int not null, id_competencia int not null, experiencia int not null,PRIMARY KEY (id_candidato_competencia)); 

create table competencia (id_competencia int not null AUTO_INCREMENT, competencia char (50) not null, PRIMARY KEY (id_competencia)); 

create table Candidato_vaga (id_candidato_vaga int not null AUTO_INCREMENT, id_candidato int not null, id_vaga int not null, visualizou bool not null,PRIMARY KEY (id_candidato_vaga));

create table Vaga_competencia(id_vaga_competencia int not null AUTO_INCREMENT, id_vaga int not null, id_competencia int not null, PRIMARY KEY (id_vaga_competencia));

					create table vaga (
					id_vaga int not null AUTO_INCREMENT, 
					nome VARCHAR(255) not null,
					id_empresa int not null, 
					faixa_salarial int not null, 
					descricao char(255) not null,
					estado varchar(50) not null,
					primary key (id_vaga) );
					
					CREATE TABLE candidato_mensagem (
					id INT NOT NULL AUTO_INCREMENT,
					id_candidato INT NOT NULL,
					id_empresa INT NOT NULL,
					PRIMARY KEY (id)
					);

INSERT INTO competencia (competencia) VALUES ('Python'), ('C#'), ('C++'), ('JS'), ('PHP'), ('Swift'), ('Java'), ('Go'), ('SQL'), ('Ruby'), ('HTML'), ('CSS'), ('NOSQL'), ('Flutter'), ('TypeScript'), ('Perl'), ('Cobol'), ('dotNet'), ('Kotlin'), ('Dart');





------------------------------------------------------------------------------------------------------------------- A partir daqui apenas testes -------------------------------------------------------------------------------------------------------------------------------

select competencia, tempo from competencia inner join Candidato_Competencia on Candidato_Competencia.id_competencia = competencia.id_competencia where id_candidato = (select id_candidato from candidato where email = "celson@");

ALTER TABLE Candidato_Competencia RENAME COLUMN tempo TO experiencia;

Select * from vaga inner join vaga_competencia on vaga.id_vaga = vaga_competencia.id_vaga where id_vaga = (select id_vaga from vaga_competencia )
 
select * from competencia inner join vaga_competencia on vaga_competencia.id_competencia = competencia.id_competencia ;
 
Select * from vaga inner join vaga_competencia on vaga.id_vaga = vaga_competencia.id_vaga inner join competencia on competencia.id_competencia = vaga_competencia.id_competencia
where vaga.id_vaga = (select id_vaga from vaga_competencia where id_competencia = (select id_competencia from competencia where competencia = 'python')) 
or  vaga.id_vaga = (select id_vaga from vaga_competencia where id_competencia = (select id_competencia from competencia where competencia = 'c++' ))
or  vaga.id_vaga = (select id_vaga from vaga_competencia where id_competencia = (select id_competencia from competencia where competencia = 'c#' ))
group by vaga.id_vaga; 
 
 
Select * from vaga inner join vaga_competencia on vaga.id_vaga = vaga_competencia.id_vaga inner join competencia on competencia.id_competencia = vaga_competencia.id_competencia
where vaga.id_vaga = (select id_vaga from vaga_competencia where id_competencia = (select id_competencia from competencia where competencia = 'python')) 
and  vaga.id_vaga = (select id_vaga from vaga_competencia where id_competencia = (select id_competencia from competencia where competencia = 'c++' ))
and  vaga.id_vaga = (select id_vaga from vaga_competencia where id_competencia = (select id_competencia from competencia where competencia = 'flutter' ))
group by vaga.id_vaga;


select competencia from competencia inner join vaga_competencia on vaga_competencia.id_competencia = competencia.id_competencia where vaga_competencia.id_vaga = ?; 

Select v.id_vaga, v.nome, v.faixa_salarial, v.descricao, v.estado, competencia.competencia from vaga v inner join vagacompetencia vc on v.id_vaga = vc.id_vaga inner join competencia on competencia.id_competencia = vc.id_competencia where competencia in ('python','c++') HAVING COUNT(vc.id_vaga_competencia) >= 2;



