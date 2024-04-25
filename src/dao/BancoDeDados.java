package dao;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Map;
import java.util.Properties;

public class BancoDeDados {

	public static Connection conn = null;
	
	
	
	private static Properties carregarPropriedades() throws IOException{
		FileInputStream propriedadesBanco = null;
		propriedadesBanco = new FileInputStream ("database.properties");
		
		Properties props = new Properties();
		props.load(propriedadesBanco);
		return props;
	}
	
	public static Connection Conectar() throws IOException, SQLException {
		
		if(conn == null ) {
			Properties props = carregarPropriedades();
			String url = props.getProperty("dburl");
			conn = DriverManager.getConnection(url,props);
			
		}
		return conn;
	}
	
	public static void Desconectar() throws SQLException {
		if(conn != null) {
			conn.close();
			conn = null;
			
		}
	}

	
	
	

}
