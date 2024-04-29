package cliente;

import java.net.*;
import java.util.Map;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Cliente {
	
	public static Socket clienteSocket;

	
	public static void main (String[] args) throws UnknownHostException, IOException {
		clienteSocket = new Socket("teste",22222);
		
		int op=0;
		
		//OutputStreamWriter out = new OutputStreamWriter(clienteSocket.getOutputStream(), StandardCharsets.UTF_8);
		
		PrintStream saida  = new PrintStream (clienteSocket.getOutputStream());
		saida.println("conexão realizada com sucesso o cliente");
		
		InputStreamReader input = new InputStreamReader(clienteSocket.getInputStream());
		BufferedReader reader = new BufferedReader(input);
		System.out.println(reader.readLine());
		
		
		Scanner leia = new Scanner(System.in);
		
		
		//OutputStreamWriter outStream = new OutputStreamWriter(clienteSocket.getOutputStream(), StandardCharsets.UTF_8);
		
		
		
		do {
		
			System.out.println("1-Cadastrar usuario\n2-Realizar Login");
			op = leia.nextInt();
			
			switch(op) {
			case 1: 
				cadastrarUsuario();
			break;
			case 2:
				realizarLogin();
			break;
			case 3:
				visualizarCandidato();
			break;
			}
		}while(op!=0);
		
		
		
			
	}
	
	
	public static void cadastrarUsuario() {
		try 
		{
			JSONObject json = new JSONObject();
			json.put("type", "CONNECT");
			Scanner leia = new Scanner(System.in);
			
			PrintStream saida  = new PrintStream (clienteSocket.getOutputStream());
			
			
				System.out.println("Digite seu nome:");
				String nome = leia.nextLine();
				System.out.println("Digite sua senha:");
				String senha= leia.nextLine();
				System.out.println("Digite seu email:");
				String email = leia.nextLine(); 
				String myString = new JSONObject().put("operacao:", "realizarCadastro").put("nome:", nome).put("email:", email).put("senha:", senha).toString(); 
				System.out.println(myString);

			    saida.println(myString);
			
			
		}
		catch(Exception ex)
		{
			System.out.println(ex);
				
		}
		
	
		try {
			InputStreamReader input = new InputStreamReader(clienteSocket.getInputStream());
			BufferedReader reader = new BufferedReader(input);
			
			ObjectMapper mapper = new ObjectMapper(); 
			Map<String, Object> userData = mapper.readValue(reader, new TypeReference<Map<String, Object>>() {});
			
			String op = userData.get("status:").toString();
			if(op.equals("422")) {
				System.out.println("Já existe esse email");
			}else if(op.equals("404")) {
				System.out.println(userData.get("mensagem:").toString());
			}else {
				System.out.println("Registro realizado com sucesso");
			}
		}catch(Exception ex) {
			System.out.print(ex);
		}
		 
	}
	
	public static void realizarLogin() {
		try 
		{
			JSONObject json = new JSONObject();
			json.put("type", "CONNECT");
			
			
			
				Scanner leia = new Scanner(System.in);
			
				PrintStream saida  = new PrintStream (clienteSocket.getOutputStream());
				
				System.out.println("Digite seu email:");
				String email = leia.nextLine();
				System.out.println("Digite sua senha:");
				String senha= leia.nextLine();
				String myString = new JSONObject().put("operacao:","loginCandidato").put("email:", email).put("senha:", senha).toString(); 
				System.out.println(myString);
				
				saida.println(myString);
				
				
			try {
				InputStreamReader input = new InputStreamReader(clienteSocket.getInputStream());
				BufferedReader reader = new BufferedReader(input);
				
				ObjectMapper mapper = new ObjectMapper(); 
				Map<String, Object> userData = mapper.readValue(reader, new TypeReference<Map<String, Object>>() {});
				
				String op = userData.get("status:").toString();
				if(op.equals("401")) {
					System.out.println(userData.get("mensagem:").toString());
				}else if(op.equals("200")) {
					System.out.println("login realizado com sucesso");
				}else {
					System.out.println("Registro realizado com sucesso");
				}
				
			}catch(Exception ex) {
				
			}
			
		}
		catch(Exception ex)
		{
			System.out.println(ex);
				
		}
	}
	public static void realizarLogout() {
		
	}
	
	public static void visualizarCandidato() {

		try {
			JSONObject json = new JSONObject();
			json.put("type", "CONNECT");
			
			Scanner leia = new Scanner(System.in);
			
			PrintStream saida  = new PrintStream (clienteSocket.getOutputStream());
			
			System.out.println("Digite seu email:");
			String email = leia.nextLine();
			
			
			String myString = new JSONObject().put("operacao:","visualizarCandidato").put("email:", email).toString(); 
			System.out.println(myString);
			
			saida.println(myString);
			
		}catch(Exception ex)
		{
			System.out.println(ex);
				
		}
		
		
	}
}
