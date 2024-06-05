package servidor;

import java.io.IOException;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dao.DaoCompetencia;
import dao.VagasDao;

public class ServidorVaga extends MainServidor {
	

	public void atualizarVaga(Map<String, Object> userData) {
		VagasDao bd = new VagasDao();
		
		try {
		
			float faixa = Float.parseFloat( userData.get("faixaSalarial").toString());
			bd.atualizarVaga(userData.get("email").toString(),faixa, userData.get("descricao").toString());
			respostaServidor(userData.get("operacao").toString());
		} catch (Exception e) {
			respostaExcecao(e, userData.get("operacao").toString(),"422");
		}
	}
	
	public void visualizarVaga(Map<String, Object> userData) {
	
		VagasDao bd = new VagasDao();
		try {
			ResultSet rs =  bd.visualizarVaga(userData.get("email").toString());
			if(rs==null)System.out.println("Alguem erro aconteceu");
			
				
			System.out.println("teste3 ");
			respostaServidorLeitura(rs);
		}catch(Exception ex) {
			System.out.println(ex);
		}
	}
	
	public void cadastrarVaga(Map<String, Object> userData) {
		VagasDao bd = new VagasDao();
		
		try {
		
			float faixa = Float.parseFloat( userData.get("faixaSalarial").toString());
			bd.cadastrarVaga(userData.get("email").toString(),faixa, userData.get("descricao").toString());
			respostaServidor(userData.get("operacao").toString());
		} catch (Exception e) {
			respostaExcecao(e, userData.get("operacao").toString(),"422");
		}
		
	}
	public void apagarVaga(Map<String, Object> userData) {
		
	}
	
	
	 private void respostaServidor(String operacao ) throws JSONException, IOException {
			
		 String myString = null;
		 PrintStream saida  = new PrintStream (ss.getOutputStream());
		 String tokenString = "201";
		 if(operacao.equals("cadastrarVaga") || operacao.equals("apagarVaga") ) {
			 
			 myString = new JSONObject()
					 .put("operacao", operacao)
					 .put("status",tokenString)
					 .put("mensagem", "Competencia/Experiencia cadastrada com sucesso")
					 .toString(); 
			 
			 System.out.println("Saida: ["+myString+"]");
			 
			 saida.println(myString);
			 
		 }else if(operacao.equals("atualizarVaga")){
			 myString = new JSONObject()
					 .put("operacao", operacao)
					 .put("status",tokenString)
					 .put("mensagem", "Atualização realizar com sucesso")
					 .toString(); 
			 
			 System.out.println("Saida: ["+myString+"]");
			 
			 saida.println(myString);
		 }
		 
	 }
	 private void respostaServidorLeitura(ResultSet rs) {
			
			try {
				
				PrintStream saida  = new PrintStream (ss.getOutputStream());
				 JSONObject json = new JSONObject();
				 JSONObject compJson= new JSONObject();
			
				 JSONArray competencias = new JSONArray();
				 //competencias.put(rsToJson(rs));
				 
				String myString	= new JSONObject().put("operacao",  "visualizarVaga").put("CompetenciaExperiencia", rsToJson(rs)).toString();
				
				System.out.println("Saida: ["+myString+"]"); 
				saida.println(myString);
			}catch(Exception ex) {
				
			}
			
			
			
		}
	 
	 private void respostaExcecao(Exception ex, String operacao, String numStatus)  {
		 String myString = null;
		 
		
		 try {
			 JSONObject json = new JSONObject();
			 json.put("type", "CONNECT");
			 
					PrintStream saida  = new PrintStream (ss.getOutputStream());
					myString = new JSONObject().put("operacao", operacao).put("status", numStatus).put("mensagem", ex).toString(); 
					 System.out.println("Saida: ["+myString+"]"); 
					saida.println(myString);


		 }catch(Exception exe) {
			 System.err.println(exe);
		 }
		 
			
	 }
	 
	 

}
