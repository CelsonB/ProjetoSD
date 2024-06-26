package dao;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import entities.Candidato;
import entities.Competencia;
import entities.Vaga;

public class VagasDao extends BancoDeDados {

	
	
	public List<Candidato> filtrarCandidatos(List<Competencia> competencias, String tipo) throws IOException, SQLException {
		
		
		
		
		
		PreparedStatement st = null;
		Conectar();
		String str = "SELECT * candidato as c inner join candidato_competencia as cc on c.id_candidato = cc.id_candidato where ";
		
		String linhaCompetencia = " cc.id_competencia = (select id_competencia from competencia where competencia = ?) ";
		String linhaExperiencia = " cc.experiencia >= ?  ";
		
		for(Competencia comp : competencias) {
			
			str.concat("(" + linhaCompetencia).replace("?", comp.getNomeCompetencia());
			str.concat(" AND ");
			str.concat(linhaExperiencia +")").replace("?", String.valueOf(comp.getExperiencia()));
			
			str.concat(tipo);
			
		}
		
		st = conn.prepareStatement (str);
		
		ResultSet rs = st.executeQuery();
		
		List<Candidato> listaCandidatos = new ArrayList<>();
		
		while(rs.next()) {
			Candidato cand = new Candidato();
			cand.setEmail(rs.getString("email"));
			cand.setNome(rs.getString("nome"));
			st = conn.prepareStatement("Select * from competencia as c "
					+ "inner join candidato_competencia as cc on c.id_competencia=cc.id_competencia where cc.id_candidato "
					+ "= (select id_candidato from candidato where email = ?) ");
			st.setString(1, cand.getEmail());
			ResultSet rs2 = st.executeQuery();
			List<Competencia> listaCompetencias = new ArrayList<>();
			
				while(rs2.next()) {
					listaCompetencias.add(new Competencia(rs.getInt("experiencia"),rs.getString("competencia"))) ;
				}
			cand.setListaCompetencia(listaCompetencias);
			
			
		}
		return listaCandidatos;
		
	}
	
	
	
	public void atualizarVaga(String email, float faixa, String descricao) throws IOException, SQLException {
		PreparedStatement st = null;
		Conectar();	
		st = conn.prepareStatement ("update vaga set faixa_salarial =? , descricao = ? where id_empresa = (select id_empresa from empresa where email = ?)");
		st.setFloat(1, faixa);
		st.setString(2, descricao);
		st.setString(3,email);
		st.executeUpdate();
	}
	public Vaga visualizarVaga(int id) throws SQLException, IOException {
		PreparedStatement st = null;
		Conectar();	

		st = conn.prepareStatement ("select * from vaga INNER JOIN vaga_competencia ON vaga_competencia.id_vaga = vaga.id_vaga where vaga.id_vaga = ?");
		st.setInt(1,id);
		ResultSet rs  = st.executeQuery();

		Vaga vagaTemp = new Vaga();
		
		if(rs.next()) {
			vagaTemp.setIdVaga(rs.getInt("id_vaga"));
			vagaTemp.setNome(rs.getString("nome"));
			vagaTemp.setFaixaSalarial(Float.parseFloat(rs.getString("faixa_salarial")));
			vagaTemp.setDescricao(rs.getString("descricao"));
			vagaTemp.setEstado(rs.getString("estado"));
		}
		
		st = conn.prepareStatement ("select competencia from competencia inner join vaga_competencia on competencia.id_competencia = vaga_competencia.id_competencia where vaga_competencia.id_vaga = ?");
		st.setInt(1,vagaTemp.getIdVaga());
		rs  = st.executeQuery();
		
		ArrayList<String> competencias = new ArrayList<>();
		while(rs.next()) {
			competencias.add(rs.getString("competencia"));
		}
		
		vagaTemp.setCompetencias(competencias);
		
		
		return vagaTemp;
	}
	public void cadastrarVaga(Map<String, Object> dados,List<String> competencias) throws SQLException, IOException {
		int op = 0; 
	
		PreparedStatement st = null;
		Conectar();	
		//Entrada: [{estado=seila, operacao=cadastrarVaga, faixaSalarial=10000, email=seila@, competencias=[python, c#, c++], descricao=seila, token=1cdb3a5e-ea56-4cbd-9f4f-878f019a2d58}]
		st = conn.prepareStatement ("insert into vaga (id_empresa, faixa_salarial, descricao,nome,estado) values ((select id_empresa from empresa where email = ?), ?,?,?,?)");
		String estado = "divulgavel";
		String email = dados.get("email").toString();
		Float faixaSalarial = Float.parseFloat(dados.get("faixaSalarial").toString());
		String descricao = dados.get("descricao").toString();
		String nome = dados.get("nome").toString();
		if(dados.get("estado")!=null) estado = dados.get("estado").toString();
		
		st.setString(1,email);
		st.setFloat(2,faixaSalarial );
		st.setString(3, descricao);
		st.setString(4, nome);
		st.setString(5, estado);
		op = st.executeUpdate();
		
		if( op != 0) {
			st = conn.prepareStatement ("select id_vaga from vaga where (id_empresa= (select id_empresa from empresa where email = ?) and  faixa_salarial= ? and descricao = ? and nome =? and estado =?)");
			st.setString(1,dados.get("email").toString());
			st.setFloat(2, Float.parseFloat(dados.get("faixaSalarial").toString()));
			st.setString(3, dados.get("descricao").toString());
			st.setString(4, dados.get("nome").toString());
			st.setString(5, estado);
			ResultSet rs = st.executeQuery();
			if(rs.next())op = rs.getInt("id_vaga");
			
			for(String str : competencias) {
				st = conn.prepareStatement ("insert into vaga_competencia (id_vaga,id_competencia) values (?, (select id_competencia from competencia where competencia = ?))");
				st.setInt(1, op);
				st.setString(2, str.replace("sharp", "#"));
				st.executeUpdate();
			}
			
			
		}
		
		
		
	}
	
	
	public void apagarVaga(String email, int idVaga ) throws IOException, SQLException {
		Conectar();
		PreparedStatement st = null;
		st = conn.prepareStatement ("delete from vaga where id_empresa = (select id_empresa from empresa where email = ?) and id_vaga = ?");
		st.setString(1,email);
		st.setInt(2, idVaga);
		st.executeUpdate();
	}
	
	public List<Vaga> listarVagas(String email) throws IOException, SQLException {
		Conectar();
		PreparedStatement st = null;
		st = conn.prepareStatement ("SELECT id_vaga, nome FROM vaga INNER JOIN empresa ON vaga.id_empresa = (select id_empresa from empresa where email = ?) group by id_vaga");
		st.setString(1, email);
		ResultSet rs = st.executeQuery();
		
		List<Vaga> vagas = new ArrayList<>();
	
		while(rs.next()) {
			Vaga vagaTemp = new Vaga();
			vagaTemp.setIdVaga(rs.getInt("id_vaga"));
			vagaTemp.setNome(rs.getString("nome"));
			vagas.add(vagaTemp);
			System.out.println("array dao: "+ vagaTemp.toString());
		}
		return vagas;
		
		
	}
	
}
