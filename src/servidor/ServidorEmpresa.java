package servidor;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Map;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import dao.EmpresaDao;
import entities.Empresa;

public class ServidorEmpresa {
	private UUID token = null;
	public ServerSocket servidorSocket;
	public Socket ss;
	
	public UUID getToken() {
		return token;
	}
	public static void setToken(UUID token) {
		token = token;
	}

	private Empresa sessao = null;
	
	public ServidorEmpresa(ServerSocket servidorSocket2, Socket ss2) {
		this.servidorSocket = servidorSocket2;
		this.ss = ss2;
	}
	
	 public void cadastrarEmpresa(Map<String, Object> userData) throws SQLException, IOException, JSONException {
		 
	
			
		EmpresaDao bd = new EmpresaDao();
		
		sessao = new Empresa(
				//userData.get("nome").toString(),
				userData.get("email").toString(),
				userData.get("senha").toString(),
				userData.get("razaoSocial").toString(),
				userData.get("descricao").toString(),
				userData.get("ramo").toString(),
				userData.get("cnpj").toString()
				);
		
	
		try {
			validarEmail(sessao);
			bd.cadastrarEmpresa(sessao);
			respostaServidor(userData.get("operacao").toString());
			
		}catch(Exception ex) {
			respostaExcecao( ex, userData.get("operacao").toString(),"422");
		}
		
		
		
	}
	 
	 public void realizarLogin(Map<String, Object> data) {
		 ResultSet rs = null; 
		 EmpresaDao bd = new EmpresaDao();
		 
		 
		 try {
			
			 
			rs =  bd.realizarLogin(data.get("email").toString(),data.get("senha").toString());
			if(rs == null) {
				throw new  EmailInvalidoException("email não encontrado");
			}else {
				respostaServidor(data.get("operacao").toString());
			}
			 
			 
		 }catch(Exception ex) {
			 respostaExcecao( ex, data.get("operacao").toString(), "401");
		 }
		 
		 
		 
		 
		 
		 
	 }
	 
	 public void visualizarEmpresa(Map<String, Object> data) throws EmailInvalidoException, SQLException, IOException, JSONException {
		 
		 ResultSet rs = null; 
		 EmpresaDao bd = new EmpresaDao();
		 Empresa resultadoPesquisa = new Empresa();
		 
		 try {
			 rs =  bd.Ler(data.get("email").toString());
				if(rs == null) {
					throw new  EmailInvalidoException("email não encontrado");
				}else {
					rs.next();
					resultadoPesquisa.setDescricao(rs.getString("descricao"));
					resultadoPesquisa.setEmail(rs.getString("email"));
					//resultadoPesquisa.setNome(rs.getString("nome"));
					resultadoPesquisa.setRamo(rs.getString("ramo"));
					resultadoPesquisa.setRazaoSocial(rs.getString("razao_social"));
					resultadoPesquisa.setSenha(rs.getString("senha"));
					
					System.out.println(resultadoPesquisa);
					
					respostaServidor(data.get("operacao").toString(), resultadoPesquisa);
				}
		 }catch(Exception ex) {
			 respostaExcecao(ex, data.get("operacao").toString(),"404");
		 }
		 
		 
		 
	 }
	 
