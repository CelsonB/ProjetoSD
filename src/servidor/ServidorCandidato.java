package servidor;


import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.net.ServerSocket;
import java.net.*;
import java.io.*;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

import dao.*;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper; 
import com.fasterxml.jackson.core.type.TypeReference; 

public class ServidorCandidato {
	private static UUID token = null;
	public static ServerSocket servidorSocket;
	public static Socket ss;
	
	public ServidorCandidato(ServerSocket servidorSocket2, Socket ss2) {
		this.servidorSocket = servidorSocket2;
		this.ss = ss2;
	}

	public void Servidor(ServerSocket servidorSocket,  Socket ss) {
		this.servidorSocket = servidorSocket;
		this.ss = ss; 
		
	}
//	public static void chamarServidor() throws IOException {
//		
//		
//		System.out.println("programa 1: servidor");
//		
//		servidorSocket = new ServerSocket(22222,5);
//		ss=servidorSocket.accept(); 	 
//
//		InputStreamReader input = new InputStreamReader(ss.getInputStream());
//		BufferedReader reader = new BufferedReader(input);
//		
//		PrintStream saida  = new PrintStream (ss.getOutputStream());
//		
//
//
//		
//		
//		
//		
//
//	String op = ""; 
//       
//        
//		ObjectMapper mapper = new ObjectMapper();  
//		do {			
//			
//
//			Map<String, Object> userData = mapper.readValue(reader.readLine(), new TypeReference<Map<String, Object>>() {}); 
//			
//			System.out.print(userData.get("operacao").toString());
//			
//			op = userData.get("operacao").toString();
//			
//			if(op.equals("loginCandidato")) {
//				SolicitarLogin(userData);
//			}else
//			
//			if(op.equals("cadastrarCandidato")) {	
//				System.out.println("Entrou em realizar cadastro 1 ");
//				SolicitarCadastro(userData);
//			}else
//			
//			if(op.equals("visualizarCandidato")) {
//				SolicitarVisualizacao(userData.get("email").toString());
//			}else
//				
//			if(op.equals("logout")){
//				logout(userData);
//			}else
//				
//			if(op.equals("atualizarCandidato")) {
//				atualizarCadastro(userData);
//			}
//			else 
//				
//			if(op.equals("apagarCandidato")) {
//				apagarCandidato(userData.get("email").toString());
//			}
//		}while(op != "");
//		
//	
//				
//	}
	
	public static void SolicitarCadastro(Map<String, Object> userData) {
		String myString = null;
		
		System.out.println("Entrou em realizar cadastro 2 ");
		System.out.println(userData.toString());
		try {
			
			try 
			{				
			JSONObject json = new JSONObject();
			json.put("type", "CONNECT");
			
			CandidatoDao bd = new CandidatoDao();
			bd.Conectar();	
			
			String email = userData.get("email").toString();
			
				if(!email.contains("@")) 
				{
				throw new EmailInvalidoException("email invalido");
				}
				else if(userData.get("senha").toString().length()>8) {
				throw new SenhaInvalidaException("senha invalida");	
				}
				else 
				{
				bd.Cadastrar(userData.get("email").toString(), userData.get("nome").toString(), userData.get("senha").toString());	
				}
			}
			catch(SQLIntegrityConstraintViolationException e)
			{
				
					PrintStream saida  = new PrintStream (ss.getOutputStream());
					myString = new JSONObject().put("operacao", "cadastrarCandidato").put("status","422").put("mensagem", "email ja cadastrado").toString(); 
					saida.println(myString);

			}
			catch(EmailInvalidoException ex) 
			{
					PrintStream saida  = new PrintStream (ss.getOutputStream());
					myString = new JSONObject().put("operacao", "cadastrarCandidato").put("status","422").put("mensagem", "Email ja cadastrado").toString(); 
					saida.println(myString);
			}
			catch(SenhaInvalidaException ex)
			{
					PrintStream saida  = new PrintStream (ss.getOutputStream());
					myString = new JSONObject().put("operacao", "cadastrarCandidato").put("status","422").put("mensagem", "Senha maior que 8 digitos").toString(); 
					saida.println(myString);
			}
			
				if(myString == null) {
					
					
					token = UUID.randomUUID();
					 
					PrintStream saida  = new PrintStream (ss.getOutputStream());
					myString = new JSONObject().put("operacao", "cadastrarCandidato").put("status","201").put("token", token.toString()).toString(); 
					saida.println(myString);
				
				}
			

		}catch(Exception ex) {
				System.err.println(ex);
			}
			
		
	}
	
