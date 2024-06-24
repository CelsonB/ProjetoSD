package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import entities.Candidato;
import entities.Competencia;
import entities.Empresa;
import entities.Vaga;

public class VagasService {
	
	private Socket clienteSocket;
	private static List<Vaga> vagas = new ArrayList<>();
	private Empresa sessao; 

	public VagasService(Socket clienteSocket,Empresa sessao) {
		this.sessao = sessao;
		this.clienteSocket = clienteSocket ;
	}
	
	
	
	
	
	
	public List<Candidato> filtrarCandidatos(List<Competencia> competencias, String tipo) {
		

		
		
		JSONObject obj = new JSONObject();
		JSONArray jarray = new JSONArray();
		
		
		try {
			PrintStream saida  = new PrintStream (clienteSocket.getOutputStream());
			
			
			JSONObject newObj = new JSONObject();
			newObj.put("competenciasExperiencias", competencias).put("tipo", tipo.toUpperCase());
			
			obj.put("operacao", "filtrarCandidatos").put("filtros", newObj).put("token", sessao.getToken());
			
			System.out.println("Saida: [" + obj.toString() + "]");
			
			saida.println(obj.toString());
			
		
			return receberRespostaServidorFiltrar();
			
		} catch (JSONException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private List<Candidato> receberRespostaServidorFiltrar() {
		List<Candidato> candidatos = new ArrayList<>();
		try {
				
				
				InputStreamReader input = new InputStreamReader(clienteSocket.getInputStream());
				BufferedReader reader = new BufferedReader(input);
				
				ObjectMapper mapper = new ObjectMapper(); 
				Map<String, Object> data = mapper.readValue(reader.readLine(), new TypeReference<Map<String, Object>>() {});
				System.out.println("Entrada => ["+data.toString()+"]");
				
						//String competencias = new JSONObject().put("competenciaExperiencia",data.get("competenciaExperiencia").toString()).toString();
						//System.out.println("<"+data.get("competenciaExperiencia").toString()+">");
						
						
						JSONArray jsonArr = new JSONArray(data.get("candidatos").toString());

				        for (int i = 0; i < jsonArr.length(); i++)
				        {
				        	System.out.println("----------------------------------------------------------------------------------------");
				            JSONObject jsonObj = jsonArr.getJSONObject(i);
				            
				            Candidato cand = new Candidato();
				            
				            cand.setEmail(jsonObj.getString("email"));
				            cand.setNome(jsonObj.getString("nome"));
				            List<Competencia> competencias = mapper.readValue(jsonObj.getString("competenciaExperiencia"), new TypeReference<List<Competencia>>() {});
				            cand.setListaCompetencia(competencias);
				            candidatos.add(cand);
				        }
						
				        return candidatos;
			}catch(Exception ex) {
				System.out.println(ex);
			}
	       return candidatos;
	       
		}
	
	public List<Vaga> listarVagas() throws IOException, JSONException {
		enviarJsonListarVagas();
		this.vagas = receberRespostaListarVagas();
		return this.vagas;
		
	}
	
	public boolean cadastrarVaga(Vaga vagaData,List<String> competencias) {
		JSONArray comp = new JSONArray();
		for(String vga: competencias) {
			JSONObject newObj = new JSONObject();
			
			comp.put(vga);
		}
		
		enviarJsonCadastroAtualizacao(vagaData, "cadastrarVaga", comp);
		
		if(receberRespostaServidor()) {
			return true;
		}else {
			return false;
		}
	}
	
	public Vaga visualizarVaga(int idVaga) {
		
		enviarJsonVisualizarVaga("visualizarVaga",idVaga);
		Vaga vaga = receberRespostaServidorVisualizar();
		if(vaga!=null) {
			return vaga;
		}
		else {
			return null;
		}
	}
	
	public boolean atualizarVaga(Vaga vagaData,List<String> competencias) {
		JSONArray comp = new JSONArray();
		for(String vga: competencias) {
			JSONObject newObj = new JSONObject();
			comp.put(vga);
		}
		
		enviarJsonCadastroAtualizacao(vagaData, "atualizarVaga", comp);
		
		return receberRespostaServidor();
		
	}
	
	public boolean deletarVaga(int idVaga) {
	
		enviarJsonVisualizarVaga("apagarVaga",idVaga);
		
		if(receberRespostaServidor()) {
			return true;
		}
		
		return false;
	}
	
	private void enviarJsonListarVagas() throws IOException, JSONException {

				PrintStream saida  = new PrintStream (this.clienteSocket.getOutputStream());
				String myString = new JSONObject()
						.put("operacao","listarVagas")
						.put("email", this.sessao.getEmail())
						.put("token", this.sessao.getToken())
						.toString();
				System.out.println("Saida: [" +myString + "]");
				
				saida.println(myString);
		}
	

	private Vaga receberRespostaServidorVisualizar() {
		Vaga vagaVisualizada = new Vaga();
		try {
			
			InputStreamReader input = new InputStreamReader(clienteSocket.getInputStream());
			BufferedReader reader = new BufferedReader(input);
			ObjectMapper mapper = new ObjectMapper(); 
			Map<String, Object> data = mapper.readValue(reader.readLine(), new TypeReference<Map<String, Object>>() {});
			System.out.println("Entrada: [" +data.toString()+ "]");
			String op = data.get("status").toString();
			
			if(op.equals("201") || op.equals("200")) {	
				
				if(data.get("operacao").toString().equals("cadastrarCompetenciaExperiencia") || data.get("operacao").toString().equals("atualizarCompetenciaExperiencia") ||data.get("operacao").toString().equals("visualizarVaga") ) {
			
					System.out.println("estado: "+ data.get("estado").toString());
					System.out.println("competencias: "+ data.get("competencias").toString());
					System.out.println("descricao: "+ data.get("descricao").toString());
					System.out.println("Faixa salarial: "+ data.get("faixaSalarial").toString());
					
					vagaVisualizada.setEstado(data.get("estado").toString());
					vagaVisualizada.setCompetencias(data.get("competencias").toString());
					vagaVisualizada.setDescricao(data.get("descricao").toString());
					vagaVisualizada.setFaixaSalarial(Float.parseFloat(data.get("faixaSalarial").toString()));
					vagaVisualizada.setCompetencias(data.get("competencias").toString());
					return vagaVisualizada;
				}
				
			}else {
				
				System.out.println(data.get("mensagem").toString());
				return null;
			}
			
			
		}catch(Exception ex) {
			
		}
			return null;
		}
	
	private void enviarJsonVisualizarVaga(String operacao, int op ) {
		
		try {
			PrintStream saida  = new PrintStream (clienteSocket.getOutputStream());
				String myString = new JSONObject()
				.put("operacao", operacao)
				.put("idVaga", String.valueOf(op))
				.put("email",sessao.getEmail())
				.put("token",sessao.getToken())
				.toString(); 
		
		System.out.println("Saida: [" +myString + "]");
		
		saida.println(myString);

		} catch (Exception e) {
			e.toString();
		}
		System.out.println("Opcao "+ op );
	}
	
	private void enviarJsonCadastroAtualizacao(Vaga dataVaga, String operacao, JSONArray comp) {
		try {
				PrintStream saida  = new PrintStream (clienteSocket.getOutputStream());
	
				
					String myString = new JSONObject()
					.put("operacao", operacao)//operacao cadastrarCompetenciaExperiencia
					.put("nome",dataVaga.getNome())
					.put("faixaSalarial", dataVaga.getFaixaSalarial())
					.put("descricao",dataVaga.getDescricao())
					.put("estado", dataVaga.getEstado())
					.put("email", this.sessao.getEmail())
					.put("competencias", comp)
					.put("token",this.sessao.getToken())
					.put("idVaga", String.valueOf(dataVaga.getIdVaga())  )
					.toString(); 
			
			System.out.println("Saida: [" +myString + "]");
			
			saida.println(myString);
			   
			} catch (Exception e) {
				e.toString();
			}
	}
	
	
	private List<Vaga> receberRespostaListarVagas() {
			
			
			try {
				InputStreamReader input = new InputStreamReader(clienteSocket.getInputStream());
			BufferedReader reader = new BufferedReader(input);
			
			ObjectMapper mapper = new ObjectMapper(); 
			Map<String, Object> data = mapper.readValue(reader.readLine(), new TypeReference<Map<String, Object>>() {});
			
			System.out.println("Entrada: [" +data.toString()+ "]");
			
			
				String competencias = new JSONObject().put("vagas",data.get("vagas").toString()).toString();
				JSONArray jsonArr = new JSONArray(data.get("vagas").toString());
				List<Vaga> vagas = new ArrayList<>();
				
				for (int i = 0; i < jsonArr.length(); i++)
			        {
					
					 	Vaga vagaTemp = new Vaga();
			            JSONObject jsonObj = jsonArr.getJSONObject(i);
			            vagaTemp.setIdVaga(  Integer.parseInt(jsonObj.get("idVaga").toString()));
			            vagaTemp.setNome( jsonObj.get("nome").toString());
			            vagas.add(vagaTemp);
			            
			        }
				 return vagas;
				 
				
			} catch (JSONException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
					
				}else 
				if(data.get("operacao").toString().equals("visualizarEmpresa")){
	
				}
				return true;
			}else {
				
				System.out.println(data.get("mensagem").toString());
				return false;
			}
			
			
		}catch(Exception ex) {
			
		}
		return false;
	}

}
