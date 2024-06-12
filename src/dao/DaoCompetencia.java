package dao;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import entities.Candidato;
import entities.Competencia;
import entities.Empresa;
import entities.Vaga;

public class DaoCompetencia extends BancoDeDados{

	public DaoCompetencia() {
		try{
			super.Conectar();
			conn = super.conn;
			
		}catch(Exception ex) {
			
		}
	}
	
	
	public List<Vaga> filtrarVagas(Map<String, Object> data, List<String> competencias,String tipo) throws SQLException, IOException {
		
		PreparedStatement st = null;
		Conectar();
		String comando = "Select * from vaga inner join vaga_competencia on vaga.id_vaga = vaga_competencia.id_vaga inner join competencia on competencia.id_competencia = vaga_competencia.id_competencia "
				+ "where competencia in ('?' ";
		String where = " ,'?' ";
				
		
		int i = 0;
		for(String str : competencias) {
					comando = comando.concat(where).replace("?", str);
					i++;
		}
		comando = comando.concat(")");
	
		if(data.get("tipo").toString().toUpperCase().equals("AND")) {
			String valor = String.valueOf(i) ;
			String AND = " HAVING COUNT( vaga.id_vaga_competencia) >= ?";
			comando = comando.concat(AND).replace("?", valor);
		}
		st = conn.prepareStatement (comando);
		
		ResultSet rs = st.executeQuery();
		
		List<Vaga> vagas = new ArrayList<>();
	
		while(rs.next()) {
			ArrayList<String> competenciasVagas =  new ArrayList<>();
			
			st = conn.prepareStatement ("select email from empresa where id_empresa = ?");
			
			st.setInt(1,rs.getInt("id_empresa"));
			ResultSet email =  st.executeQuery();
			email.next();
			
			st = conn.prepareStatement ("select competencia from competencia inner join vaga_competencia on vaga_competencia.id_competencia = competencia.id_competencia where vaga_competencia.id_vaga = ? ");
			st.setInt(1,rs.getInt("id_vaga"));
			ResultSet comps = st.executeQuery();
			
			while(comps.next()) {
				competenciasVagas.add(comps.getString("competencia").replace("#", "sharp"));
			}
			
			
			Vaga vagaTemp = new Vaga();
			vagaTemp.setIdVaga(rs.getInt("id_vaga"));
			vagaTemp.setNome(rs.getString("nome"));
			vagaTemp.setFaixaSalarial(Float.parseFloat(rs.getString("faixa_salarial")));
			vagaTemp.setDescricao(rs.getString("descricao"));
			vagaTemp.setEstado(rs.getString("estado"));
			vagaTemp.setEmail(email.getString("email"));
			vagaTemp.setCompetencias(competenciasVagas);
			vagas.add(vagaTemp);
			
			System.out.println("array dao: "+ vagaTemp.toString());
		}
		
		return vagas;
		
	}

	public void cadastrarExperienciaCandidato(String email, JSONArray competencia )throws SQLException, IOException, JSONException  {
		
		
		PreparedStatement st = null;
		
		
	
			Conectar();
				
			for(int i=0; i<competencia.length();i++) {
				JSONObject objJson = new JSONObject();
				objJson = competencia.getJSONObject(i);
				
			
				st = conn.prepareStatement ("insert into Candidato_Competencia (id_candidato, id_competencia, experiencia) "
						+ "values ((select id_candidato from candidato where email = ?)"
						+ ",(select id_competencia from competencia where competencia = ?)"
						+ ",?)");

				st.setString(1,email);
				st.setString(2, objJson.getString("competencia").replace("sharp","#"));
				st.setInt(3, objJson.getInt("experiencia"));
				st.executeUpdate();
			
			
			}
		
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
	

	public int apagarExperienciaCandidato(String email, String competencia,int exp) throws IOException, SQLException {

		Conectar();
		PreparedStatement st = null;
		
		
		st = conn.prepareStatement ("delete from Candidato_Competencia where id_candidato = (select id_candidato from candidato where email = ?) "
				+ "and id_competencia =  (select id_competencia from competencia where competencia = ?)"
				+ "and experiencia = ?");
		st.setString(1,email);
		st.setString(2, competencia.replace("sharp","#"));
		st.setInt(3, exp);
		return st.executeUpdate();
		//delete from Candidato_Competencia where id_candidato = (select id_candidato from candidato where email = "celsonb") and id_competencia =  (select id_competencia from competencia where competencia = "python");
		//delete from Candidato_Competencia where id_candidato = (select id_candidato from candidato where email = ?) and id_competencia =  (select id_competencia from competencia where competencia = ?
	}
	
	
	
	
	
}
