package cliente;

import java.net.*;
import java.util.Scanner;
import org.json.JSONObject;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class Cliente {
	
	public static Socket clienteSocket;

	
	public static void main (String[] args) throws UnknownHostException, IOException {
		clienteSocket = new Socket("teste",2299);
		
		int op=2;
		
//		OutputStreamWriter out = new OutputStreamWriter(clienteSocket.getOutputStream(), StandardCharsets.UTF_8);
//		out.write(op);
		
		//Scanner leia = new Scanner(System.in);
		
		
		//op = leia.nextInt();
		switch(op) {
		case 1: 
			CadastrarUsuario();
		break;
		case 2:
			RealizarLogin();
		break;
		}
		
			
	}
	
	
	public static void CadastrarUsuario() {
		try 
		{
			JSONObject json = new JSONObject();
			json.put("type", "CONNECT");
			
			
			
			Scanner leia = new Scanner(System.in);
			try (OutputStreamWriter out = new OutputStreamWriter(
				clienteSocket.getOutputStream(), StandardCharsets.UTF_8)) {
				
				System.out.println("Digite seu nome:");
				String nome = leia.nextLine(); 
				System.out.println("Digite sua senha:");
				String senha= leia.nextLine();
				System.out.println("Digite seu email:");
				String email = leia.nextLine(); 
				String myString = new JSONObject().put("Email:", email).put("Senha:", senha).put("nome:", nome).toString(); 
			    out.write(myString);
			}
			
		}
		catch(Exception ex)
		{
			System.out.println(ex);
				
		}
	}
	
	public static void RealizarLogin() {
		try 
		{
			JSONObject json = new JSONObject();
			json.put("type", "CONNECT");
			
			
			
			Scanner leia = new Scanner(System.in);
			try (OutputStreamWriter out = new OutputStreamWriter(
				clienteSocket.getOutputStream(), StandardCharsets.UTF_8)) {
				
				System.out.println("Digite seu email:");
				String email = leia.nextLine();
				System.out.println("Digite sua senha:");
				String senha= leia.nextLine();
				String myString = new JSONObject().put("Email:", email).put("Senha:", senha).toString(); 
			    out.write(myString);
			}
			
		}
		catch(Exception ex)
		{
			System.out.println(ex);
				
		}
	}
}
