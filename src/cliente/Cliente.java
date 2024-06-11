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
	public static Empresa sessaoEmpresa = null;
	protected static String [] competenciasNomes  = {"python" , "c#", "c++"};
	
	public static void main (String[] args) throws UnknownHostException, IOException {
		
		
		System.out.println("Digite o servidor");
		String ip = leia.nextLine();
		clienteSocket = new Socket(ip,22222);
		
		
		ClienteCandidato cliente = new ClienteCandidato();
		ClienteEmpresa empresa = new ClienteEmpresa(clienteSocket);
		ClienteCompetencia competencia = new ClienteCompetencia();
		
		
		sessao = new Candidato();
		sessaoEmpresa = new Empresa();

		
//		
//		sessao = new Candidato("celson", "123","celsonb@",UUID.randomUUID());
//		//String email, String senha, String razaoSocial, String descricao, String ramo,String cnpj
//		sessaoEmpresa = new Empresa("bsoft@", "123","bsoft","Empresa de logistica", "logistica", "123321123321");
//		sessaoEmpresa.setToken(UUID.randomUUID());
////		competencia.cadastrarCandidatoCompetencia(candidato);

	
		
		
		
		
		int opcao=3;
		do {
			
			
		
				String [] strOp = {"Sair","Empresa", "Candidato", "competencia", "Vagas"};
				int tamanhoOp =strOp.length ; 
				for(int i = 1; i<=tamanhoOp-1; i++) {
					System.out.println(i+"-"+strOp[i]);
				} System.out.println("0-Sair");

			opcao = leia.nextInt();
			switch(opcao) {
			case 1:	
				clienteEmpresa(empresa);
				break;
			case 2: 
				clienteCandidato(cliente);
				break;
			case 3:
				if(sessao.getToken()==null) {
					System.out.println("Você não está logado");
				}else {
					competenciaExperiencia(competencia);
				}
				
				break;
			case 4:
				
				if(sessaoEmpresa.getToken()==null) {
					System.out.println("Você não está logado");
				}else {
					ClienteVaga vaga = new ClienteVaga();
					vagaEmpresa (vaga);
				}
				
				break;
				
			case 0:
			
			}
		
		}while(opcao!=0);
				
	}
	
	protected static void vagaEmpresa (ClienteVaga vaga) {
		int op =0 ;
		
				do {
			
			System.out.println("Selecione qual opção deseja: \n"
					+ "1-Cadastrar vaga\n"
					+ "2-Visualizar vaga\n"
					+ "3-Apagar vaga\n"
					+ "4-Atualizar vaga\n"
					+ "0-Voltar");
			try {
				op = leia.nextInt();
			}catch(Exception ex) {
				System.out.println(ex);
			}
		
			switch(op) {
			
			case 1: 
				vaga.cadastrarVagas();
			break;
			case 2:
				vaga.visualizarVagas();
			break;
			case 3:
				vaga.apagarVaga();
			break;
			case 4:
				vaga.atualizarVagas();
				break;
				}
		}while(op!=0);
	}
	
	protected static void competenciaExperiencia(ClienteCompetencia competencia) {
		int op =0 ;
		//System.out.println(sessao);
				do {
					
					
					String [] strOp = {"Sair","Cadastrar competencia", "Visualizar competencia", "Apagar competencia", "Atualizar competencias"};
					int tamanhoOp = 0; 
					if(sessaoEmpresa.getToken()!=null) {tamanhoOp = strOp.length;} else {tamanhoOp = 3;}
					for(int i = 1; i<=tamanhoOp-1; i++) {
						System.out.println(i+"-"+strOp[i]);
					} System.out.println("0-Sair");
					
					
					
					try {
						op = leia.nextInt();
					}catch(Exception ex) {
						System.out.println(ex);
					}
			
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
		int op = 0;
		do {
			
			String [] strOp = {"Sair","Cadastrar empresa", "Realizar login", "Visualizar empresa", "Atualizar cadastro","Deletar empresa", "Realizar logout"};
			int tamanhoOp = 0; 
			if(sessaoEmpresa.getToken()!=null) {tamanhoOp = strOp.length;} else {tamanhoOp = 3;}
			for(int i = 1; i<=tamanhoOp-1; i++) {
				System.out.println(i+"-"+strOp[i]);
			} System.out.println("0-Sair");
			
			try {
				op = leia.nextInt();
			}catch(Exception ex) {
				System.out.println(ex);
			}
			
			
			switch(op) {
			
			case 1: 
				empresa.cadastrarEmpresa();
			break;
			case 2:
				empresa.realizarLogin();
			break;
			case 3:
				if(sessaoEmpresa.getToken()!=null)	empresa.visualizarEmpresa();
			break;
			case 4:
				if(sessaoEmpresa.getToken()!=null)	empresa.atualizarEmpresa();
				break;
			case 5: 
				if(sessaoEmpresa.getToken()!=null)	empresa.apagarEmpresa();
				break;
			case 6: 
				if(sessaoEmpresa.getToken()!=null)	empresa.logout();
				break;
			case 0:
				
				
			}
		}while(op!=0);
	}
	
	
	protected static void clienteCandidato(ClienteCandidato cliente) {
		int op =0;
		do {
			String [] strOp = {"Sair","Cadastrar usuario", "Realizar login", "Visualizar candidato", "Atualizar cadastro","Deletar cadastro", "Realizar logout"};
			int tamanhoOp = 0; 
			if(sessao.getToken()!=null) {tamanhoOp = strOp.length;} else {tamanhoOp = 3;}
			for(int i = 1; i<=tamanhoOp-1; i++) {
				System.out.println(i+"-"+strOp[i]);
			} System.out.println("0-Sair");
			
			
			try {
				op = leia.nextInt();
			}catch(Exception ex) {
				System.out.println(ex);
			}
			
			switch(op) {
			
			case 1: 
				cliente.cadastrarUsuario();
			break;
			case 2:
				cliente.realizarLogin();
			break;
			case 3:
				if(sessao.getToken()!=null)cliente.visualizarCandidato();
			break;
			case 4:
				if(sessao.getToken()!=null)cliente.atualizarCadastro();
				break;
			case 5: 
				if(sessao.getToken()!=null)cliente.deletarCadastro();
				break; 
			case 6: 
				if(sessao.getToken()!=null)cliente.realizarLogout();
				break;
			case 0:
				
		
			}
		}while(op!=0);
	}
}
	
	