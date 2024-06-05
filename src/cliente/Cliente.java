package cliente;

import java.net.*;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import entities.Candidato;
import entities.Competencia;
import entities.Empresa;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Cliente {
	
	public static Socket clienteSocket = null;
	public static UUID token = null;
	//public static InputStreamReader input;
	public static Scanner leia = new Scanner(System.in);
	public static Candidato sessao = null;

	
	public static void main (String[] args) throws UnknownHostException, IOException {
		
		
		System.out.println("Digite o servidor");
		String ip = leia.nextLine();
		clienteSocket = new Socket(ip,22222);
		
		
		ClienteCandidato cliente = new ClienteCandidato();
		ClienteEmpresa empresa = new ClienteEmpresa(clienteSocket);
		ClienteCompetencia competencia = new ClienteCompetencia();

//		
//		
		sessao = new Candidato("celson", "123","celsonb@",UUID.randomUUID());
//		competencia.cadastrarCandidatoCompetencia(candidato);

	
		
		
		
		
		int opcao=3;
		do {
			
			System.out.println("1-Empresa \n2-Candidato\n3-competencia do candidato\n4-Vagas da empresa \n0-Sair da aplicação");
			opcao = leia.nextInt();
			switch(opcao) {
			case 1:	
				clienteEmpresa(empresa);
				break;
			case 2: 
				clienteCandidato(cliente);
				break;
			case 3:
				competenciaExperiencia(competencia);
				break;
			case 4:
				
				break;
				
			case 0:
			
			}
		
		}while(opcao!=0);
				
	}
	
	protected static void competenciaExperiencia(ClienteCompetencia competencia) {
		int op;
		//System.out.println(sessao);
				do {
			
			System.out.println("Selecione qual opção deseja: \n"
					+ "1-Cadastrar competencia\n"
					+ "2-Visualizar competencia\n"
					+ "3-Apagar competencia\n"
					+ "4-Atualizar competencias\n"
					+ "0-Voltar");
			op = leia.nextInt();
			
			switch(op) {
			
			case 1: 
				competencia.cadastrarCandidatoCompetencia();
			break;
			case 2:
				competencia.visualizarCompetencia();
			break;
			case 3:
				competencia.apagarCompetencia();
			break;
			case 4:
				competencia.atualizarCompetencia();
				break;
				}
		}while(op!=0);
		
	}
	
	protected static void clienteEmpresa(ClienteEmpresa empresa) {
		int op;
		do {
			
			System.out.println("1-Cadastrar empresa\n"
					+ "2-Realizar Login\n"
					+ "3-Visualizar Candidato\n"
					+ "4-Atualizar Cadastro\n"
					+ "5-Deletar cadastro\n"
					+ "6-Realizar logout\n"
					+ "0-Voltar");
			op = leia.nextInt();
			
			switch(op) {
			
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
			case 0:
				
				
			}
		}while(op!=0);
	}
	
	
	protected static void clienteCandidato(ClienteCandidato cliente) {
		int op =0;
		do {
			
			System.out.println("1-Cadastrar usuario\n"
					+ "2-Realizar Login\n"
					+ "3-Visualizar Candidato\n"
					+ "4-Atualizar Cadastro\n"
					+ "5-Deletar cadastro\n"
					+ "6-Realizar logout\n"
					+ "0-Voltar");
			op = leia.nextInt();
			
			switch(op) {
			
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
			case 0:
				
		
			}
		}while(op!=0);
	}
}
	
	