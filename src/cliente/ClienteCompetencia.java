package cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.UUID;

import javax.swing.text.html.HTMLDocument.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import entities.Candidato;
import entities.Competencia;
import exceptions.SessaoInvalidaException;

public class ClienteCompetencia extends Cliente {

	private Socket clienteSocket = super.clienteSocket ;
	private static UUID token = null;
	private static Scanner leia  = new Scanner(System.in);	
	
	//private static String [] competenciasNome  = {"python" , "c#", "c++"};
	
	
	

	
	public void filtrarVagas() {

		
		
		JSONObject obj = new JSONObject();
		JSONArray jarray = new JSONArray();
		
		
		try {
			PrintStream saida  = new PrintStream (super.clienteSocket.getOutputStream());
			
			ArrayList<String> competencias =  selecionarCompetencia();
			
			for(String str : competencias) {
				
				jarray.put(str);
			}
			
			JSONObject newObj = new JSONObject();
			newObj.put("competencias", jarray).put("tipo", selecionarTipo().toUpperCase());
			obj.put("operacao", "filtrarVagas").put("filtros", newObj).put("token", super.sessao.getToken());
			
			System.out.println("Saida: [" + obj.toString() + "]");
			
			saida.println(obj.toString());
			
		
			receberRespostaServidorFiltrar();
			
		} catch (JSONException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void receberRespostaServidorFiltrar() {
	try {
			
			InputStreamReader input = new InputStreamReader(clienteSocket.getInputStream());
			BufferedReader reader = new BufferedReader(input);
			
			ObjectMapper mapper = new ObjectMapper(); 
			Map<String, Object> data = mapper.readValue(reader.readLine(), new TypeReference<Map<String, Object>>() {});
			System.out.println("Entrada => ["+data.toString()+"]");
			
					//String competencias = new JSONObject().put("competenciaExperiencia",data.get("competenciaExperiencia").toString()).toString();
					//System.out.println("<"+data.get("competenciaExperiencia").toString()+">");
					
					
					JSONArray jsonArr = new JSONArray(data.get("vagas").toString());

			        for (int i = 0; i < jsonArr.length(); i++)
			        {
			        	System.out.println("----------------------------------------------------------------------------------------");
			            JSONObject jsonObj = jsonArr.getJSONObject(i);

			            System.out.println("faixaSalarial: " + jsonObj.get("faixaSalarial").toString() + "|| descricao: " + jsonObj.get("descricao").toString());
			            System.out.println("estado: " + jsonObj.get("estado").toString() + " || idVaga: " + jsonObj.get("idVaga").toString());
			            System.out.println("competencias: " + jsonObj.get("competencias").toString());
			        }
					
				
		
				
					
				
			
		}catch(Exception ex) {
			System.out.println(ex);
		}
	}
	
	private String selecionarTipo() {
		System.out.println("1 - and\n2 - or\n0 - Sair");
		int op = 0;
			do {
				op = leia.nextInt();
				switch(op) {
				case 1: return "and";
				case 2: return "or";
				case 0:
				}
			
			}while(op!=0);
			
		return "or";
	}
	
	public void cadastrarCandidatoCompetencia() 
	 {
	
		enviarJsonCompetenciaExperiencia(selecionarCompetencia(), "cadastrarCompetenciaExperiencia");
		receberRespostaServidor();
	}
	

	

	
	
	
	
	public void atualizarCompetencia() {
		try {
			
			if(super.sessao.getToken()==null) throw new SessaoInvalidaException("Você não está logado");
			enviarJsonCompetenciaExperiencia(selecionarCompetencia(), "atualizarCompetenciaExperiencia");
					
			receberRespostaServidor();
			
			
			
		} catch (SessaoInvalidaException ex) {
			System.out.println(ex);
		} catch(Exception e) {
			
		}
	}
	
	public void visualizarCompetencia() {
		//{"operacao": "visualizarCompetenciaExperiencia","email":"xx@xxx.xxx","token": "UUID"}
		
		
		try {
			if(super.sessao.getToken()==null) throw new SessaoInvalidaException("Você não está logado");
			PrintStream saida  = new PrintStream (super.clienteSocket.getOutputStream());
			JSONObject json = new JSONObject();
			json.put("type", "CONNECT");
			String myString = new JSONObject()
					.put("operacao", "visualizarCompetenciaExperiencia")
					.put("email",super.sessao.getEmail())
					.put("token",super.sessao.getToken().toString())
					.toString();
			
			saida.println(myString);
			receberRespostaServidorVisualizar();
			
			
			
		} catch (SessaoInvalidaException ex) {
			System.out.println(ex);
		} catch(Exception e) {
			
		}
		
		
	}
	
	
	public void apagarCompetencia() {
		try {
			
			
			if(super.sessao.getToken()==null) throw new SessaoInvalidaException("Você não está logado");
			enviarJsonCompetenciaExperiencia(selecionarCompetencia(), "apagarCompetenciaExperiencia");					
			receberRespostaServidor();
			
			
			
		} catch (SessaoInvalidaException ex) {
			System.out.println(ex);
		} catch(Exception e) {
			
		}
		
	}
	
	
	private ArrayList<String> selecionarCompetencia() {
		System.out.println("por favor selecione a competencia que vc deseja: ");
		ArrayList<String> comps = new ArrayList<String>();
		int op = 0; 
		do { op = 1;
			for(String comp :super.competenciasNome) {
				System.out.print(op + ": " + comp+ "  ");
				if(op%5==0)System.out.println();
				op++;
			}
			System.out.println("0 : Sair da seleção");
			op = leia.nextInt();
			if(op!=0) {
				String sr = super.competenciasNome[op-1] ; 
				comps.add(sr); 
			}
			
		}while(op!=0);
		
		return comps;
	}
	
	private void enviarJsonCompetenciaExperiencia(ArrayList<String> compOp,String operacao) {
		 
		try {
		PrintStream saida  = new PrintStream (super.clienteSocket.getOutputStream());
		JSONObject json = new JSONObject();
		json.put("type", "CONNECT");
		
		
		JSONArray competencias = new JSONArray();
		for(String sr : compOp) {
			System.out.println("Qual sua experiencia com " + sr );
			int tempo = leia.nextInt();
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("competencia", sr ).put("experiencia", tempo);
			
			competencias.put(jsonObj);
		}
		
			
				String myString = new JSONObject()
				.put("operacao", operacao)//operacao cadastrarCompetenciaExperiencia
				.put("email", super.sessao.getEmail())
				.put("competenciaExperiencia", competencias)
				.put("token", super.sessao.getToken())
				.toString(); 
		
		System.out.println("Saida: [" +myString + "]");
		
		saida.println(myString);
		   
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.toString();
		}
	}
	
	
	private void receberRespostaServidorVisualizar() {
		try {
			
			InputStreamReader input = new InputStreamReader(clienteSocket.getInputStream());
			BufferedReader reader = new BufferedReader(input);
			
			ObjectMapper mapper = new ObjectMapper(); 
			Map<String, Object> data = mapper.readValue(reader.readLine(), new TypeReference<Map<String, Object>>() {});

			System.out.println("Entrada => ["+data.toString()+"]");
			
			
			if(data.get("operacao").toString().equals("visualizarCompetenciaExperiencia") ) {//visualizarCompetenciaExperiencia
				
				if(data.get("status").toString().equals("201") || data.get("status").toString().equals("200")) {
					

					String competencias = new JSONObject().put("competenciaExperiencia",data.get("competenciaExperiencia").toString()).toString();
					
					System.out.println("<"+data.get("competenciaExperiencia").toString()+">");
					
					if(data.get("competenciaExperiencia").toString().equals("<[]>")) {
						
					}else {
						JSONArray jsonArr = new JSONArray(data.get("competenciaExperiencia").toString());

				        for (int i = 0; i < jsonArr.length(); i++)
				        {
				            JSONObject jsonObj = jsonArr.getJSONObject(i);

				            System.out.println("Competencia: " + jsonObj.get("competencia").toString() + " Experiencia: " + jsonObj.get("experiencia").toString());
				        }
					}
				
					
				
				}else{
				
				
					}
				}else {
					
				}
			
		}catch(Exception ex) {
			System.out.println(ex);
		}
	}
	private void receberRespostaServidor() {
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
				
			}else {
				
				System.out.println(data.get("mensagem").toString());
				
			}
			
			
		}catch(Exception ex) {
			
		}
		
	}
	
	private static Map<String, String> converterStringArrayToMap (String data) {
		Map<String, String> competencia = new HashMap<String, String>();  
		List<Map<String, String>> myList = new ArrayList();
	
	  StringTokenizer tokenizer = new StringTokenizer(data.replace("{", "").replace("}", "").replace("[", "").replace("]", ""), ", ");		

	  while (tokenizer.hasMoreTokens()) {
	        String token = tokenizer.nextToken();
	        String[] keyValue = token.split("=");
	        
	        competencia.put(keyValue[0], keyValue[1]);
	        System.out.println(competencia.toString());
	        myList.add(competencia);
	    }
	  
//	  ListIterator<Map<String, String>> listIterator = myList.listIterator();
//	  while(listIterator.hasNext()) {
//		  System.out.println(listIterator.next());
//	  }
	  return competencia;
	}
	
}
