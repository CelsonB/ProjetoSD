package servidor;

import java.io.IOException;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mysql.cj.xdevapi.JsonArray;

import dao.DaoCompetencia;
import dao.VagasDao;
import entities.Vaga;

public class ServidorVaga extends MainServidor {
	

	public void listarVagas(String op,Map<String, Object> data){
		VagasDao bd = new VagasDao();
		try {
			
			List<Vaga> vagas = bd.listarVagas(data.get("email").toString());
			
			JSONArray jArry = new JSONArray(vagas);
			

			respostarServidorListarTodos(jArry);
			
		} catch (Exception e) {
			respostaExcecao(e, op,"422");
		} 
		
		
		
	}
	
	
	
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
			Vaga vagaTemp =  bd.visualizarVaga(Integer.parseInt(userData.get("idVaga").toString()));
			
			respostaServidorLeitura(vagaTemp);
		}catch(Exception ex) {
			respostaExcecao(ex, userData.get("operacao").toString(),"422");
		}
	}
	
	public void cadastrarVaga(Map<String, Object> userData) {
		VagasDao bd = new VagasDao();
		
		try {
		
			
			
			List<String> competencias = converterJsonArrayToList(userData.get("competencias").toString());
			for(String str : competencias) {
				System.out.println(str);
			}
			
			//JSONArray jsonArr = new JSONArray(userData.get("competencias").toString());
			//System.out.println(jsonArr.toString());			
			bd.cadastrarVaga(userData,competencias);
			
			
			
			
			
			respostaServidor(userData.get("operacao").toString());
		} catch (Exception e) {
			respostaExcecao(e, userData.get("operacao").toString(),"422");
		}
		
	}
	public void apagarVaga(Map<String, Object> userData) {
		VagasDao bd = new VagasDao();
	try {
		
	
		
	int idVaga = Integer.parseInt(userData.get("idVaga").toString());
	        	bd.apagarVaga(userData.get("email").toString(), idVaga );
		
		respostaServidor(userData.get("operacao").toString());
	} catch (Exception e) {
		System.err.println(e);
		respostaExcecao(e, userData.get("operacao").toString(),"422");
	}
	
	}
	
	
	 private void respostaServidor(String operacao ) throws JSONException, IOException {
			
		 String myString = null;
		 PrintStream saida  = new PrintStream (ss.getOutputStream());
		 String tokenString = "201";
		 if(operacao.equals("cadastrarVaga") || operacao.equals("apagarVaga") ) {
			 String str = operacao.concat("realizada com sucesso");
			 myString = new JSONObject()
					 .put("operacao", operacao)
					 .put("status",tokenString)
					 .put("mensagem", str)
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
	 
	 private void respostarServidorListarTodos(JSONArray jArry) {
		 try {
//				
				PrintStream saida  = new PrintStream (ss.getOutputStream());
				JSONObject json = new JSONObject();
				JSONObject compJson= new JSONObject();				 
				String myString	= new JSONObject()
						.put("operacao",  "listarVagas")
						.put("vagas", jArry)
						.put("status",201)
						.toString();
				System.out.println("Saida: ["+myString+"]"); 
				saida.println(myString);
			}catch(Exception ex) {
				
			}
	 }
	 private void respostaServidorLeitura(Vaga vagaTemp) {
			
			try {
				JSONArray jsonArray = new JSONArray(vagaTemp.getCompetencias());
				
				

//				
				PrintStream saida  = new PrintStream (ss.getOutputStream());
				JSONObject json = new JSONObject();
				JSONObject compJson= new JSONObject();				 
				String myString	= new JSONObject()
						.put("operacao",  "visualizarVaga")
						.put("faixaSalarial", vagaTemp.getFaixaSalarial())
						.put("descricao",vagaTemp.getDescricao())
						.put("estado", vagaTemp.getEstado())
						.put("competencias", jsonArray)
						.put("status", 201)
						.toString();
				
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
	 
	 private static List<String> converterJsonArrayToList (String data) {
			
			data = data.replace("[", "").replace("]", "");
		    String[] array = data.split(", ");
		    List<String> list = new ArrayList<>();
		    for (String s : array) {
		        list.add(s);
		    }
		    return list;
		}
	 
	 

}
