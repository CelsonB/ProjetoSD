package servidor;


import java.net.Socket;
import java.nio.charset.StandardCharsets;
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
	
	public static ServerSocket servidorSocket;
	public static Socket ss;
	public static void main(String[] args) throws IOException{
		System.out.println("programa 1: servidor");
		
		
		
		servidorSocket = new ServerSocket(22222,5);
		ss=servidorSocket.accept(); 	 
		
		
	
		
		InputStreamReader input = new InputStreamReader(ss.getInputStream());
		BufferedReader reader = new BufferedReader(input);
		System.out.println(reader.readLine());
		
		PrintStream saida  = new PrintStream (ss.getOutputStream());
		saida.println("Conexão realizada com sucesso com o servidor");


		
		
		
		String op = ""; 

		
			
	//	InputStreamReader inputReader = new InputStreamReader(ss.getInputStream());
		//BufferedReader reader = new BufferedReader(inputReader);
		
		//System.out.println(reader.toString());
	
	
        String leitura = reader.readLine(); 
        
		ObjectMapper mapper = new ObjectMapper();  
		
		System.out.println(leitura);
		
		do {			
			Map<String, Object> userData = mapper.readValue(leitura, new TypeReference<Map<String, Object>>() {}); 
			
			System.out.print(userData.get("operacao:").toString());
			
			op = userData.get("operacao:").toString();
			
			if(op.equals("loginCandidato")) {
				SolicitarLogin(userData);
			}
			
			if(op.equals("realizarCadastro")) {	
				System.out.println("Entrou em realizar cadastro 1 ");
				SolicitarCadastro(userData);
			}
			
			if(op.equals("visualizarCandidato")) {
				SolicitarVisualizacao(userData.get("email:").toString());
			}
			
		}while(op == "");
		
	
				
	}
	
	public static void SolicitarCadastro(Map<String, Object> userData) {

		
		System.out.println("Entrou em realizar cadastro 2 ");
		System.out.println(userData.toString());
			try 
			{				
			JSONObject json = new JSONObject();
			json.put("type", "CONNECT");
			
			Crud bd = new Crud();
			bd.Conectar();
			bd.Cadastrar(userData.get("email:").toString(), userData.get("nome:").toString(), userData.get("senha:").toString());	
			
			throw new SQLIntegrityConstraintViolationException();  
			}
			catch(SQLIntegrityConstraintViolationException e)
			{
				try {
					PrintStream saida  = new PrintStream (ss.getOutputStream());
					String myString = new JSONObject().put("operacao:", "realizarCadastro").put("status:","422").put("mensagem:", "email ja cadastrado").toString(); 
					saida.println(myString);
				}
				catch(Exception ex) {
					
				}
				

			}catch(Exception ex) {
				
			}
		
			

			try {
				PrintStream saida  = new PrintStream (ss.getOutputStream());
				String myString = new JSONObject().put("operacao:", "realizarCadastro").put("status:","201").put("token:", "UUID").toString(); 
				saida.println(myString);
			}
			catch(Exception ex) {
				
			}
			
		
	}
	
	public static void SolicitarLogin(Map<String, Object> userData) throws IOException {
		
		//BufferedReader reader  = new BufferedReader(new InputStreamReader(ss.getInputStream()));
		
		
		try 
		{	
			
			Crud bd = new Crud();
			bd.Conectar();		
			ObjectMapper mapper = new ObjectMapper();  
			

			 
			 
			 	if(bd.getLogin(userData.get("email:").toString(), userData.get("senha:").toString()) != 0 ) {
			 		System.out.print("Login realizado com sucesso");
			 	}else {
			 		System.out.print("Falha ao realizar o login");
			 	}
			 
			}
			catch(Exception ex)
			{
				System.out.println(ex);
					
			}
	}
	
	public static void SolicitarVisualizacao(String email) throws IOException {
	
	
		try{
			Crud bd = new Crud();
			bd.Conectar();	
			
			bd.Ler(email);
		}
		catch(Exception ex)
		{
			System.out.println(ex);
				
		}
		
	}

}
	