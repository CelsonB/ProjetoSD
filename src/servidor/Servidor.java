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

	public static void main(String[] args){
		
		
		
		try 
		{	System.out.println("programa 1: servidor");
		
			ServerSocket servidorSocket = new ServerSocket(8787,5);
			Socket ss=servidorSocket.accept();  
			System.out.println("conectado");
			
			CrudCliente bd = new CrudCliente();
			
			 bd.Conectar();
			 bd.Ler(3);
			 
			
			InputStreamReader inputReader = new InputStreamReader(ss.getInputStream());
			BufferedReader reader = new BufferedReader(inputReader);
			String x; 
	
			
			ObjectMapper mapper = new ObjectMapper();  
			
			Map<String, Object> userData = mapper.readValue(  
					 reader, new TypeReference<Map<String, Object>>() {  
	           }); 
			
			
			 String email =  userData.get("Email:").toString();  
			 String senha =  userData.get("Senha:").toString();  
			 String nome = userData.get("nome:").toString(); 
			
			 bd.Cadastrar(email, nome, senha);
			
		
	            
			 
		}
		catch(Exception ex)
		{
			System.out.println(ex);
				
		}
				
	}

}
	