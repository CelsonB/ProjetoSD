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

public class Servidor {
	private static UUID token = null;
	public static ServerSocket servidorSocket;
	public static Socket ss;
	public static void main(String[] args) throws IOException{
		System.out.println("programa 1: servidor");
		
		
		
		servidorSocket = new ServerSocket(22222,5);
		ss=servidorSocket.accept(); 	 
		
		
	
		
		InputStreamReader input = new InputStreamReader(ss.getInputStream());
		BufferedReader reader = new BufferedReader(input);
		
		PrintStream saida  = new PrintStream (ss.getOutputStream());
		


		
		
		
		

	String op = ""; 
       
        
		ObjectMapper mapper = new ObjectMapper();  
		do {			
			

			Map<String, Object> userData = mapper.readValue(reader.readLine(), new TypeReference<Map<String, Object>>() {}); 
			
			System.out.print(userData.get("operacao").toString());
			
			op = userData.get("operacao").toString();
			
			if(op.equals("loginCandidato")) {
				SolicitarLogin(userData);
			}else
			
			if(op.equals("cadastrarCandidato")) {	
				System.out.println("Entrou em realizar cadastro 1 ");
				SolicitarCadastro(userData);
			}else
			
			if(op.equals("visualizarCandidato")) {
				SolicitarVisualizacao(userData.get("email").toString());
			}else
				
			if(op.equals("logout")){
				logout(userData);
			}else
				
			if(op.equals("atualizarCandidato")) {
				atualizarCadastro(userData);
			}
			
		}while(op != "");
		
	
				
	}
	
	public static void SolicitarCadastro(Map<String, Object> userData) {
		String myString = null;
		
		System.out.println("Entrou em realizar cadastro 2 ");
		System.out.println(userData.toString());
		try {
			
			try 
			{				
			JSONObject json = new JSONObject();
			json.put("type", "CONNECT");
			
			Crud bd = new Crud();
			bd.Conectar();	
			
			String email = userData.get("email").toString();
			
				if(!email.contains("@")) 
				{
				throw new EmaiInvalidoException("email invalido");
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
			catch(EmaiInvalidoException ex) 
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
				System.err.println(ex.getStackTrace());
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
			
			Crud bd = new Crud();
			bd.Conectar();	
			
			String email = userData.get("email").toString();
			
				if(userData.get("senha").toString().length()>8 ||userData.get("senha").toString().length()<3) 
				{
				throw new SenhaInvalidaException("senha invalida");	
				}
				else 
				{
				bd.atualizarCadastro(userData.get("email").toString(), userData.get("nome").toString(), userData.get("senha").toString());	
				}
			}
			catch(SenhaInvalidaException ex)
			{
					PrintStream saida  = new PrintStream (ss.getOutputStream());
					myString = new JSONObject().put("operacao", "atualizarCadastro").put("status","422").put("mensagem", " A senha menor que 8 digitos e maior que 3 digitos").toString(); 
					saida.println(myString);
			}
			
				if(myString == null) {
					
				
					 
					PrintStream saida  = new PrintStream (ss.getOutputStream());
					myString = new JSONObject().put("operacao", "atualizarCadastro").put("status","201").put("mensagem","Atualização realizada com sucesso").toString(); 
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
			
			Crud bd = new Crud();
			bd.Conectar();		
			ObjectMapper mapper = new ObjectMapper();  
			PrintStream saida  = new PrintStream (ss.getOutputStream());
		

			 if(token == null) {
				 
				 if(bd.getLogin(userData.get("email").toString(), userData.get("senha").toString()) != 0 ) {
				 		token = UUID.randomUUID();
				 		String myString = new JSONObject().put("operacao", "loginCandidato").put("status","200").put("token", token.toString()).toString(); 
						saida.println(myString);
				 	}else {
				 		String myString = new JSONObject().put("operacao", "loginCandidato").put("status","401").put("mensagem", "logins ou senha incorretos").toString(); 
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
			
			Crud bd = new Crud();
			bd.Conectar();
			ResultSet rs= bd.Ler(email);
			if( rs == null ) {
				String myString = new JSONObject().put("operacao", "visualizarCandidato").put("status","404").put("mensagem", "Usuario não encontrado").toString(); 
				saida.println(myString);
			}else {
				rs.next();
				
				//System.out.println(" "+  +" "+  rs.getString(2).toString());
				
				
				
				String myString = new JSONObject().put("operacao", "visualizarCandidato")
						.put("status","401")
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



	public static void logout(Map<String, Object> info) {
		try {
			
			JSONObject json = new JSONObject();
			json.put("type", "CONNECT");
			
			ObjectMapper mapper = new ObjectMapper();  
			PrintStream saida  = new PrintStream (ss.getOutputStream());
			
			if(token != null) {
				if(info.get("token").toString().equals(token.toString())) {
					String myString = new JSONObject().put("operacao", "logout").put("status","422").toString(); 
					token = null;
					saida.println(myString);
				}else {
					
				}
				
			}
			
		}catch(Exception ex) {
			
		}
	}
}