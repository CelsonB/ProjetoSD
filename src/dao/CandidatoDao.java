package dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import entities.Empresa;

import java.io.FileInputStream;
import java.io.IOException;

public class CandidatoDao extends BancoDeDados{
		BancoDeDados bd = null;
		public static Connection conn = null;
		
		public CandidatoDao() {
			try{
				super.Conectar();
				conn = super.conn;
				
			}catch(Exception ex) {
				
			}
		}
	
		public List<Empresa> receberMensagem(String emailCandidato) throws IOException, SQLException {
			PreparedStatement st = null;
			
			
			List<Empresa> empresas = new ArrayList<>();
			super.Conectar();
			st = conn.prepareStatement 
		("SELECT * FROM candidato_mensagem INNER JOIN empresa on candidato_mensagem.id_empresa = empresa.id_empresa WHERE candidato_mensagem.id_candidato = (select id_candidato from candidato where email = ?) ");
			
			st.setString(1,emailCandidato);
			
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				Empresa emp = new Empresa();
				
				
				emp.setEmail(rs.getString("email"));
				
				//resultadoPesquisa.setNome(rs.getString("nome"));
				emp.setRamo(rs.getString("ramo"));
				emp.setRazaoSocial(rs.getString("razao_social"));
				emp.setSenha(rs.getString("senha"));
				empresas.add(emp);
			}
			return empresas;
		}
		
	
	public void Cadastrar(String email, String nome, String senha) throws SQLException, IOException {
		
		PreparedStatement st = null;
		
		
	
			super.Conectar();
			st = conn.prepareStatement ("insert into candidato (nome, email, senha) values (?,?,?)");
			st.setString(1,nome);
			st.setString(2,email);
			st.setString(3,senha);
			st.executeUpdate();
			
		
		
			
		
	}
	public boolean getLogin(String email, String senha)  throws SQLException, IOException {
		
		

			super.Conectar();
			
			PreparedStatement st = null;
			st = conn.prepareStatement ("SELECT * FROM candidato WHERE email = ? and Senha = ? ");
			st.setString(1,email);
			st.setString(2,senha);
			ResultSet rs = st.executeQuery();
			
			if(rs.next()) {
				return true;
			}else {
				return false;
			}
	}
	
	
	public ResultSet Ler(String email) throws SQLException {
		ResultSet rs = null;
		try {
			super.Conectar();
			PreparedStatement st = null;
			st = conn.prepareStatement ("SELECT * FROM candidato WHERE email = ?");
			st.setString(1,email);
			
			rs = st.executeQuery();
			return rs;
			
			
			
			
			
		}catch(Exception ex) {
			System.err.print(ex);
		}finally {
			return rs; 
		}
		
	}
	
	public boolean atualizarCadastro(String email, String nome, String senha) {
		int op=0;
		try {
			super.Conectar();
			PreparedStatement st = null;
			st = conn.prepareStatement ("UPDATE candidato SET senha = ?, nome = ? WHERE email = ?");
			st.setString(1, senha);
			st.setString(2, nome);
			st.setString(3, email);
			op  = st.executeUpdate();
			
			
			
			
			
			
		}catch(Exception ex) {
			System.err.print(ex);
		}finally {
		
			if(op == 0 ) {
				return false;
			}else {
				return true;
			}
			
		}
	}
	
	
	
	public boolean apagarCandidato(String email) throws SQLException, IOException {
		super.Conectar();
		System.out.println("2" + email);
		PreparedStatement st = null;
		st = conn.prepareStatement ("DELETE FROM candidato WHERE email = ?");
		st.setString(1, email);
		int op  = st.executeUpdate();
		
		if(op == 0 ) {
			return false;
		}else {
			return true;
		}
		
	}

}
