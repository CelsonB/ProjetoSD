package servidor;

import java.io.IOException;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import dao.BancoDeDados;
import dao.DaoCompetencia;
import dao.EmpresaDao;
import entities.Competencia;
import entities.Empresa;

public class ServidorCompetencia extends MainServidor {

	public void cadastrarCandidatoCompetencia(Map<String, Object> dataCandidato, Map<String, String> dataCompetencia ) {
		
		DaoCompetencia bd = new DaoCompetencia();
		
		int periodo =  Integer.parseInt(dataCompetencia.get("experiencia").toString());
				
				
				
				
		
		try {
		
			
			bd.cadastrarExperienciaCandidato(dataCandidato.get("email").toString(), dataCompetencia.get("competencia").toString(), periodo);
			respostaServidor(dataCandidato.get("operacao").toString());
		} catch (Exception e) {
			respostaExcecao(e, dataCandidato.get("operacao").toString(),"422");
		}
		
	
		
	}
	public void atualizarCompetencia(Map<String, Object> data) {
	try {
		DaoCompetencia bd = new DaoCompetencia();
		System.out.println("teste 1 ");
		JSONArray jsonArr = StringToJSONArray(data);
		
		 for (int i = 0; i < jsonArr.length(); i++)
	        {
	            JSONObject jsonObj = jsonArr.getJSONObject(i);
	            int tempo = Integer.parseInt(jsonObj.get("experiencia").toString());
	        	bd.atualizarCompetenciaExperiencia(data.get("email").toString(),tempo, jsonObj.get("competencia").toString());
	        }
		 
	} catch (Exception e) {
		System.err.println(e);
		respostaExcecao(e, data.get("operacao").toString(),"422");
	}
	}
	
	public void visualizarCompetenciaExperiencia(Map<String, Object> data) {
		System.out.println("teste1 ");
		DaoCompetencia bd = new DaoCompetencia();
		try {
			System.out.println("teste2 ");
			ResultSet rs =  bd.visualizarExperienciaCandidato(data.get("email").toString());
			if(rs==null)System.out.println("Alguem erro aconteceu");
			
				
			System.out.println("teste3 ");
			respostaServidorLeitura(rs);
		}catch(Exception ex) {
			System.out.println(ex);
		}
	}
	
	public void apagarCompetenciaExperiencia(Map<String, Object> data,Map<String, String> dataCompetencia ) {
	
		
		DaoCompetencia bd = new DaoCompetencia();
		
		//int periodo =  Integer.parseInt(dataCompetencia.get("experiencia").toString());
				
		try {
			System.out.println("teste 1 ");
			JSONArray jsonArr = StringToJSONArray(data);
			
			 for (int i = 0; i < jsonArr.length(); i++)
		        {
		            JSONObject jsonObj = jsonArr.getJSONObject(i);
		        	bd.apagarExperienciaCandidato(data.get("email").toString(), jsonObj.get("competencia").toString());
		        }
			 
		
			
			
			
			respostaServidor(data.get("operacao").toString());
		} catch (Exception e) {
			System.err.println(e);
			respostaExcecao(e, data.get("operacao").toString(),"422");
		}
		
	
	}
	
	private JSONArray StringToJSONArray(Map<String, Object> data) throws JSONException {
		
		
		String competencias = new JSONObject().put("competenciaExperiencia",data.get("competenciaExperiencia").toString()).toString();
		System.out.println("<"+data.get("competenciaExperiencia").toString()+">");
		JSONArray jsonArr = new JSONArray(data.get("competenciaExperiencia").toString());
		System.out.println("teste 2 ");
        return jsonArr;
        
        
	}

	private void respostaServidorLeitura(ResultSet rs) {
		
		try {
			
			PrintStream saida  = new PrintStream (ss.getOutputStream());
			 JSONObject json = new JSONObject();
			 JSONObject compJson= new JSONObject();
		
			 JSONArray competencias = new JSONArray();
			 //competencias.put(rsToJson(rs));
			 
			String myString	= new JSONObject().put("operacao",  "visualizarCompetenciaExperiencia").put("CompetenciaExperiencia", rsToJson(rs)).toString();
			
			System.out.println("Saida: ["+myString+"]"); 
			saida.println(myString);
		}catch(Exception ex) {
			
		}
		
		
		
	}
	private static JSONArray rsToJson(ResultSet rs ) throws SQLException {
		ResultSetMetaData md = rs.getMetaData();
		int numCols = md.getColumnCount();
		List<String> colNames = IntStream.range(0, numCols)
		  .mapToObj(i -> {
		      try {
		          return md.getColumnName(i + 1);
		      } catch (SQLException e) {
		          e.printStackTrace();
		          return "?";
		      }
		  })
		  .collect(Collectors.toList());

		JSONArray result = new JSONArray();
		while (rs.next()) {
		    JSONObject row = new JSONObject();
		    colNames.forEach(cn -> {
		        try {
		            row.put(cn, rs.getObject(cn));
		        } catch (JSONException | SQLException e) {
		            e.printStackTrace();
		        }
		    });
		    result.put(row);
		}
		return result;
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
	
	 
	 
	 private void respostaServidor(String operacao ) throws JSONException, IOException {
		
		 String myString = null;
		 PrintStream saida  = new PrintStream (ss.getOutputStream());
		
		 if(operacao.equals("cadastrarCompetenciaExperiencia")) {
			 
			 String tokenString = "201";
			 
			 
			 myString = new JSONObject()
					 .put("operacao", operacao)
					 .put("status",tokenString)
					 .put("mensagem", "Competencia/Experiencia cadastrada com sucesso")
					 .toString(); 
			 
			 System.out.println("Saida: ["+myString+"]");
			 
			 saida.println(myString);
			 
		 }else if(operacao.equals("apagarCompetenciaExperiencia")) {
		String tokenString = "201";
			 
			 
			 myString = new JSONObject()
					 .put("operacao", operacao)
					 .put("status",tokenString)
					 .put("mensagem", "Competencia/Experiencia cadastrada com sucesso")
					 .toString(); 
			 
			 System.out.println("Saida: ["+myString+"]");
			 
			 saida.println(myString);
		 }else {
			 
		 }
		 
	 }
		 
	private void respostaServidor(String operacao , Empresa resultado ) throws JSONException, IOException {
		 
			 
			 String myString = null;
			 PrintStream saida  = new PrintStream (ss.getOutputStream());
			 
			 if(operacao.equals("visualizarEmpresa")) {
				
				 myString = new JSONObject().put("operacao", operacao).toString();
						
				saida.println(myString);
			 }
	 }
	 
	 
	 
	 
	 
	 
	 
	 
}
