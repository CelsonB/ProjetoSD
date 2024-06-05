package dao;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import entities.Candidato;
import entities.Competencia;
import entities.Empresa;

public class DaoCompetencia extends BancoDeDados{

	public DaoCompetencia() {
		try{
			super.Conectar();
			conn = super.conn;
			
		}catch(Exception ex) {
			
		}
	}
	
	

	public void cadastrarExperienciaCandidato(String email,String competencia, int periodo)throws SQLException, IOException  {
		
		
		PreparedStatement st = null;
		
		
	
			Conectar();
				
			st = conn.prepareStatement ("insert into Candidato_Competencia (id_candidato, id_competencia, experiencia) "
					+ "values ((select id_candidato from candidato where email = ?)"
					+ ",(select id_competencia from competencia where competencia = ?)"
					+ ",?)");
			st.setString(1,email);
			st.setString(2, competencia);
			st.setInt(3, periodo);
			st.executeUpdate();
	}
	public void atualizarCompetenciaExperiencia(String email,int experiencia, String competencia) throws SQLException, IOException {

		Conectar();
		PreparedStatement st = null;
		
		
		st = conn.prepareStatement ("update Candidato_Competencia set experiencia = ? where id_candidato = (select id_candidato from candidato where email = ?) and id_competencia = (select id_competencia from competencia where competencia = ? )");
		//update Candidato_Competencia set experiencia = ? where id_candidato = (select id_candidato from candidato where email = ?) and id_competencia = (select id_competencia from competencia where competencia = ? )
		st.setInt(1, experiencia);
		st.setString(2, email);
		st.setString(3, competencia);
		st.executeUpdate();
	}
	public ResultSet visualizarExperienciaCandidato(String email) throws IOException, SQLException {
		Conectar();
		PreparedStatement st = null;
		ResultSet rs=null;
		st = conn.prepareStatement 
		("select competencia,experiencia from competencia inner join Candidato_Competencia on Candidato_Competencia.id_competencia = competencia.id_competencia where id_candidato = (select id_candidato from candidato where email = ?) ");
		st.setString(1,email);
		 rs  = st.executeQuery();
		
		if(rs !=null) {
			return rs;
		}else {
			
		}
	return rs;
	}
	
	public void atualizarExperienciaCandidato() {
		
	}
	public int apagarExperienciaCandidato(String email, String competencia) throws IOException, SQLException {

		Conectar();
		PreparedStatement st = null;
		
		
		st = conn.prepareStatement ("delete from Candidato_Competencia where id_candidato = (select id_candidato from candidato where email = ?) and id_competencia =  (select id_competencia from competencia where competencia = ?)");
		st.setString(1,email);
		st.setString(2, competencia);
		return st.executeUpdate();
		//delete from Candidato_Competencia where id_candidato = (select id_candidato from candidato where email = "celsonb") and id_competencia =  (select id_competencia from competencia where competencia = "python");
		//delete from Candidato_Competencia where id_candidato = (select id_candidato from candidato where email = ?) and id_competencia =  (select id_competencia from competencia where competencia = ?
	}
	
	
	
	
	
	
}
