package dao;
import java.sql.*;
import java.io.FileInputStream;
import java.io.IOException;

public class Crud extends BancoDeDados{
		BancoDeDados bd = null;
		public static Connection conn = null;
		
		public Crud() {
			try{
				super.Conectar();
				conn = super.conn;
				
			}catch(Exception ex) {
				
			}
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
	public int getLogin(String email, String senha)  throws SQLException, IOException {
		
		

			super.Conectar();
			PreparedStatement st = null;
			st = conn.prepareStatement ("SELECT * FROM candidato WHERE Email = ? and Senha = ? ");
			st.setString(1,email);
			st.setString(2,senha);
			ResultSet rs = st.executeQuery();
			rs.next();
			
			if(st!=null) {
				return 1;
			}else {
				return 0;
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
		try {
			super.Conectar();
			PreparedStatement st = null;
			st = conn.prepareStatement ("UPDATE Candidato SET senha = ?, nome = ? WHERE email = ?");
			st.setString(1, senha);
			st.setString(2, nome);
			st.setString(3, email);
			if(st.execute())
			{
			return true; 	
			}else {
			return false; 
			}
			
			
			
			
		}catch(Exception ex) {
			System.err.print(ex);
		}finally {
			return false;
		}
	}
	
	
	public void Apagar(int id) throws SQLException, IOException {
		super.Conectar();
		PreparedStatement st = null;
		st = conn.prepareStatement ("DELETE FROM candidatos WHERE id_candidato = ?");
		st.setInt(1, id);
		st.executeQuery();
	}

}
