package servidor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
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
		InputStreamReader input = new InputStreamReader(ss.getInputStream());
		BufferedReader reader = new BufferedReader(input);
		
		PrintStream saida  = new PrintStream (ss.getOutputStream());
		
		ObjectMapper mapper = new ObjectMapper();  
	
		//Map<String, Object> userData = mapper.readValue(reader.readLine(), new TypeReference<Map<String, Object>>() {}); 
		
		
		
		
		
		
		
	String op = ""; 
      
       
		
	do {			
			
			Map<String, Object> userData = mapper.readValue(reader.readLine(), new TypeReference<Map<String, Object>>() {}); 
		
			System.out.print(userData.get("operacao").toString());
			
			op = userData.get("operacao").toString();
			
			if(op.equals("loginCandidato")) {
				candidatoServer.SolicitarLogin(userData);
			}else
			
			if(op.equals("cadastrarCandidato")) {	
				
				candidatoServer.SolicitarCadastro(userData);
			}else
			
			if(op.equals("visualizarCandidato")) {
				candidatoServer.SolicitarVisualizacao(userData.get("email").toString());
			}else
				
			if(op.equals("logout")){
				candidatoServer.logout(userData);
			}else
				
			if(op.equals("atualizarCandidato")) {
				candidatoServer.atualizarCadastro(userData);
			}
			else 
				
			if(op.equals("apagarCandidato")) {
				candidatoServer.apagarCandidato(userData.get("email").toString());
			}		
			else
				
			
			if(op.equals("cadastrarEmpresa"))
			{
						
			}else 
			if((op.equals("loginEmpresa")) {
					
			}
			else 
			if
			(op.equals("visualizarEmpresa"))
			{
						
			}else //atualizarEmpresa
			if(op.equals("atualizarEmpresa")) {
							
			}else//apagarEmpresa
				if(op.equals("apagarEmpresa")) {
					
				}
		}while(op != "");
		
		
		
		
		
		
		}catch(Exception ex) {
			System.err.println(ex);
		}
		
	}
	
	
	
	



}
