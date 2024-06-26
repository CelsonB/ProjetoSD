package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import entities.Candidato;
import entities.Competencia;
import entities.Vaga;
import exceptions.SessaoInvalidaException;

public class CompetenciaService {

	
	private Candidato sessao; 
	private Socket clienteSocket;
	public CompetenciaService (Candidato sessao, Socket clienteSocket) {
		
		this.sessao = sessao;
		this.clienteSocket = clienteSocket;
		
		
	}
	
	public List<Vaga> filtrarVagas(List<String> competencias,String tipo) {
		
		JSONObject obj = new JSONObject();
		JSONArray jarray = new JSONArray();
		try {
			PrintStream saida  = new PrintStream (clienteSocket.getOutputStream());
			
			for(String str : competencias) {
				jarray.put(str);
			}
			
			JSONObject newObj = new JSONObject();
			newObj.put("competencias", jarray).put("tipo", tipo.toUpperCase());
			obj.put("operacao", "filtrarVagas").put("filtros", newObj).put("token", sessao.getToken());
			
			System.out.println("Saida: [" + obj.toString() + "]");
			saida.println(obj.toString());
			return receberRespostaServidorFiltrar();
			
		} catch (JSONException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return null;
	}

	
	private List<Vaga> receberRespostaServidorFiltrar() {
		List<Vaga> listaVaga = new ArrayList<>();
	try {
		
		Vaga vagaTemp = new Vaga();
			InputStreamReader input = new InputStreamReader(clienteSocket.getInputStream());
			BufferedReader reader = new BufferedReader(input);
			
			ObjectMapper mapper = new ObjectMapper(); 
			Map<String, Object> data = mapper.readValue(reader.readLine(), new TypeReference<Map<String, Object>>() {});
			System.out.println("Entrada => ["+data.toString()+"]");
								
					JSONArray jsonArr = new JSONArray(data.get("vagas").toString());

			        for (int i = 0; i < jsonArr.length(); i++)
			        {
			        	System.out.println("----------------------------------------------------------------------------------------");
			            JSONObject jsonObj = jsonArr.getJSONObject(i);

			            
			            System.out.println("faixaSalarial: " +  jsonObj.get("faixaSalarial").toString()+ "|| descricao: " );
			            System.out.println("estado: " + jsonObj.get("estado").toString() + " || idVaga: " + jsonObj.get("idVaga").toString());
			            System.out.println("competencias: " + jsonObj.get("competencias").toString());
			            
			            vagaTemp.setNome(jsonObj.get("nome").toString());
			            vagaTemp.setFaixaSalarial(Float.parseFloat(jsonObj.get("faixaSalarial").toString())  );
			            vagaTemp.setDescricao(jsonObj.get("descricao").toString());
			            vagaTemp.setIdVaga(Integer.parseInt(jsonObj.get("idVaga").toString()));
			            vagaTemp.setCompetencias(jsonObj.get("competencias").toString());
			            listaVaga.add(vagaTemp);
			        }
			        return listaVaga;
			
			
			}catch(Exception ex) {
				System.out.println(ex);
			}
		return null;
	}
	public List<Competencia> visualizarCompetencias() throws IOException, JSONException{
		 enviarJsonVisualizarCompetencia();

		    List<Competencia> listaCompetencia = new ArrayList<>();
		  
		    try {
		        InputStreamReader input = new InputStreamReader(clienteSocket.getInputStream());
		        BufferedReader reader = new BufferedReader(input);
		        String linhaResposta = reader.readLine();

		        if (linhaResposta == null) {
		            System.out.println("Nenhuma resposta recebida do servidor.");
		            return listaCompetencia;
		        }

		        System.out.println("Resposta recebida do servidor: " + linhaResposta);

		        ObjectMapper mapper = new ObjectMapper();
		        Map<String, Object> data = mapper.readValue(linhaResposta, new TypeReference<Map<String, Object>>() {});
		        System.out.println("Entrada => [" + data.toString() + "]");

		        String status = data.get("status").toString();
		        if (status.equals("201") || status.equals("200")) {
		            JSONArray jsonArr = new JSONArray(data.get("competenciaExperiencia").toString());

		            for (int i = 0; i < jsonArr.length(); i++) {
		                JSONObject jsonObj = jsonArr.getJSONObject(i);
		                Competencia comp = new Competencia(
		                    Integer.parseInt(jsonObj.get("experiencia").toString()),
		                    jsonObj.get("competencia").toString()
		                );
		                System.out.println(jsonObj.get("competencia").toString() + " " + Integer.parseInt(jsonObj.get("experiencia").toString()));
		                listaCompetencia.add(comp);
		            }
		        } else {
		            System.out.println("Erro no servidor. Status: " + status);
		        }
		    } catch (IOException e) {
		        System.err.println("Erro de E/S: " + e.getMessage());
		        e.printStackTrace();
		    } catch (JSONException e) {
		        System.err.println("Erro de JSON: " + e.getMessage());
		        e.printStackTrace();
		    } catch (Exception e) {
		        System.err.println("Erro inesperado: " + e.getMessage());
		        e.printStackTrace();
		    }

		    return listaCompetencia;
	}
	
	public boolean apagarCompetencia(List<Competencia> competencias) {
		try {
			
			
		
			
			JSONArray jsonArray = new JSONArray();
			
			for(Competencia comp : competencias) {
				JSONObject objJson = new JSONObject();
				objJson.put("competencia", comp.getNomeCompetencia()).put("experiencia",comp.getExperiencia());
				jsonArray.put(objJson);
		
			}
			
			enviarJsonCompetenciaExperiencia(jsonArray, "apagarCompetenciaExperiencia");					
			return receberRespostaServidor();
			
			
			
		} catch(Exception e) {
			
		}
		return false;
	}
	
	public boolean cadastrarCandidatoCompetencia(List<Competencia> competencias) throws JSONException 
	 {
		
		JSONArray jsonArray = new JSONArray();
		
		for(Competencia comp : competencias) {
			JSONObject objJson = new JSONObject();
			objJson.put("competencia", comp.getNomeCompetencia()).put("experiencia",comp.getExperiencia());
			jsonArray.put(objJson);
	
		}
		
		
		
		enviarJsonCompetenciaExperiencia(jsonArray, "cadastrarCompetenciaExperiencia");
		return receberRespostaServidor();
	}
	

	
	private void enviarJsonVisualizarCompetencia() {
		try {
			
			PrintStream saida  = new PrintStream (clienteSocket.getOutputStream());
			JSONObject json = new JSONObject();
			json.put("type", "CONNECT");
			String myString = new JSONObject()
					.put("operacao", "visualizarCompetenciaExperiencia")
					.put("email",sessao.getEmail())
					.put("token",sessao.getToken().toString())
					.toString();
			
			saida.println(myString);
		
			
			
			
		}catch(Exception e) {
			
		}
	}

	
	private void enviarJsonCompetenciaExperiencia(JSONArray competencias,String operacao) {
		 
		try {
		PrintStream saida  = new PrintStream (this.clienteSocket.getOutputStream());
		JSONObject json = new JSONObject();
		json.put("type", "CONNECT");

			
				String myString = new JSONObject()
				.put("operacao", operacao)//operacao cadastrarCompetenciaExperiencia
				.put("email", sessao.getEmail())
				.put("competenciaExperiencia", competencias)
				.put("token", sessao.getToken())
				.toString(); 
		
		System.out.println("Saida: [" +myString + "]");
		
		saida.println(myString);
		   
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.toString();
		}
	}
	
	 private List<Competencia> receberRespostaServidorVisualizar() throws IOException, JSONException {
	        List<Competencia> listaCompetencia = new ArrayList<>();
	      
	            InputStreamReader input = new InputStreamReader(clienteSocket.getInputStream());
	            BufferedReader reader = new BufferedReader(input);
	            String linhaResposta = reader.readLine();
	            System.out.println("Resposta recebida do servidor: " + linhaResposta);

	            ObjectMapper mapper = new ObjectMapper();
	            Map<String, Object> data = mapper.readValue(linhaResposta, new TypeReference<Map<String, Object>>() {});
	            System.out.println("Entrada => [" + data.toString() + "]");

	       
	                if (data.get("status").toString().equals("201") || data.get("status").toString().equals("200")) {
	                    JSONArray jsonArr = new JSONArray(data.get("competenciaExperiencia").toString());
	                    
	                    
	                    for (int i = 0; i < jsonArr.length(); i++) {
	                        JSONObject jsonObj = jsonArr.getJSONObject(i);
	                        Competencia comp = new Competencia(
	                            Integer.parseInt(jsonObj.get("experiencia").toString()),
	                            jsonObj.get("competencia").toString()
	                        );
	                        System.out.println(jsonObj.get("competencia").toString()+"   "+Integer.parseInt(jsonObj.get("experiencia").toString()));
	                        listaCompetencia.add(comp);
	                 
	                    }
	                    
	                    
	                    return listaCompetencia;
	                    
	                }
	            
	     
	        return null;
	    }

	
	
	private boolean receberRespostaServidor() {
		try {
			InputStreamReader input = new InputStreamReader(clienteSocket.getInputStream());
			BufferedReader reader = new BufferedReader(input);
			
			ObjectMapper mapper = new ObjectMapper(); 
			Map<String, Object> data = mapper.readValue(reader.readLine(), new TypeReference<Map<String, Object>>() {});
			
			System.out.println("Entrada: [" +data.toString()+ "]");
			
			String op = data.get("status").toString();
			
			if(op.equals("201") || op.equals("200")) {
				
				
				if(data.get("operacao").toString().equals("cadastrarCompetenciaExperiencia") || data.get("operacao").toString().equals("atualizarCompetenciaExperiencia") ) {
				System.out.println(data.get("mensagem".toString()));
					
				return true;
				}
				
			}else {
				
				System.out.println(data.get("mensagem").toString());
				return false;
			}
			
			
		}catch(Exception ex) {
			
		}
		return false;
	}
	
	
}
