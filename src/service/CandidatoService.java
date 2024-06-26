package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import entities.Candidato;
import entities.Empresa;

public class CandidatoService {

	
	public static Socket clienteSocket = null;
	private Candidato sessao;
	
	public CandidatoService(Socket ss,Candidato sessao) {
		this.sessao = sessao;
		this.clienteSocket = ss;
	}
	
	public CandidatoService(Socket ss) {
		this.clienteSocket = ss;
	}
	
	
	
	public List<Empresa> receberMensagem() throws JSONException, IOException {
		//{"operacao":"receberMensagem", "email":"xxxx@xsss.com","token":"UUID"}
		
			JSONObject json = new JSONObject();
			json.put("type", "CONNECT");
			Scanner leia = new Scanner(System.in);
		
			PrintStream saida  = new PrintStream (clienteSocket.getOutputStream());
		
		
			
			String myString = new JSONObject()
					.put("operacao", "receberMensagem")
					.put("email", this.sessao.getEmail())
					.put("token",sessao.getToken())
					.toString(); 
			
			System.out.println(myString);
			saida.println(myString);
			return receberRespostaEmpresaMensagem();
	}
	
	public List<Empresa> receberRespostaEmpresaMensagem() throws JsonMappingException, JsonProcessingException, IOException, JSONException {
		
	
		InputStreamReader input = new InputStreamReader(clienteSocket.getInputStream());
		BufferedReader reader = new BufferedReader(input);
		ObjectMapper mapper = new ObjectMapper(); 
		Map<String, Object> data = mapper.readValue(reader.readLine(), new TypeReference<Map<String, Object>>() {});
		
		System.out.println("Entrada: [" +data.toString()+ "]");
		String op = data.get("status").toString();
		
		List<Empresa> empresasContato = new ArrayList<>();
		
		//{"operacao":"receberMensagem","empresas":[{"nome":"xxxx","email":"yyyy@yyy.com","ramo":"zzzz"},],"status":201}
		
		if(op.equals("201") || op.equals("200")) {
			JSONArray jsonArr = new JSONArray(data.get("empresas").toString());
			for(int i =0 ; i<jsonArr.length();i++) {
				JSONObject jsonObj = new JSONObject();
				jsonObj = jsonArr.getJSONObject(i);
				
				Empresa emp = new Empresa();
				emp.setRazaoSocial(jsonObj.getString("nome"));
				emp.setEmail(jsonObj.getString("email"));
				emp.setRamo(jsonObj.getString("ramo"));
				empresasContato.add(emp);
				
			}
			
			return empresasContato;
			
		}else {
			JOptionPane.showMessageDialog(null, data.get("mensagem").toString(), "Realizar login", JOptionPane.WARNING_MESSAGE);
			return null;
		}
		
	}
	
	public boolean atualizarCadastro(Candidato user) throws JSONException, IOException {
		
			JSONObject json = new JSONObject();
			json.put("type", "CONNECT");
			Scanner leia = new Scanner(System.in);
			
			PrintStream saida  = new PrintStream (clienteSocket.getOutputStream());
			
				String nome = user.getNome();
				String senha= user.getSenha();
				String email = user.getEmail();
				
				String myString = new JSONObject()
						.put("operacao", "atualizarCandidato")
						.put("nome", nome)
						.put("email", email)
						.put("senha", senha)
						.put("token",sessao.getToken())
						.toString(); 
				
				System.out.println(myString);

			    saida.println(myString);
			
			
			InputStreamReader input = new InputStreamReader(clienteSocket.getInputStream());
			BufferedReader reader = new BufferedReader(input);
			
			ObjectMapper mapper = new ObjectMapper();  
			Map<String, Object> userData = mapper.readValue(reader.readLine(), new TypeReference<Map<String, Object>>() {});
			System.out.println("Entrada => ["+userData.toString()+"]");
			String op = userData.get("status").toString();
			
			if(op.equals("201") || op.equals("200")) {
				
				System.out.println("Atualização realizada com sucesso");
				return true;
				
			}else if(op.equals("404")) {
				
				System.out.println(userData.get("mensagem").toString());
				return false;
				
			}else {
				
				System.out.println(userData.get("mensagem").toString());
				return false;
				
			}
			
		
	}
	
	public boolean deletarUsuario() {
		
		try 
		{
			JSONObject json = new JSONObject();
			json.put("type", "CONNECT");
			Scanner leia = new Scanner(System.in);
			
			PrintStream saida  = new PrintStream (clienteSocket.getOutputStream());
			
				
				String email = sessao.getEmail(); 
				String myString = new JSONObject().put("operacao", "apagarCandidato").put("email", email).put("token", sessao.getToken()).toString(); 
				
				System.out.println(myString);
			    saida.println(myString);
			
			
		}
		catch(Exception ex)
		{
			System.out.println(ex);	
			return false;
		}
		
		
		
		try {
			
			InputStreamReader input = new InputStreamReader(clienteSocket.getInputStream());
			BufferedReader reader = new BufferedReader(input);
			
			ObjectMapper mapper = new ObjectMapper(); 
			Map<String, Object> userData = mapper.readValue(reader.readLine(), new TypeReference<Map<String, Object>>() {});
			System.out.println("Entrada => ["+userData.toString()+"]");
			String op = userData.get("status").toString();
			
			if(op.equals("201") || op.equals("200")) {
				
				System.out.println("Apagar candidado realizado com sucesso");
				return true;
			}else if(op.equals("404")) {
				
				System.out.println(userData.get("mensagem").toString());
				return false;
			}
			
		}catch(Exception ex) {
			System.out.print(ex);
		return false;
	}
		return false;
	}
	
