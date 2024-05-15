package cliente;

import java.net.*;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Cliente {
	
	public static Socket clienteSocket;
	public static UUID token = null;
	//public static InputStreamReader input;
	
	public static void main (String[] args) throws UnknownHostException, IOException {
		Scanner leia = new Scanner(System.in);
		
		System.out.println("Digite o servidor");
		
		String ip = "localhost";
		//ip = leia.nextLine();
		
		clienteSocket = new Socket(ip,22222);
		
		int op=0;
		
	
		
		InputStreamReader input = new InputStreamReader(clienteSocket.getInputStream());
		
		
	
		
		
		
		
		
		
		
		do {
		
			System.out.println("1-Cadastrar usuario\n"
					+ "2-Realizar Login\n"
					+ "3-Visualizar Candidato\n"
					+ "4-Atualizar Cadastro\n"
					+ "5-Realizar logout");
			op = leia.nextInt();
			
			switch(op) {
			case 0:
				
				break;
			case 1: 
				cadastrarUsuario();
			break;
			case 2:
				realizarLogin();
			break;
			case 3:
				visualizarCandidato();
			break;
			case 4:
				atualizarCadastro();
				break;
			case 5: 
				realizarLogout();
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
				String myString = new JSONObject().put("operacao", "cadastrarCandidato").put("nome", nome).put("email", email).put("senha", senha).toString(); 
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
			Map<String, Object> userData = mapper.readValue(reader.readLine(), new TypeReference<Map<String, Object>>() {});
			
			String op = userData.get("status").toString();
			
			if(op.equals("422")) {
				
				System.out.println(userData.get("mensagem"));
				
			}else if(op.equals("404")) {
				
				System.out.println(userData.get("mensagem").toString());
				
			}else {
				token = UUID.fromString(userData.get("token").toString()); ;
				
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
				String myString = new JSONObject().put("operacao","loginCandidato").put("email", email).put("senha", senha).toString(); 
				System.out.println(myString);
				
				saida.println(myString);
				
				
			try {
				InputStreamReader input = new InputStreamReader(clienteSocket.getInputStream());
				BufferedReader reader = new BufferedReader(input);
				
				ObjectMapper mapper = new ObjectMapper(); 
				Map<String, Object> userData = mapper.readValue(reader.readLine(), new TypeReference<Map<String, Object>>() {});
				
				String op = userData.get("status").toString();
				if(op.equals("401")) 
				{
					System.out.println(userData.get("mensagem").toString());
				}
				else if(op.equals("200")) 
				{
					token = UUID.fromString(userData.get("token").toString());
					System.out.println("login realizado com sucesso");
				}
				else 
				{
					token = UUID.fromString(userData.get("token").toString());
					System.out.println("Login realizado com sucesso");
				}
				
			}catch(Exception ex) {
				
			}
			
		}
		catch(Exception ex)
		{
			System.out.println(ex);
				
		}
	}

	
	public static void visualizarCandidato() {

		try {
			JSONObject json = new JSONObject();
			json.put("type", "CONNECT");
			
			Scanner leia = new Scanner(System.in);
			
			PrintStream saida  = new PrintStream (clienteSocket.getOutputStream());
			
			System.out.println("Digite seu email:");
			String email = leia.nextLine();
			
			
			String myString = new JSONObject().put("operacao","visualizarCandidato").put("email", email).toString(); 
			System.out.println(myString);
			
			saida.println(myString);
			
			
			try {
				InputStreamReader input = new InputStreamReader(clienteSocket.getInputStream());
				BufferedReader reader = new BufferedReader(input);
				
				ObjectMapper mapper = new ObjectMapper(); 
				Map<String, Object> userData = mapper.readValue(reader.readLine(), new TypeReference<Map<String, Object>>() {});
				
				String op = userData.get("status").toString();
				if(op.equals("201")) 
				{
					System.out.println("nome: " + userData.get("nome").toString() +"\nsenha: " + userData.get("senha").toString());
				}
				else if(op.equals("404")) 
				{
					System.out.println(userData.get("mensagem").toString());
				}
				else 
				{
					System.out.println("Registro realizado com sucesso");
				}
			}catch(Exception ex) {
				System.out.println(ex);
			}
				
				
		}catch(Exception ex)
		{
			System.out.println(ex);
				
		}
		
		
	}
	
	public static void atualizarCadastro() {
		
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
				String myString = new JSONObject().put("operacao", "atualizarCandidato").put("nome", nome).put("email", email).put("senha", senha).toString(); 
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
			Map<String, Object> userData = mapper.readValue(reader.readLine(), new TypeReference<Map<String, Object>>() {});
			
			String op = userData.get("status").toString();
			
			if(op.equals("201")) {
				
				System.out.println("Atualiza��o realizada com sucesso");
				
			}else if(op.equals("404")) {
				
				System.out.println(userData.get("mensagem").toString());
				
			}else {
				
				System.out.println(userData.get("mensagem").toString());
				
			}
			
		}catch(Exception ex) {
			System.out.print(ex);
		}
		
		
		
		
		
		
		
		
		
	}
	
	public static void realizarLogout() {
		
		try {
			JSONObject json = new JSONObject();
			json.put("type", "CONNECT");
			Scanner leia = new Scanner(System.in);
			
			PrintStream saida  = new PrintStream (clienteSocket.getOutputStream());	
			
			if(token != null) {
				String myString = new JSONObject().put("operacao", "logout").put("token", token.toString()).toString(); 
				
				
				
				
				try {
					InputStreamReader input = new InputStreamReader(clienteSocket.getInputStream());
					BufferedReader reader = new BufferedReader(input);
					
					ObjectMapper mapper = new ObjectMapper(); 
					Map<String, Object> Data = mapper.readValue(reader.readLine(), new TypeReference<Map<String, Object>>() {});
					
					String op = Data.get("status").toString();
					if(op.equals("422")) {
						token = null;
						System.out.println(Data.get("mensagem").toString());
						
					}else if(op.equals("204")){
						
					}else {
						token = null;
						System.out.print("logout realizado com sucesso");
					}
				}catch(Exception ex) {
					
				}
				
				
				
				
				
				
				
				
				
			}else {
				System.out.println("Voc� n�o est� logado");
			}
			
			
		}catch(Exception ex) {
			
		}
		
		
		
	}
}
