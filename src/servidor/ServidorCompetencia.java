package servidor;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import dao.BancoDeDados;
import dao.DaoCompetencia;
import dao.EmpresaDao;
import entities.Competencia;
import entities.Empresa;
import entities.Vaga;

public class ServidorCompetencia 
{

	
	private Socket ss;
	
	
	public ServidorCompetencia( Socket ss) {
		this.ss = ss;
	}
	
	public void filtrarVagas(Map<String, Object> data) {

		DaoCompetencia bd = new DaoCompetencia();
		ObjectMapper mapper = new ObjectMapper();  
		
	
		
				System.out.println(data.toString());
		 try {
			
			Map<String, Object> comps = mapper.readValue(new JSONObject (data.get("filtros").toString().replace("#", "sharp")).toString(), new TypeReference<Map<String, Object>>() {}); 
			
			System.out.println(comps.toString());
			
			List<String> competencias =  converterJsonArrayToList (comps.get("competencias").toString().replace("#", "sharp"));
			 
			System.out.println(competencias.toString());
			
			System.out.println("Tipo: " + comps.get("tipo").toString());
			
			List<Vaga> vagas = bd.filtrarVagas(data, competencias, comps.get("tipo").toString());
			
			//System.out.println(vagas.toString());
			
			respostarFiltrarVagas(vagas);
			
			
		} catch (Exception e) {
			System.err.println(e);
			respostaExcecao(e, data.get("operacao").toString(),"422");
		}
		
		
		
	}
	
	public void respostarFiltrarVagas(List<Vaga> vagas) {
		JSONObject obj = new JSONObject();
		JSONArray jarray = new JSONArray();
		System.out.println("chegou aqui");
		String myString = null;

		
		try {
			 PrintStream saida  = new PrintStream (ss.getOutputStream());
			
			for(Vaga vag : vagas) {
				JSONObject objTemp = new JSONObject();
				JSONArray jsonArrayComp = new JSONArray();
				
				List<String> lista = new ArrayList<>();
				for(String str : vag.getCompetencias()) {
					lista.add(str.replace("#", "sharp"));
				}
				
				
				objTemp.put("idVaga",vag.getIdVaga());
				objTemp.put("nome", vag.getNome());
				objTemp.put("faixaSalarial", vag.getFaixaSalarial());
				objTemp.put("descricao", vag.getDescricao());
				objTemp.put("estado", vag.getEstado());
				objTemp.put("competencias", lista);
				objTemp.put("email", vag.getEmail());
				
			
				jarray.put(objTemp);
			}
			
			
			
			obj.put("operacao", "filtrarVagas").put("vagas", jarray).put("status", 201);
			
			
			System.out.println("Saida: ["+obj.toString()+"]");
			 
			saida.println(obj.toString().replace("sharp", "#"));
			
			
			
		} catch (JSONException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println(e);
		}
	}
	
	
	public void cadastrarCandidatoCompetencia(Map<String, Object> dataCandidato) {
		
		DaoCompetencia bd = new DaoCompetencia();
			
		try {
		
			
			
			
			
			JSONArray competencia = new JSONArray(dataCandidato.get("competenciaExperiencia").toString().replace("#","sharp"));
			
			bd.cadastrarExperienciaCandidato(dataCandidato.get("email").toString(), competencia);
			
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
		 respostaServidor(data.get("operacao").toString());
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
	
	public void apagarCompetenciaExperiencia(Map<String, Object> data) {
	
		
		DaoCompetencia bd = new DaoCompetencia();
		
		//int periodo =  Integer.parseInt(dataCompetencia.get("experiencia").toString());
				
		
		try {
			System.out.println("teste 1 ");
			JSONArray jsonArr = new JSONArray(data.get("competenciaExperiencia").toString());
			
			 for (int i = 0; i < jsonArr.length(); i++)
		        {
		            JSONObject jsonObj = jsonArr.getJSONObject(i);
		            int exp = Integer.parseInt(jsonObj.get("experiencia").toString());
		        	bd.apagarExperienciaCandidato(data.get("email").toString(), jsonObj.get("competencia").toString(),exp);
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
			 
			String myString	= new JSONObject().put("operacao",  "visualizarCompetenciaExperiencia").put("competenciaExperiencia", rsToJson(rs)).put("status", 201).toString();
			
			System.out.println("Saida: ["+myString+"]"); 
			saida.println(myString.replace("sharp","#"));
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
		            row.put(cn, rs.getObject(cn).toString().replace("#","sharp"));
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
		
		 if(operacao.equals("cadastrarCompetenciaExperiencia") || operacao.equals("atualizarCompetenciaExperiencia")) {
			 
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
