package cliente;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import org.json.JSONObject;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import entities.Empresa;

public class ClienteCandidato {
	
	public static Socket clienteSocket = null;
	public static UUID token = null;
	//public static InputStreamReader input;
	
	
	public ClienteCandidato(Socket ip) {
		clienteSocket = ip;
	}
	
	
	
	
	
	
	public static void cadastrarEmpresa() {
		try 
		{
			JSONObject json = new JSONObject();
			json.put("type", "CONNECT");
			Scanner leia = new Scanner(System.in);
			
			PrintStream saida  = new PrintStream (clienteSocket.getOutputStream());
			Empresa emp1 = new Empresa();
			
				System.out.println("Digite seu nome:");
				emp1.setNome(leia.nextLine()); 
				System.out.println("Digite sua senha:");
				emp1.setSenha(leia.nextLine());
				System.out.println("Digite seu email:");
				emp1.setEmail(leia.nextLine());
				
				System.out.println("Digite sua razao social:");
				emp1.setRazaoSocial(leia.nextLine());
				
				System.out.println("Digite seu cnpj:");
				String cnpj = leia.nextLine();
				
				System.out.println("Digite uma breve descrição da empresa:");
				emp1.setDescricao(leia.nextLine());
				
				System.out.println("Digite o ramo que sua empresa trabalha:");
				emp1.setRamo(leia.nextLine());
				
				//{"operacao": "cadastrarEmpresa", "razaoSocial":"xxxxxxx", "email":"xx@xxx.xxx", "cnpj": "12345678000100", "senha":"xxx","descricao":"xxxxxx","ramo":"xxxxxx"}
				String myString = new JSONObject()
						.put("operacao", "cadastrarEmpresa")
						.put("nome", emp1.getNome())
						.put("email", emp1.getEmail())
						.put("senha", emp1.getSenha())
						.put("cnpj", cnpj)
						.put("razaoSocial", emp1.getRazaoSocial())
						.put("descricao", emp1.getDescricao())
						.put("ramo", emp1.getRamo())
						.toString(); 
				
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
					///token = UUID.fromString(userData.get("token").toString());
					//System.out.println("Login realizado com sucesso");
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
				
				System.out.println("Atualização realizada com sucesso");
				
			}else if(op.equals("404")) {
				
				System.out.println(userData.get("mensagem").toString());
				
			}else {
				
				System.out.println(userData.get("mensagem").toString());
				
			}
			
		}catch(Exception ex) {
			System.out.print(ex);
		}
		
		
		
		
		
		
		
		
		
	}
	
	public static void deletarCadastro() {
		
		try 
		{
			JSONObject json = new JSONObject();
			json.put("type", "CONNECT");
			Scanner leia = new Scanner(System.in);
			
			PrintStream saida  = new PrintStream (clienteSocket.getOutputStream());
			
				System.out.println("Digite seu email:");
				String email = leia.nextLine(); 
				String myString = new JSONObject().put("operacao", "apagarCandidato").put("email", email).toString(); 
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
				
				System.out.println("Apagar candidado realizado com sucesso");
				
			}else if(op.equals("404")) {
				
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
				saida.println(myString);
				
				
				
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
						token = null;
						System.out.println(Data.get("mensagem").toString());
					}else {
						token = null;
						System.out.print("logout realizado com sucesso");
					}
				}catch(Exception ex) {
					
				}
				
				
				
				
				
				
				
				
				
			}else {
				System.out.println("Você não está logado");
			}
			
			
		}catch(Exception ex) {
			
		}
		
		
		
	}


}