	public Candidato visualizarUsuario() {
		try {
			Candidato user = new Candidato();
			JSONObject json = new JSONObject();
			json.put("type", "CONNECT");
			
		
			
			PrintStream saida  = new PrintStream (clienteSocket.getOutputStream());
			String email = sessao.getEmail();
			String myString = new JSONObject()
					.put("operacao","visualizarCandidato")
					.put("email", email)
					.put("token",sessao.getToken()).toString(); 
			
			System.out.println(myString);
			saida.println(myString);
			
			
			try {
				InputStreamReader input = new InputStreamReader(clienteSocket.getInputStream());
				BufferedReader reader = new BufferedReader(input);
				
				ObjectMapper mapper = new ObjectMapper(); 
				Map<String, Object> userData = mapper.readValue(reader.readLine(), new TypeReference<Map<String, Object>>() {});
				System.out.println("Entrada => ["+userData.toString()+"]");
				String op = userData.get("status").toString();
				if(op.equals("201")||op.equals("200")) 
				{
					System.out.println("nome: " + userData.get("nome").toString() +"\nsenha: " + userData.get("senha").toString());
					user.setEmail(sessao.getEmail());
					user.setSenha(userData.get("senha").toString());
					user.setNome(userData.get("nome").toString());
					return user;
				}
				else if(op.equals("404")) 
				{
					System.out.println(userData.get("mensagem").toString());
					return null;
				}
				
			}catch(Exception ex) {
				System.out.println(ex);
				return null;
			}
				
				
		}catch(Exception ex)
		{
			System.out.println(ex);
			return null;
		}
		return null;
		
		
	}
	
	public Candidato cadastrarUsuario(Candidato user) throws JsonMappingException, JsonProcessingException, IOException, JSONException {
		
		Candidato sessao = new Candidato();
		
			JSONObject json = new JSONObject();
			json.put("type", "CONNECT");
			Scanner leia = new Scanner(System.in);
			
			
			String nome = user.getNome();
			String email = user.getEmail();
			String senha = user.getSenha();
			
				PrintStream saida  = new PrintStream (clienteSocket.getOutputStream());
				String myString = new JSONObject().put("operacao", "cadastrarCandidato").put("nome", nome).put("email", email).put("senha", senha).toString(); 
				System.out.println(myString);		
			    saida.println(myString);
			    
			    
			sessao=user;
			
			InputStreamReader input = new InputStreamReader(clienteSocket.getInputStream());
			BufferedReader reader = new BufferedReader(input);
			ObjectMapper mapper = new ObjectMapper(); 
			Map<String, Object> userData = mapper.readValue(reader.readLine(), new TypeReference<Map<String, Object>>() {});
			System.out.println("Entrada => ["+userData.toString()+"]");
			String op = userData.get("status").toString();
			if(op.equals("422")) {
				
				JOptionPane.showMessageDialog(null, userData.get("mensagem").toString(), "Conectar", JOptionPane.WARNING_MESSAGE);
				 return null;
				 
			}else if(op.equals("404")) {
				
				JOptionPane.showMessageDialog(null, userData.get("mensagem").toString(), "Conectar", JOptionPane.WARNING_MESSAGE);
				 return null;
				
			}else {
				
				
				UUID token = UUID.fromString(userData.get("token").toString()); ;
				sessao.setToken(token);
				return sessao;
				
			}
			
	

	}
	
	public Candidato realizarLogin(Candidato user) throws JSONException, IOException {
		
			Candidato sessao = new Candidato();
		
			JSONObject json = new JSONObject();
			json.put("type", "CONNECT");
			
			
			
			
			
				PrintStream saida  = new PrintStream (clienteSocket.getOutputStream());
				
				String email = user.getEmail();
				
				String senha= user.getSenha();
				
				
				
				
				String myString = new JSONObject().put("operacao","loginCandidato").put("email", email).put("senha", senha).toString(); 
				System.out.println(myString);
				
				saida.println(myString);
				
				
		
				InputStreamReader input = new InputStreamReader(clienteSocket.getInputStream());
				BufferedReader reader = new BufferedReader(input);
				
				ObjectMapper mapper = new ObjectMapper(); 
				Map<String, Object> userData = mapper.readValue(reader.readLine(), new TypeReference<Map<String, Object>>() {});
				System.out.println("Entrada => ["+userData.toString()+"]");
				String op = userData.get("status").toString();
				if(op.equals("401")) 
				{
					System.out.println(userData.get("mensagem").toString());
					JOptionPane.showMessageDialog(null, userData.get("mensagem").toString(), "Login", JOptionPane.WARNING_MESSAGE);
					return null;
					
				}
				else if(op.equals("200") || op.equals("201")) 
				{

					UUID token = UUID.fromString(userData.get("token").toString());
					if(sessao == null)sessao = new Candidato();
					
					sessao.setEmail(email);
					sessao.setSenha(senha);
					sessao.setToken(token);
					
					//JOptionPane.showMessageDialog(null, "login realizado com sucesso", "Login", JOptionPane.WARNING_MESSAGE);
					
					return sessao;
				}
				else 
				{
					return sessao;
				}
	}
	
	
	
}
