package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Map;
import java.util.UUID;

import javax.swing.JOptionPane;

import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import entities.Empresa;

public class EmpresaService {
	
	
	
	
	public static Socket clienteSocket = null;
	public EmpresaService(Socket clienteSocket) {	
		this.clienteSocket = clienteSocket;
	}
	
	
	
	public boolean enviarMensagem(Empresa sessao, int []candidatos) throws IOException, JSONException {
		//{"operacao":"enviarMensagem", "email":"xxxx@xsss.com", "candidatos":[1,2,3,4], "token":"UUID"}
		
		PrintStream saida  = new PrintStream (clienteSocket.getOutputStream());
		JSONObject json = new JSONObject();
		json.put("type", "CONNECT");
		
		String myString = new JSONObject()
				.put("operacao", "enviarMensagem")
				.put("email", sessao.getEmail())
				.put("candidatos", candidatos )
				.put("token", sessao.getToken())
				.toString(); 
		
			System.out.println("Saida: [" +myString + "]");
		
		   saida.println(myString);
		   return receberRespostaMensagem();
	}
	
	public boolean receberRespostaMensagem() throws JsonMappingException, JsonProcessingException, IOException {
		Empresa sessaoEmpresa = new Empresa();
		InputStreamReader input = new InputStreamReader(clienteSocket.getInputStream());
		BufferedReader reader = new BufferedReader(input);
		
		ObjectMapper mapper = new ObjectMapper(); 
		Map<String, Object> data = mapper.readValue(reader.readLine(), new TypeReference<Map<String, Object>>() {});
		
		System.out.println("Entrada: [" +data.toString()+ "]");
		String op = data.get("status").toString();
		
		if(op.equals("201") || op.equals("200")) {
			return true;
			
		}else {
			JOptionPane.showMessageDialog(null, data.get("mensagem").toString(), "Realizar login", JOptionPane.WARNING_MESSAGE);
			return false;
		}
	}
	
	
	
	public boolean cadastrarEmpresa(Empresa user) throws IOException {
		
		Empresa cadastro = user;	
		Empresa sessaoEmpresa;
		String dado; 
		
	
		
		enviarJsonCadastro(cadastro, "cadastrarEmpresa");

		sessaoEmpresa = receberRespostaServidor();
		
		if(sessaoEmpresa!=null) 
		{
			cadastro.setToken(sessaoEmpresa.getToken());
			return true;
		}else {
			return false;
		}
		
		}
	
	
	
	public boolean deletarEmpresa(Empresa user) throws IOException, JSONException {
	
		
		enviarJsonLeitura(user, "apagarEmpresa");
		
		if(receberRespostaServidor()!=null) {
			
			return true;
		}else {
			return false;
		}
		
	}
	
	public Empresa realizarLogin(Empresa user) throws IOException {
		String email = null, senha=null; 
		Empresa sessaoEmpresa = new Empresa();

		email = user.getEmail();

		senha = user.getSenha();
		
		enviarJsonLogin(email,senha);
		
		Empresa sessaoToken = receberRespostaServidor();
		
		
		if(sessaoToken!=null) {
			
			sessaoEmpresa.setToken(sessaoToken.getToken());
			sessaoEmpresa.setEmail(email);
			sessaoEmpresa.setSenha(senha);
			return sessaoEmpresa;
			
		}else {
			return null;
		}
	}
	
	public boolean atualizarEmpresa(Empresa user) throws IOException {
		
		System.out.println(user.getToken());
		enviarJsonCadastro(user, "atualizarEmpresa");
		
		if(receberRespostaServidor()!=null) {
			return true;
		}
		
		return false;
	}
	public Empresa visualizarEmpresa(Empresa sessao) throws IOException, JSONException {
		System.out.println("Por favor digite o email que deseja visualizar");
		String email =sessao.getEmail();
		
		
		enviarJsonLeitura(sessao,"visualizarEmpresa");
		
		Empresa novaEmpresa = receberRespostaServidor();
		
		if(novaEmpresa!=null) {
		 return novaEmpresa;
		}else {
			return null;
		}
		
	}
	
	
	private void enviarJsonLeitura(Empresa sessao, String op ) throws IOException, JSONException {
	
			PrintStream saida  = new PrintStream (clienteSocket.getOutputStream());
			JSONObject json = new JSONObject();
			json.put("type", "CONNECT");
			
			String myString = new JSONObject()
					.put("operacao", op)
					.put("email", sessao.getEmail())
					.put("token", sessao.getToken())
					.toString(); 
			
			System.out.println("Saida: [" +myString + "]");
			
			   saida.println(myString);
		
	}
	
	
	

	
	
	private void enviarJsonCadastro(Empresa dados,String operacao) {
	try {
		PrintStream saida  = new PrintStream (clienteSocket.getOutputStream());
		
		JSONObject json = new JSONObject();
		json.put("type", "CONNECT");
		
		String myString = new JSONObject()
				.put("operacao", operacao)
				.put("email", dados.getEmail())
				.put("senha", dados.getSenha())
				.put("cnpj", dados.getCnpj())
				.put("razaoSocial",dados.getRazaoSocial())
				.put("descricao", dados.getDescricao())
				.put("ramo", dados.getRamo())
				.put("token", dados.getToken())
				.toString(); 
		
		System.out.println("Saida: [" +myString + "]");
		
		   saida.println(myString);
	}catch(Exception ex) {
		System.out.print(ex);
	}
	} 
	
	private Empresa receberRespostaServidor() throws IOException {
	
			
			Empresa sessaoEmpresa = new Empresa();
			InputStreamReader input = new InputStreamReader(clienteSocket.getInputStream());
			BufferedReader reader = new BufferedReader(input);
			
			ObjectMapper mapper = new ObjectMapper(); 
			Map<String, Object> data = mapper.readValue(reader.readLine(), new TypeReference<Map<String, Object>>() {});
			
			System.out.println("Entrada: [" +data.toString()+ "]");
				String op = data.get("status").toString();
			
			if(op.equals("201") || op.equals("200")) {
				
				
				if(data.get("operacao").toString().equals("cadastrarEmpresa") || data.get("operacao").toString().equals("loginEmpresa") ) {
					
					System.out.println("Operação realizada com sucesso");
					
					UUID token =   UUID.fromString ( data.get("token").toString());
					
					sessaoEmpresa.setToken(token);
					return sessaoEmpresa;
				}else 
				if(data.get("operacao").toString().equals("visualizarEmpresa")){
					
					Empresa visuEmpresa = new Empresa();
			
					sessaoEmpresa.setRazaoSocial(data.get("razaoSocial").toString());
					sessaoEmpresa.setCnpj(data.get("cnpj").toString());
					sessaoEmpresa.setSenha( data.get("senha").toString());
					sessaoEmpresa.setDescricao(data.get("descricao").toString());
					sessaoEmpresa.setRamo(data.get("ramo").toString());
					
					return sessaoEmpresa;
				}else if(data.get("operacao").toString().equals("atualizarEmpresa")){
					return sessaoEmpresa;
				}
				
			}else {
				JOptionPane.showMessageDialog(null, data.get("mensagem").toString(), "Realizar login", JOptionPane.WARNING_MESSAGE);
				return null;
				
			}
			
			
		
		return sessaoEmpresa;
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
			
			System.out.println("Saida: [" +myString + "]");
			
			   saida.println(myString);
		}catch(Exception ex) {
			System.out.print(ex);
		}
		
	}

}
