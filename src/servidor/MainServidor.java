package servidor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.UUID;

import org.json.JSONArray;
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
		
		Scanner leia = new Scanner(System.in);
		System.out.println("Digite a porta");
		int porta = leia.nextInt();
		
		servidorSocket = new ServerSocket(porta,5);
		ss=servidorSocket.accept(); 	 

		ServidorCandidato candidatoServer = new ServidorCandidato(ss);
		ServidorEmpresa empresaServer = new ServidorEmpresa( ss);
		ServidorCompetencia competenciaSever = new ServidorCompetencia(ss);
		ServidorVaga vagaServer = new ServidorVaga(ss);
		
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
			
			op = userData.get("operacao").toString();
			
			if(op.equals("cadastrarCompetenciaExperiencia") 
					|| op.equals("visualizarCompetenciaExperiencia") 
					|| op.equals("apagarCompetenciaExperiencia") 
					|| op.equals("atualizarCompetenciaExperiencia")
					|| op.equals("filtrarVagas")  
					) {
				
					
				
				 
				
				
				switch(op) {
					case "cadastrarCompetenciaExperiencia":
						competenciaSever.cadastrarCandidatoCompetencia(userData);
					break;
					case "visualizarCompetenciaExperiencia":
						competenciaSever.visualizarCompetenciaExperiencia(userData);
					break;
					case "apagarCompetenciaExperiencia":
				
						competenciaSever.apagarCompetenciaExperiencia(userData);
						break;
					case "atualizarCompetenciaExperiencia":
						competenciaSever.atualizarCompetencia(userData);
						break;
					case "filtrarVagas":
						competenciaSever.filtrarVagas(userData);
						break;
						
				}
				}
				
			else if(op.equals("apagarVaga") || op.equals("cadastrarVaga") || op.equals("visualizarVaga") || op.equals("atualizarVaga" )|| op.equals("listarVagas") ) {
				switch(op) {
				case "cadastrarVaga":
					vagaServer.cadastrarVaga(userData);
				break;
				case "visualizarVaga":
					vagaServer.visualizarVaga(userData);
				break;
				case "apagarVaga":
					vagaServer.apagarVaga(userData);
					break;
				case "atualizarVaga":
					vagaServer.atualizarVaga(userData);
					break;
				case "listarVagas":
					vagaServer.listarVagas("listarVagas", userData);
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
	

	}
