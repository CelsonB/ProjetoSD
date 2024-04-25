package servidor;


import java.net.Socket;
import java.net.ServerSocket;
import java.net.*;
import java.io.*;
import java.util.Scanner;
import dao.*;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper; 
import com.fasterxml.jackson.core.type.TypeReference; 

public class Servidor {
	
	public static ServerSocket servidorSocket;
	
	public static void main(String[] args) throws IOException{
		System.out.println("programa 1: servidor");
		
		servidorSocket = new ServerSocket(2299,5);
		Socket ss=servidorSocket.accept(); 
		
		InputStreamReader inputReader = new InputStreamReader(ss.getInputStream());
		BufferedReader reader = new BufferedReader(inputReader);
		System.out.println(reader.toString());
		
		//SolicitarCadastro();
		
				
	}
	
	public static void SolicitarCadastro() {
		
		
		try 
		{	
			System.out.println("conectado");
			
			Crud bd = new Crud();
			bd.Conectar();
			 
			Socket ss=servidorSocket.accept(); 
			InputStreamReader inputReader = new InputStreamReader(ss.getInputStream());
			BufferedReader reader = new BufferedReader(inputReader);
			String x; 
			System.out.println(reader.toString());
			
			ObjectMapper mapper = new ObjectMapper();  
			
			Map<String, Object> userData = mapper.readValue(reader, new TypeReference<Map<String, Object>>() {}); 
						
			 bd.Cadastrar(userData.get("Email:").toString(),userData.get("nome:").toString(), userData.get("Senha:").toString());
 
			}
			catch(Exception ex)
			{
				System.out.println(ex);
					
			}
	}
	
	public static void SolicitarLeitura() {
		
	}

}
	