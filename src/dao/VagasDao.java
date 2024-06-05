package dao;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

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
	public ResultSet visualizarVaga(String email) throws SQLException, IOException {
		PreparedStatement st = null;
		Conectar();	
		st = conn.prepareStatement ("select * from vaga where id_empresa = (select id_empresa from empresa where email = ?)");
		st.setString(1,email);
		ResultSet rs  = st.executeQuery();
		return rs;
	}
	public void cadastrarVaga(String email, float faixa, String descricao) throws SQLException, IOException {
		PreparedStatement st = null;
		Conectar();	
		st = conn.prepareStatement ("insert into vaga (id_empresa, faixa_salarial, descricao) values ((select id_empresa from empresa where email = ?), ?,?)");
		st.setString(1,email);
		st.setFloat(2, faixa);
		st.setString(3, descricao);
		st.executeUpdate();
	}
	public void apagarVaga(String email) throws IOException, SQLException {
		Conectar();
		PreparedStatement st = null;
		st = conn.prepareStatement ("delete from vaga where id_empresa = (select id_empresa from candidato where email = ?)");
		st.setString(1,email);
		st.executeUpdate();
	}
	
}
