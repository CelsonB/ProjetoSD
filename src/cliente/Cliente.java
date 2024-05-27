package cliente;

import java.net.*;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import entities.Empresa;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Cliente {
	
	public static Socket clienteSocket = null;
	public static UUID token = null;
	//public static InputStreamReader input;
	
	public static Scanner leia = new Scanner(System.in);
	
	
	public static void main (String[] args) throws UnknownHostException, IOException {
		
		
		System.out.println("Digite o servidor");
		String ip = leia.nextLine();
		clienteSocket = new Socket(ip,22222);
		ClienteCandidato cliente = new ClienteCandidato(clienteSocket);
		ClienteEmpresa empresa = new ClienteEmpresa(clienteSocket);
		int op=0;
		
	
		
		InputStreamReader input = new InputStreamReader(clienteSocket.getInputStream());
		
		//cliente.cadastrarEmpresa();
	
		
		int opcao=0;
		do {
			
			System.out.println("1-Empresa \n2-Candidato \n0-Sair da aplicação");
			opcao = leia.nextInt();
			switch(opcao) {
			case 1:	
				clienteEmpresa(empresa);
				break;
			case 2: 
				clienteCandidato(cliente);
				break;
			case 0:
			
			break;
			}
		
		}while(op!=0);
				
	}
	
	private static void clienteEmpresa(ClienteEmpresa empresa) {
		int op;
		do {
			
			System.out.println("1-Cadastrar empresa\n"
					+ "2-Realizar Login\n"
					+ "3-Visualizar Candidato\n"
					+ "4-Atualizar Cadastro\n"
					+ "5-Deletar cadastro\n"
					+ "6-Realizar logout"
					+ "0-Voltar");
			op = leia.nextInt();
			
			switch(op) {
			case 0:
				
				break;
			case 1: 
				empresa.cadastrarEmpresa();
			break;
			case 2:
				empresa.realizarLogin();
			break;
			case 3:
				empresa.visualizarEmpresa();
			break;
			case 4:
				empresa.atualizarEmpresa();
				break;
			case 5: 
				empresa.apagarEmpresa();
				break;
			case 6: 
				empresa.logout();
				break;
			
			}
		}while(op!=0);
	}
	
	
	private static void clienteCandidato(ClienteCandidato cliente) {
		int op;
		do {
			
			System.out.println("1-Cadastrar usuario\n"
					+ "2-Realizar Login\n"
					+ "3-Visualizar Candidato\n"
					+ "4-Atualizar Cadastro\n"
					+ "5-Deletar cadastro\n"
					+ "6-Realizar logout"
					+ "0-Voltar");
			op = leia.nextInt();
			
			switch(op) {
			case 0:
				
				break;
			case 1: 
				cliente.cadastrarUsuario();
			break;
			case 2:
				cliente.realizarLogin();
			break;
			case 3:
				cliente.visualizarCandidato();
			break;
			case 4:
				cliente.atualizarCadastro();
				break;
			case 5: 
				cliente.deletarCadastro();
				break;
			case 6: 
				cliente.realizarLogout();
				break;
			
			}
		}while(op!=0);
	}
}
	
	