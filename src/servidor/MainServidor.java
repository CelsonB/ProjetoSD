package servidor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;

import org.json.JSONObject;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import dao.EmpresaDao;
import entities.Empresa;



public class MainServidor {

	private static UUID token = null;
	public static ServerSocket servidorSocket;
	public static Socket ss;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		try {
		
		System.out.println("programa 1: servidor");
		
		servidorSocket = new ServerSocket(22222,5);
		ss=servidorSocket.accept(); 	 

		ServidorCandidato candidatoServer = new ServidorCandidato(servidorSocket, ss);
		ServidorEmpresa empresaServer = new ServidorEmpresa(servidorSocket, ss);
		ServidorCompetencia competenciaSever = new ServidorCompetencia();
		ServidorVaga vagaServer = new ServidorVaga();
		InputStreamReader input = new InputStreamReader(ss.getInputStream());
		BufferedReader reader = new BufferedReader(input);
		
		PrintStream saida  = new PrintStream (ss.getOutputStream());
		
		ObjectMapper mapper = new ObjectMapper();  
	
//		Map<String, Object> data = mapper.readValue(reader.readLine(), new TypeReference<Map<String, Object>>() {}); 
//		
//		System.out.println(data.toString());
		
		
		
		
		
		
		
		
	String op = ""; 
      
	
		
	do {			
			
			Map<String, Object> userData = mapper.readValue(reader.readLine(), new TypeReference<Map<String, Object>>() {}); 
			
			System.out.println("Entrada: ["+userData.toString()+"]");
			
			//competenciaSever.visualizarCompetenciaExperiencia(userData);
			op = userData.get("operacao").toString();
			
			if(op.equals("cadastrarCompetenciaExperiencia") || op.equals("visualizarCompetenciaExperiencia") || op.equals("apagarCompetenciaExperiencia") || op.equals("atualizarCompetenciaExperiencia")  ) {
				
					
				
				switch(op) {
					case "cadastrarCompetenciaExperiencia":
						Map <String, String> competencia = converterStringArrayToMap(userData.get("competenciaExperiencia").toString());
						competenciaSever.cadastrarCandidatoCompetencia(userData, competencia);
					
					break;
					case "visualizarCompetenciaExperiencia":
						competenciaSever.visualizarCompetenciaExperiencia(userData);
					break;
					case "apagarCompetenciaExperiencia":
						Map <String, String> competencia2 = converterStringArrayToMap(userData.get("competenciaExperiencia").toString());
						competenciaSever.apagarCompetenciaExperiencia(userData, competencia2);
						break;
					case "atualizarCompetenciaExperiencia":
						competenciaSever.atualizarCompetencia(userData);
						break;
						
				}
				}
				
			else if(op.equals("apagarVaga") || op.equals("cadastrarVaga") || op.equals("visualizarVaga") || op.equals("atualizarVaga")) {
				switch(op) {
				case "apagarVaga":
				
					vagaServer.cadastrarVaga(userData);
				
				break;
				case "cadastrarVaga":
					vagaServer.visualizarVaga(userData);
				break;
				case "visualizarVaga":
					vagaServer.apagarVaga(userData);
					break;
				case "atualizarVaga":
					vagaServer.atualizarVaga(userData);
					break;
					
			}
				
				
			}else if(op.equals("loginCandidato")) {
				candidatoServer.SolicitarLogin(userData);
			}else if(op.equals("cadastrarCandidato")) {	
				
				candidatoServer.SolicitarCadastro(userData);
			}else if(op.equals("visualizarCandidato")) {
				candidatoServer.SolicitarVisualizacao(userData.get("email").toString());
			}else if(op.equals("logout")){
				
				if(empresaServer.getToken()!=null) {
				
					candidatoServer.setToken(null);
					empresaServer.logout();
					
			}else if(candidatoServer.getToken()!=null){
					
					empresaServer.setToken(null);
					candidatoServer.logout();
					
			}
			
				
				
			}else if(op.equals("atualizarCandidato")) {
				candidatoServer.atualizarCadastro(userData);
			}
			else  if(op.equals("apagarCandidato")) {
				candidatoServer.apagarCandidato(userData.get("email").toString());
			}		
			else if(op.equals("cadastrarEmpresa"))
			{
				empresaServer.cadastrarEmpresa(userData);
			}else  if(op.equals("loginEmpresa")) {
				empresaServer.realizarLogin(userData);
			}
			else if
			(op.equals("visualizarEmpresa"))
			{
				empresaServer.visualizarEmpresa(userData);	
			}else if(op.equals("atualizarEmpresa")) {
				empresaServer.atualizarEmpresa(userData);		
			}else if(op.equals("apagarEmpresa")) {
				empresaServer.apagarEmpresa(userData);
			}
		}while(op != "");
		
		
		
		
		
		
		}catch(Exception ex) {
			System.err.println(ex);
		}
		

	
	
	
	



}
	
	private static Map<String, String> converterStringArrayToMap (String data) {
		Map<String, String> competencia = new HashMap<String, String>();  
		System.out.println(data);
	
	  StringTokenizer tokenizer = new StringTokenizer(data.replace("{", "").replace("}", "").replace("[", "").replace("]",""), ", ");		
	  while (tokenizer.hasMoreTokens()) {
	        String token = tokenizer.nextToken();
	        String[] keyValue = token.split("=");
	        competencia.put(keyValue[0], keyValue[1]);
	    }
	  return competencia;
	}
	}
