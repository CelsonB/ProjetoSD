package dao;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import entities.Vaga;

public class VagasDao extends BancoDeDados {

	
	
	
	public void atualizarVaga(String email, float faixa, String descricao) throws IOException, SQLException {
		PreparedStatement st = null;
		Conectar();	
		st = conn.prepareStatement ("update vaga set faixa_salarial =? , descricao = ? where id_empresa = (select id_empresa from empresa where email = ?)");
		st.setFloat(1, faixa);
		st.setString(2, descricao);
		st.setString(3,email);
		st.executeUpdate();
	}
	public Vaga visualizarVaga(int id ) throws SQLException, IOException {
		PreparedStatement st = null;
		Conectar();	

		st = conn.prepareStatement ("select * from vaga INNER JOIN vaga_competencia ON vaga_competencia.id_vaga = vaga.id_vaga where vaga.id_vaga = ?)");
		st.setInt(1,id);
		ResultSet rs  = st.executeQuery();

		Vaga vagaTemp = new Vaga();
		
		if(rs.next()) {
			vagaTemp.setIdVaga(rs.getInt("id_vaga"));
			vagaTemp.setNome(rs.getString("nome"));
			vagaTemp.setFaixaSalarial(Float.parseFloat(rs.getString("faixa_salarial")));
			vagaTemp.setDescricao(rs.getString("descricao"));
			vagaTemp.setDescricao(rs.getString("estado"));
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
		st.setString(1,dados.get("email").toString());
		st.setFloat(2, Float.parseFloat(dados.get("faixaSalarial").toString()));
		st.setString(3, dados.get("descricao").toString());
		st.setString(4, dados.get("nome").toString());
		st.setString(5, dados.get("estado").toString());
		op = st.executeUpdate();
		
		if( op != 0) {
			st = conn.prepareStatement ("select id_vaga from vaga where (id_empresa= (select id_empresa from empresa where email = ?) and  faixa_salarial= ? and descricao = ? and nome =? and estado =?)");
			st.setString(1,dados.get("email").toString());
			st.setFloat(2, Float.parseFloat(dados.get("faixaSalarial").toString()));
			st.setString(3, dados.get("descricao").toString());
			st.setString(4, dados.get("nome").toString());
			st.setString(5, dados.get("estado").toString());
			ResultSet rs = st.executeQuery();
			if(rs.next())op = rs.getInt("id_vaga");
			
			for(String str : competencias) {
				st = conn.prepareStatement ("insert into vaga_competencia (id_vaga,id_competencia) values (?, (select id_competencia from competencia where competencia = ?))");
				st.setInt(1, op);
				st.setString(2, str);
				st.executeUpdate();
			}
			
			
		}
		
		
		
	}
	
	
	public void apagarVaga(String email, int idVaga) throws IOException, SQLException {
		Conectar();
		PreparedStatement st = null;
		st = conn.prepareStatement ("delete from vaga where id_empresa = (select id_empresa from candidato where email = ?) and id_vaga = ?");
		st.setString(1,email);
		st.setInt(2, idVaga);
		st.executeUpdate();
	}
	
	public List<Vaga> listarVagas(String email) throws IOException, SQLException {
		Conectar();
		PreparedStatement st = null;
		st = conn.prepareStatement ("SELECT id_vaga, nome FROM vaga INNER JOIN empresa ON vaga.id_empresa = (select id_empresa from empresa where email = ?)");
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
