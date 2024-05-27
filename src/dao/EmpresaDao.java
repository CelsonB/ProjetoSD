package dao;

import java.io.IOException;
import java.sql.*;

import entities.Empresa;

	public class EmpresaDao extends BancoDeDados{
		BancoDeDados bd = null;
		public static Connection conn = null;
		
		public EmpresaDao() {
			try{
				super.Conectar();
				conn = super.conn;
				
			}catch(Exception ex) {
				
			}
		}
		
		public static void cadastrarEmpresa(Empresa empresa)throws SQLException, IOException  {
			
			
			PreparedStatement st = null;
			
			
		
				Conectar();
				
				st = conn.prepareStatement ("insert into empresa (razao_social, email, senha,ramo,descricao) values (?,?,?,?,?)");
				st.setString(1,empresa.getRazaoSocial());
				st.setString(2, empresa.getEmail());
				st.setString(3,empresa.getSenha());
				st.setString(4,empresa.getRamo());
				st.setString(5,empresa.getDescricao());
				st.executeUpdate();
		}
		
		
		public ResultSet realizarLogin(String email, String senha)  throws SQLException, IOException {
			
			

			super.Conectar();
			
			PreparedStatement st = null;
			st = conn.prepareStatement ("SELECT * FROM empresa WHERE email = ? and senha = ? ");
			st.setString(1,email);
			st.setString(2,senha);
			ResultSet rs = st.executeQuery();
			
			if(rs.next()) {
				return rs;
			}else {
				return null;
			}
	}
	
	
	public ResultSet Ler(String email) throws SQLException {
		ResultSet rs = null;
		try {
			super.Conectar();
			PreparedStatement st = null;
			st = conn.prepareStatement ("SELECT * FROM empresa WHERE email = ?");
			st.setString(1,email);
			
			rs = st.executeQuery();
			return rs;
			
			
			
			
			
		}catch(Exception ex) {
			System.err.print(ex);
		}finally {
			return rs; 
		}
		
	}
	
	public boolean atualizarCadastro(Empresa empresa) {
		int op=1;
		try {
			super.Conectar();
			PreparedStatement st = null;
			
			
			st = conn.prepareStatement ("UPDATE empresa SET razao_social = ? , senha = ? ,ramo = ? ,descricao = ? WHERE email = ? ");
			st.setString(1,empresa.getRazaoSocial());
			st.setString(2,empresa.getSenha());
			st.setString(3,empresa.getRamo());
			st.setString(4,empresa.getDescricao());
			st.setString(5,empresa.getEmail());
			op = st.executeUpdate();
			
			
			
			
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
	
	
	
	public boolean apagarEmpresa(String email) throws SQLException, IOException {
		super.Conectar();
		System.out.println("2" + email);
		PreparedStatement st = null;
		st = conn.prepareStatement ("DELETE FROM empresa WHERE email = ?");
		st.setString(1, email);
		int op  = st.executeUpdate();
		
		if(op == 0 ) {
			return false;
		}else {
			return true;
		}
		
	}
		
		
		
}