	public static void atualizarCadastro(Map<String, Object> userData) {
	String myString = null;
		
		System.out.println("Entrou em realizar cadastro 2 ");
		System.out.println(userData.toString());
		try {
			
			try 
			{				
			JSONObject json = new JSONObject();
			json.put("type", "CONNECT");
			
			CandidatoDao bd = new CandidatoDao();
			bd.Conectar();	
			
			String email = userData.get("email").toString();
			
				if(userData.get("senha").toString().length()>8 ||userData.get("senha").toString().length()<3) 
				{
				throw new SenhaInvalidaException("senha invalida");	
				}
				else 
				{
				boolean con = bd.atualizarCadastro(userData.get("email").toString(), userData.get("nome").toString(), userData.get("senha").toString());	
				
				if(con==false) {
					PrintStream saida  = new PrintStream (ss.getOutputStream());
					myString = new JSONObject().put("operacao", "atualizarCadastro").put("status","404").put("mensagem", "Usuario não encontrado").toString(); 
					saida.println(myString);
				}else {
					//if sem else
				}
				}
			}
			catch(SenhaInvalidaException ex)
			{
					PrintStream saida  = new PrintStream (ss.getOutputStream());
					myString = new JSONObject().put("operacao", "atualizarCadastro").put("status","404").put("mensagem", " A senha menor que 8 digitos e maior que 3 digitos").toString(); 
					saida.println(myString);
			}
			
				if(myString == null) {
					
				
					 
					PrintStream saida  = new PrintStream (ss.getOutputStream());
					myString = new JSONObject().put("operacao", "atualizarCadastro").put("status","201").toString(); 
					saida.println(myString);
				
				}
			

		}catch(Exception ex) {
				System.err.println(ex.getStackTrace());
			}
	}
	
	
	public static void SolicitarLogin(Map<String, Object> userData) throws IOException {
		
			
		
		try 
		{	JSONObject json = new JSONObject();
			json.put("type", "CONNECT");
			
			CandidatoDao bd = new CandidatoDao();
			bd.Conectar();		
			ObjectMapper mapper = new ObjectMapper();  
			PrintStream saida  = new PrintStream (ss.getOutputStream());
		

			 if(token == null) {
				 
				 if(bd.getLogin(userData.get("email").toString(), userData.get("senha").toString())) {
				 		token = UUID.randomUUID();
				 		
				 		String myString = new JSONObject().put("operacao", "loginCandidato").put("status","200").put("token", token.toString()).toString(); 
				 		System.out.println(myString);
				 		saida.println(myString);
				 	}else {
				 		String myString = new JSONObject().put("operacao", "loginCandidato").put("status","401").put("mensagem", "logins ou senha incorretos").toString(); 
				 		System.out.println(myString);
						saida.println(myString);
				 	} 
				 
			 }else {
					String myString = new JSONObject().put("operacao", "loginCandidato").put("status","401").put("mensagem", "Usuario já está logado").toString(); 
					saida.println(myString);
			 }
			 
			 	
			 
			}
			catch(Exception ex)
			{
				System.out.println(ex);
					
			}
	}
	
	public static void SolicitarVisualizacao(String email) throws IOException {
	
	
		try{
			
			ObjectMapper mapper = new ObjectMapper();  
			PrintStream saida  = new PrintStream (ss.getOutputStream());
			
			CandidatoDao bd = new CandidatoDao();
			bd.Conectar();
			ResultSet rs= bd.Ler(email);
			if( rs == null ) {
				String myString = new JSONObject().put("operacao", "visualizarCandidato").put("status","404").put("mensagem", "Usuario não encontrado").toString(); 
				saida.println(myString);
			}else {
				rs.next();
				
				//System.out.println(" "+  +" "+  rs.getString(2).toString());
				
				
				
				String myString = new JSONObject().put("operacao", "visualizarCandidato")
						.put("status","201")
						.put("nome", rs.getString(1).toString())
						.put("senha",rs.getString(3).toString())
						.toString(); 
				saida.println(myString);
			}
		}
		catch(Exception ex)
		{
			System.out.println(ex);
				
		}
		
	}

	public static void apagarCandidato(String email) {
		boolean status = false;
		try{
			System.out.println("1");
			CandidatoDao bd = new CandidatoDao();
			bd.Conectar();
			System.out.println("2" + email);
			status =  bd.apagarCandidato(email);
			
				ObjectMapper mapper = new ObjectMapper();  
				PrintStream saida  = new PrintStream (ss.getOutputStream());
				
				
				System.out.println("3");		
					if(status == true)
					{
						String myString = new JSONObject().put("operacao", "apagarCandidato").put("status","201").toString(); 
						saida.println(myString); 
					}else {
						String myString = new JSONObject().put("operacao", "apagarCandidato").put("status","404").put("mensagem", "Email não encontrado").toString(); 
						saida.println(myString); 
					}
					
				
				
				
	
		}catch(Exception ex) {
			
		}
	}

	public static void logout(Map<String, Object> info) {
		try {
			
			JSONObject json = new JSONObject();
			json.put("type", "CONNECT");
			
			ObjectMapper mapper = new ObjectMapper();  
			PrintStream saida  = new PrintStream (ss.getOutputStream());
			
			if(token != null) {
				
					String myString = new JSONObject().put("operacao", "logout").put("status","204").toString(); 
					token = null;
					saida.println(myString);
				
				
			}
			
		}catch(Exception ex) {
			
		}
	}
}