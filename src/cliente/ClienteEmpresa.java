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

public class ClienteEmpresa {
	public static Scanner leia  = new Scanner(System.in);	
	public static Socket clienteSocket = null;
	public static UUID token = null;
	
	public ClienteEmpresa(Socket ip) {
		clienteSocket = ip;
	}
	
	public void cadastrarEmpresa() {
	
	Empresa cadastro = new Empresa();	
	
	String dado; 
	
	
	System.out.println("Por favor digite o nome da empresa:");
	dado = leia.nextLine();
	cadastro.setNome(dado);
	
	
	System.out.println("Por favor digite a razao social da empresa:");
	dado = leia.nextLine();
	cadastro.setRazaoSocial(dado);
	
	System.out.println("Por favor digite o email da empresa:");
	dado = leia.nextLine();
	cadastro.setEmail(dado);;
	
	System.out.println("Por favor digite o cnpj da empresa:");
	dado = leia.nextLine();
	cadastro.setCnpj(dado);
	
	System.out.println("Por favor digite a senha da empresa:");
	dado = leia.nextLine();
	cadastro.setSenha(dado);
	
	
	System.out.println("Por favor digite a descricao da empresa:");
	dado = leia.nextLine();
	cadastro.setDescricao(dado);
	
	
	System.out.println("Por favor digite o ramo da empresa:");
	dado = leia.nextLine();
	cadastro.setRamo(dado);
	
	enviarJsonCadastro(cadastro, "cadastrarEmpresa");
	
	
	}
	
	public void realizarLogin() {
		String email = null, senha=null; 
		
		System.out.println("Por favor digite seu email");
		email = leia.nextLine();
		
		System.out.println("Por favor digite sua senha");
		senha = leia.nextLine();
		
		enviarJsonLogin(email,senha);
		receberRespostaServidor();
	}
	
	public void visualizarEmpresa() {
//{"operacao": "visualizarEmpresa","email":"xx@xxx.xxx"}
		System.out.println("Por favor digite o email que deseja visualizar");
		String email = leia.nextLine();
		
		
		enviarJsonLeitura(email,"visualizarEmpresa");
		receberRespostaServidor();
		
	}
	
	public void atualizarEmpresa() {
		Empresa att = new Empresa();	
		
		String dado; 
		
		
		System.out.println("Por favor digite o nome da empresa:");
		dado = leia.nextLine();
		att.setNome(dado);
		
		
		System.out.println("Por favor digite a razao social da empresa:");
		dado = leia.nextLine();
		att.setRazaoSocial(dado);
		
		System.out.println("Por favor digite o email da empresa:");
		dado = leia.nextLine();
		att.setEmail(dado);;
		
		System.out.println("Por favor digite o cnpj da empresa:");
		dado = leia.nextLine();
		att.setCnpj(dado);
		
		System.out.println("Por favor digite a senha da empresa:");
		dado = leia.nextLine();
		att.setSenha(dado);
		
		
		System.out.println("Por favor digite a descricao da empresa:");
		dado = leia.nextLine();
		att.setDescricao(dado);
		
		
		System.out.println("Por favor digite o ramo da empresa:");
		dado = leia.nextLine();
		att.setRamo(dado);
		
		enviarJsonCadastro(att, "atualizarEmpresa");
		receberRespostaServidor();
		
	
	}
	
	public void apagarEmpresa() {
		//{"operacao": "apagarEmpresa","email":"xx@xxx.xxx"}
		String dado;
		System.out.println("Por favor digite o email da empresa que deseja apagar:");
		dado = leia.nextLine();
		
		enviarJsonLeitura(dado, "apagarEmpresa");
		receberRespostaServidor();
		
	}
	
	public void logout() {
		if(token !=null) {
			enviarTokenLogout();
			receberRespostaServidor();
			
		}else {
			System.out.print("Você não está logado");
		}
	}
	
	
	private void enviarTokenLogout() {
		try {
			PrintStream saida  = new PrintStream (clienteSocket.getOutputStream());
			JSONObject json = new JSONObject();
			json.put("type", "CONNECT");
			
			String myString = new JSONObject()
					.put("operacao", "logout")
					.put("token", token.toString())
					.toString(); 
			
			System.out.println(myString);
			
			  saida.println(myString);
		}catch(Exception ex) {
			System.out.print(ex);
		}
	}
	private void enviarJsonLeitura(String email, String op ) {
		try {
			PrintStream saida  = new PrintStream (clienteSocket.getOutputStream());
			JSONObject json = new JSONObject();
			json.put("type", "CONNECT");
			
			String myString = new JSONObject()
					.put("operacao", op)
					.put("email", email)
					.toString(); 
			
			System.out.println(myString);
			
			   saida.println(myString);
		}catch(Exception ex) {
			System.out.print(ex);
		}
	}
	
	private void enviarJsonLogin(String email, String senha) {
		try {
			PrintStream saida  = new PrintStream (clienteSocket.getOutputStream());
			JSONObject json = new JSONObject();
			json.put("type", "CONNECT");
			
			String myString = new JSONObject()
					.put("operacao", "loginEmpresa")
					.put("email", email)
					.put("senha",senha)
					.toString(); 
			
			System.out.println(myString);
			
			   saida.println(myString);
		}catch(Exception ex) {
			System.out.print(ex);
		}
		
	}
	
	private void receberRespostaServidor() {
		try {
			InputStreamReader input = new InputStreamReader(clienteSocket.getInputStream());
			BufferedReader reader = new BufferedReader(input);
			
			ObjectMapper mapper = new ObjectMapper(); 
			Map<String, Object> data = mapper.readValue(reader.readLine(), new TypeReference<Map<String, Object>>() {});
			
			
				String op = data.get("status").toString();
			
			if(op.equals("201") || op.equals("200")) {
				
				System.out.println("Atualização realizada com sucesso");
				if(data.get("operacao").toString().equals("cadastrarEmpresa") || data.get("operacao").toString().equals("loginEmpresa") ) {
					
					
					token =   UUID.fromString ( data.get("token").toString());
				}else 
				if(data.get("operacao").toString().equals("visualizarEmpresa")){
					
					System.out.println("dados: "
							+"razao social"+ data.get("razaoSocial").toString()
							+"cnpj"+ data.get("cnpj").toString()
							+"senha"+ data.get("senha").toString()
							+"descricao"+ data.get("descricao").toString()
							+"ramo"+ data.get("ramo").toString()
							);
				}
				
			}else {
				
				System.out.println(data.get("mensagem").toString());
				
			}
			
			
		}catch(Exception ex) {
			
		}
		
	}
	
	private void enviarJsonCadastro(Empresa dados,String operacao) {
	try {
		PrintStream saida  = new PrintStream (clienteSocket.getOutputStream());
		
		JSONObject json = new JSONObject();
		json.put("type", "CONNECT");
		
		String myString = new JSONObject()
				.put("operacao", operacao)
				.put("nome", dados.getNome())
				.put("email", dados.getEmail())
				.put("senha", dados.getSenha())
				.put("cnpj", dados.getCnpj())
				.put("razaoSocial",dados.getRazaoSocial())
				.put("descricao", dados.getDescricao())
				.put("ramo", dados.getRamo())
				.toString(); 
		
		System.out.println(myString);
		
		   saida.println(myString);
	}catch(Exception ex) {
		System.out.print(ex);
	}
	}
}
