	 public void atualizarEmpresa(Map<String, Object> data) {
		 //{"operacao": "atualizarEmpresa","email":"xx@xxx.xxx","razaoSocial":"xxxxxxx","cnpj":"12345678000100","senha":"xxx","descricao":"xxxxxx","ramo":"xxxxx"}
		 
		 ResultSet rs = null; 
		 EmpresaDao bd = new EmpresaDao();
		
		 Empresa atualizacaoEmpresa = new Empresa();
		 
		 atualizacaoEmpresa.setDescricao(data.get("descricao").toString());
		 atualizacaoEmpresa.setEmail(data.get("email").toString());
		 //atualizacaoEmpresa.setNome(data.get("nome").toString());
		 atualizacaoEmpresa.setRamo(data.get("ramo").toString());
		 atualizacaoEmpresa.setRazaoSocial(data.get("razaoSocial").toString());
		 atualizacaoEmpresa.setSenha(data.get("senha").toString());
			
		 
		 try {
			 validarEmail(atualizacaoEmpresa);
				if(bd.atualizarCadastro(atualizacaoEmpresa) == false) {
					throw new  EmailInvalidoException("email não encontrado");
				}else {
					
					respostaServidor(data.get("operacao").toString());
				}
		 }catch(Exception ex) {
			 respostaExcecao(ex, data.get("operacao").toString(),"404");
		 }
		 
	 }
	 public void logout() {
		
			 try {
				respostaServidor("logout");
			} catch (Exception e) {
			System.out.println(e);
			} 
		 
		//	 System.out.println("empresa: você não está logado");
		 
		 
		 
	 }
	 public void apagarEmpresa(Map<String, Object> data) {
		 ResultSet rs = null; 
		 EmpresaDao bd = new EmpresaDao();
		
		 try {
			 if(bd.apagarEmpresa(data.get("email").toString())) {
				 
				 respostaServidor(data.get("operacao").toString());
				 
			 }else {
				throw new  EmailInvalidoException("email não encontrado");
				 
			 }
		 }catch(Exception ex) {
			 respostaExcecao(ex, data.get("operacao").toString(), "404");
		 }
	 }
	 
	 private boolean validarEmail(Empresa cadastro) throws SenhaInvalidaException, EmailInvalidoException {
			 if(!cadastro.getEmail().contains("@")) 
				{
				throw new EmailInvalidoException("Email não contem @");
				}
				else if(cadastro.getSenha().length()>8) {
				throw new SenhaInvalidaException("senha invalida");	
		
				}else {
					return true;
				}
	 }
	 
	  
	 private void respostaExcecao(Exception ex, String operacao, String numStatus)  {
		 String myString = null;
		 
		
		 try {
			 JSONObject json = new JSONObject();
			 json.put("type", "CONNECT");
			 
					PrintStream saida  = new PrintStream (ss.getOutputStream());
					myString = new JSONObject().put("operacao", operacao).put("status", numStatus).put("mensagem", ex.toString()).toString(); 
					saida.println(myString);


		 }catch(Exception exe) {
			 System.err.println(exe);
		 }
		
			
	 }
	
	 
	 
	 private void respostaServidor(String operacao ) throws JSONException, IOException {
		 token = UUID.randomUUID();
		 String myString = null;
		 PrintStream saida  = new PrintStream (ss.getOutputStream());
		
		 if(operacao.equals("cadastrarEmpresa") || operacao.equals("loginEmpresa") ) {
			 token = UUID.randomUUID();
			 String tokenString = "200";
			 
			 if(operacao.equals("cadastrarEmpresa")) {
				 tokenString = "201";
			 }
			 myString = new JSONObject()
					 .put("operacao", operacao)
					 .put("status",tokenString)
					 .put("token", token.toString())
					 .toString(); 
			 
			 saida.println(myString);
			 
		 } else  
		 if(operacao.equals("atualizarEmpresa" ) || operacao.equals("apagarEmpresa") ) 
		 {
			 myString = new JSONObject()
					 .put("operacao", operacao)
					 .put("status","201")
					 .toString(); 
			 
			 saida.println(myString);
		 }else 
			 if(operacao.equals("logout")){
				 token = null;	 
				 
			 myString = new JSONObject()
					 .put("operacao", operacao)
					 .put("status","204")
					 .toString(); 
			 
			 saida.println(myString);
		 }
		 
	 }
		 
		 private void respostaServidor(String operacao , Empresa resultado ) throws JSONException, IOException {
		 
			 token = UUID.randomUUID();
			 String myString = null;
			 PrintStream saida  = new PrintStream (ss.getOutputStream());
			 
			 if(operacao.equals("visualizarEmpresa")) {
				
				 myString = new JSONObject().put("operacao", operacao)
						 .put("status","201")
						 .put("razaoSocial", resultado.getRazaoSocial() )
						 .put("cnpj","xxxx")
						 .put("senha", resultado.getSenha())
						 .put("descricao", resultado.getDescricao())
						 .put("ramo", resultado.getRamo()
						  ).toString(); 
				 
				 saida.println(myString);
			 }
	 }
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 

}
