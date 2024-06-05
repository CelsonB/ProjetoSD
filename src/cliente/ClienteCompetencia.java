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
	
	private static String [] competenciasNome  = {"python" , "c#", "c++"};
	
	
	

	

	
	
	 public void cadastrarCandidatoCompetencia() 
	 {
	
		enviarJsonCompetenciaExperiencia(selecionarCompetencia(), "cadastrarCompetenciaExperiencia");
		receberRespostaServidor();
	}
	

	

	
	
	
	
	public void atualizarCompetencia() {
		try {
			
			if(super.sessao.getToken()==null) throw new SessaoInvalidaException("Voc� n�o est� logado");
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
			if(super.sessao.getToken()==null) throw new SessaoInvalidaException("Voc� n�o est� logado");
			PrintStream saida  = new PrintStream (super.clienteSocket.getOutputStream());
			JSONObject json = new JSONObject();
			json.put("type", "CONNECT");
			String myString = new JSONObject().put("operacao", "visualizarCompetenciaExperiencia").put("email",super.sessao.getEmail()).put("token",super.sessao.getToken().toString()).toString();
			
			saida.println(myString);
			receberRespostaServidorVisualizar();
			
			
			
		} catch (SessaoInvalidaException ex) {
			System.out.println(ex);
		} catch(Exception e) {
			
		}
		
		
	}
	
	
	public void apagarCompetencia() {
		try {
			
			if(super.sessao.getToken()==null) throw new SessaoInvalidaException("Voc� n�o est� logado");
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
			for(String comp :competenciasNome) {
				System.out.println(op + " : " + comp);
				op++;
			}
			System.out.println("0 : Sair da sele��o");
			op = leia.nextInt();
			if(op!=0) {
				String sr = competenciasNome[op-1] ; 
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
		
		JSONObject competencia = new JSONObject();
		JSONArray competencias = new JSONArray();
		for(String sr : compOp) {
			System.out.println("Qual sua experiencia com " + sr );
			int tempo = leia.nextInt();
			competencia.put("competencia", sr ).put("experiencia", tempo);
			competencias.put(competencia);
		}
		
	
	//	competencias.put(competencia);
			
				String myString = new JSONObject()
				.put("operacao", operacao)//operacao cadastrarCompetenciaExperiencia
				.put("email", super.sessao.getEmail())
				.put("competenciaExperiencia", competencias)
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
			System.out.println(data.toString());
			if(data.get("operacao").toString().equals("visualizarCompetenciaExperiencia") ) {//visualizarCompetenciaExperiencia
				
				
				
				
				 
				 
				if(data.get("status")==null) {
					

					String competencias = new JSONObject().put("CompetenciaExperiencia",data.get("CompetenciaExperiencia").toString()).toString();
					
					System.out.println("<"+data.get("CompetenciaExperiencia").toString()+">");
					
					
					JSONArray jsonArr = new JSONArray(data.get("CompetenciaExperiencia").toString());

			        for (int i = 0; i < jsonArr.length(); i++)
			        {
			            JSONObject jsonObj = jsonArr.getJSONObject(i);

			            System.out.println(jsonObj.get("competencia").toString());
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