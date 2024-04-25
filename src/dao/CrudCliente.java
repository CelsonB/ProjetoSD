package dao;
import java.sql.*;
import java.io.FileInputStream;
import java.io.IOException;

public class CrudCliente extends BancoDeDados{
		BancoDeDados bd = null;
		public static Connection conn = null;
		
		public CrudCliente() {
			try{
				super.Conectar();
				conn = super.conn;
				
			}catch(Exception ex) {
				
			}
		}
	
	
	public void Cadastrar(String email, String nome, String senha) throws SQLException {
		
		PreparedStatement st = null;
		
		
		try {
			super.Conectar();
			st = conn.prepareStatement ("insert into candidato (id_candidato, nome, email, senha) values (22131, ?,?,?)");
			
			st.setString(1,nome);
			st.setString(2,email);
			st.setString(3,senha);
			st.executeUpdate();
			
		
		}catch(Exception ex) {
			System.err.print(ex);
		}finally{
			
		}
	}
	
	public void Ler(int id) throws SQLException {
		try {
			super.Conectar();
			PreparedStatement st = null;
			st = conn.prepareStatement ("SELECT * FROM candidato WHERE id_candidato = ?");
			st.setInt(1,id);
			ResultSet rs = st.executeQuery();
			
			
			while(rs.next())
			{
			   System.out.println(rs.getString(2));
			   System.out.println(rs.getString(3));
			   System.out.println(rs.getString(4));
			}
			
		}catch(Exception ex) {
			System.err.print(ex);
		}finally {
			
		}
	
	}
	
	public void AtualizarSenha(int id, String novo) {
		try {
			super.Conectar();
			PreparedStatement st = null;
			st = conn.prepareStatement ("UPDATE Candidato SET senha = ? WHERE id_candidato = ?");
			st.setString(1, novo);
			st.setInt(2, id);
			st.executeQuery();
			
			
			
			
		}catch(Exception ex) {
			System.err.print(ex);
		}finally {
			
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
