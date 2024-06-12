package cliente;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import entities.Vaga;

public class ClienteVaga extends Cliente {
	
	private Socket clienteSocket = super.clienteSocket ;
	private static Scanner leia  = new Scanner(System.in);	
	
	private static List<Vaga> vagas = new ArrayList<>();
	
	public ClienteVaga() {
		listarVagas();
	}
	
	public void listarVagas() {
		enviarJsonListarVagas();
		this.vagas = receberRespostaListarVagas();
		
	}
	
	
	
	public void cadastrarVagas() {
		
		enviarJsonCadastroAtualizacao(cadastrarAtualizarJson("cadastrarVaga"), "cadastrarVaga");
		receberRespostaServidor();
		this.listarVagas();
	}
	public void atualizarVagas() {
		enviarJsonCadastroAtualizacao(cadastrarAtualizarJson("atualizarVaga"),"atualizarVaga");
		receberRespostaServidor();
		this.listarVagas();
	}
	public void visualizarVagas() {
		
		enviarJsonVisualizarVaga("visualizarVaga");
		receberRespostaServidorVisualizar() ;
		
	}
	public void apagarVaga() {
		//{"operacao": "apagarVaga","idVaga":0,"email":"xx@xxx.xxx","token": "UUID"}
		enviarJsonVisualizarVaga("apagarVaga");
		receberRespostaServidor();
		this.listarVagas();
	}
	
	private int enviarJsonVisualizarVaga(String operacao) {
		int op = selecionarVaga(operacao);
		String opString = String.valueOf(op);
		try {
			PrintStream saida  = new PrintStream (super.clienteSocket.getOutputStream());
				String myString = new JSONObject()
				.put("operacao", operacao)
				.put("idVaga", opString)
				.put("email",super.sessaoEmpresa.getEmail())
				.put("token",super.sessaoEmpresa.getToken())
				.toString(); 
		
		System.out.println("Saida: [" +myString + "]");
		
		saida.println(myString);
		return op;
		} catch (Exception e) {
			e.toString();
		}
		System.out.println("Opcao "+ op );
		return op;
	}
	
	
	private void enviarJsonListarVagas() {
		
		
		
		try {
			PrintStream saida  = new PrintStream (super.clienteSocket.getOutputStream());
			String myString = new JSONObject()
					.put("operacao","listarVagas")
					.put("email", super.sessaoEmpresa.getEmail())
					.put("token",super.sessaoEmpresa.getToken())
					.toString();
			System.out.println("Saida: [" +myString + "]");
			
			saida.println(myString);
			
		} catch (JSONException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private List<Vaga> receberRespostaListarVagas() {
		
		
		try {
			InputStreamReader input = new InputStreamReader(clienteSocket.getInputStream());
		BufferedReader reader = new BufferedReader(input);
		
		ObjectMapper mapper = new ObjectMapper(); 
		Map<String, Object> data = mapper.readValue(reader.readLine(), new TypeReference<Map<String, Object>>() {});
		
		System.out.println("Entrada: [" +data.toString()+ "]");
		
		
			String competencias = new JSONObject().put("vagas",data.get("vagas").toString()).toString();
			JSONArray jsonArr = new JSONArray(data.get("vagas").toString());
			List<Vaga> vagas = new ArrayList<>();
			
			for (int i = 0; i < jsonArr.length(); i++)
		        {
				
				 	Vaga vagaTemp = new Vaga();
		            JSONObject jsonObj = jsonArr.getJSONObject(i);
		            vagaTemp.setIdVaga(  Integer.parseInt(jsonObj.get("idVaga").toString()));
		            vagaTemp.setNome( jsonObj.get("nome").toString());
		            vagas.add(vagaTemp);
		            
		        }
			 return vagas;
			 
			
		} catch (JSONException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
		
		
	}
	
	private void enviarJsonCadastroAtualizacao(Vaga dataVaga, String operacao) {
		try {
				PrintStream saida  = new PrintStream (super.clienteSocket.getOutputStream());
	
				
					String myString = new JSONObject()
					.put("operacao", operacao)//operacao cadastrarCompetenciaExperiencia
					.put("nome",dataVaga.getNome())
					.put("faixaSalarial", dataVaga.getFaixaSalarial())
					.put("descricao",dataVaga.getDescricao())
					.put("estado", dataVaga.getEstado())
					.put("email", super.sessaoEmpresa.getEmail())
					.put("competencias", selecionarCompetencia())
					.put("token",super.sessaoEmpresa.getToken())
					.put("idVaga", String.valueOf(dataVaga.getIdVaga())  )
					.toString(); 
			
			System.out.println("Saida: [" +myString + "]");
			
			saida.println(myString);
			   
			} catch (Exception e) {
				e.toString();
			}
	}
	
	
	
	private void enviarJsonVisualizarVaga(String operacao, int op ) {
		
		try {
			PrintStream saida  = new PrintStream (super.clienteSocket.getOutputStream());
				String myString = new JSONObject()
				.put("operacao", operacao)
				.put("idVaga", String.valueOf(op))
				.put("email",super.sessaoEmpresa.getEmail())
				.put("token",super.sessaoEmpresa.getToken())
				.toString(); 
		
		System.out.println("Saida: [" +myString + "]");
		
		saida.println(myString);

		} catch (Exception e) {
			e.toString();
		}
		System.out.println("Opcao "+ op );
	}
	
	
	
	private Vaga cadastrarAtualizarJson(String op) {
		Vaga vaga = new Vaga();
		int idVaga = 0;
		String dado ="   "; 
		
		
		try {
			
			if(op.equals("atualizarVaga")) {
				int i = 0; 
				idVaga = selecionarVaga("visualizarVaga");
				
				enviarJsonVisualizarVaga("visualizarVaga",idVaga);
				
				System.out.println("id vaga " + idVaga);
				
				vaga = receberRespostaServidorVisualizar("  ");
				
			}
			
			vaga.setIdVaga(idVaga);
			
			Scanner ler = new Scanner(System.in);
			
			System.out.println("Id vaga: " + vaga.getIdVaga());
			
			
			System.out.println("Por favor digite o nome da vaga:");
			dado = ler.nextLine();
			if(dado.isBlank()==false) {vaga.setNome(dado);}
		
			
			dado = selecionarEstado();
			if(dado.isBlank()==false) { vaga.setEstado(dado);}
			
			System.out.println("Por favor digite a descricao da empresa:");
			dado = ler.nextLine();
			if(dado.isBlank()==false) {vaga.setDescricao(dado);}
					
			System.out.println("Por favor digite a faixa salarial da vaga:");
			float faixa = ler.nextFloat();
			if(dado.isBlank()==false) {vaga.setFaixaSalarial(faixa);}
			
		}catch(Exception ex) {
			System.out.println(ex);
		}		
		return vaga;
	}
	
	private String selecionarEstado() {
		
		
		String dado;
		do{
			System.out.println("Por favor digite o estado da vaga:\n1-disponível \n2-divulgavel");
			switch(leia.nextInt()) {
				case 1:
					dado = "disponível";
				break;
				case 2:
					dado = "divulgavel";
				break;
				default: 
					System.out.println("Opção invalida");
					dado =  "Opcaoinvalida";
				break;
			}
		}while(dado.equals("Opcaoinvalida"));
		return dado;
	}
	private JSONArray selecionarCompetencia() {
		System.out.println("por favor selecione a competencia que vc deseja: ");
		
		
		
		
		JSONArray jArry = new JSONArray();
		
		int op = 0; 
		String [] competencias = super.competenciasNome;
		do {	 
			op = 1;
			for(String comp :competencias) {
					System.out.println(op + " : " + comp);
					
				op++;
			}
			System.out.println("0 : Sair da seleção");
			op = leia.nextInt();
			if(op!=0) {
				
				String sr = super.competenciasNome[op-1] ; 
				jArry.put(sr);
			
				
			}
			
		}while(op!=0);
		
		return jArry;
	}
	
	private void receberRespostaServidor() {
		try {
			InputStreamReader input = new InputStreamReader(clienteSocket.getInputStream());
			BufferedReader reader = new BufferedReader(input);
			
			ObjectMapper mapper = new ObjectMapper(); 
			Map<String, Object> data = mapper.readValue(reader.readLine(), new TypeReference<Map<String, Object>>() {});
			
			System.out.println("Entrada: [" +data.toString()+ "]");
			
			String op = data.get("status").toString();
			
			if(op.equals("201") || op.equals("200")) {
				
				if(data.get("operacao").toString().equals("cadastrarCompetenciaExperiencia") || data.get("operacao").toString().equals("atualizarCompetenciaExperiencia") ) {
				System.out.println(data.get("mensagem".toString()));
					
				}else 
				if(data.get("operacao").toString().equals("visualizarEmpresa")){
	
				}
				
			}else {
				
				System.out.println(data.get("mensagem").toString());
				
			}
			
			
		}catch(Exception ex) {
			
		}
		
	}

	
	private Vaga receberRespostaServidorVisualizar(String op2) {
		Vaga vagaRet = new Vaga();
		try {
			
			
			InputStreamReader input = new InputStreamReader(clienteSocket.getInputStream());
			BufferedReader reader = new BufferedReader(input);
			ObjectMapper mapper = new ObjectMapper(); 
			Map<String, Object> data = mapper.readValue(reader.readLine(), new TypeReference<Map<String, Object>>() {});
			System.out.println("Entrada: [" +data.toString()+ "]");
				String op = data.get("status").toString();
					if(op.equals("201") || op.equals("200")) {
						
						
						
						vagaRet.setEstado(data.get("estado").toString());
						vagaRet.setCompetencias(data.get("competencias").toString());
						vagaRet.setDescricao( data.get("descricao").toString());
						vagaRet.setFaixaSalarial(Float.parseFloat(data.get("faixaSalarial").toString()));
					
					}else {
						System.out.println("Erro: "+ data.get("mensagem").toString() );
					}
				
			return vagaRet;
		}
		catch(Exception ex ) {
			System.out.println(ex);
		}
		return vagaRet;
		
	}
	
private void receberRespostaServidorVisualizar() {
	
	try {
		
		InputStreamReader input = new InputStreamReader(clienteSocket.getInputStream());
		BufferedReader reader = new BufferedReader(input);
		ObjectMapper mapper = new ObjectMapper(); 
		Map<String, Object> data = mapper.readValue(reader.readLine(), new TypeReference<Map<String, Object>>() {});
		System.out.println("Entrada: [" +data.toString()+ "]");
		String op = data.get("status").toString();
		
		if(op.equals("201") || op.equals("200")) {			
			if(data.get("operacao").toString().equals("cadastrarCompetenciaExperiencia") || data.get("operacao").toString().equals("atualizarCompetenciaExperiencia") ||data.get("operacao").toString().equals("visualizarVaga") ) {
		
				System.out.println("estado: "+ data.get("estado").toString());
				System.out.println("competencias: "+ data.get("competencias").toString());
				System.out.println("descricao: "+ data.get("descricao").toString());
				System.out.println("Faixa salarial: "+ data.get("faixaSalarial").toString());
				
			
				
				
			}else 
			if(data.get("operacao").toString().equals("visualizarEmpresa")){

			}
			
		}else {
			
			System.out.println(data.get("mensagem").toString());
			
		}
		
		
	}catch(Exception ex) {
		
	}

	}

	private static List<String> converterJsonArrayToList (String data) {
		
		data = data.replace("[", "").replace("]", "");
	    String[] array = data.split(", ");
	    List<String> list = new ArrayList<>();
	    for (String s : array) {
	        list.add(s);
	    }
	    return list;
	}
	
	private int selecionarVaga(String operacao) {
		if(operacao.equals("apagarVaga"))System.out.println("Por escolha qual vaga deseja apagar:");
		else System.out.println("Por escolha qual vaga deseja visualizar:");
			
		
		for(Vaga vaga : this.vagas) {
			System.out.println(vaga.getIdVaga() +"-"+ vaga.getNome());
		}
		System.out.println("Digite o numero da vaga:");
		return leia.nextInt();
	}

}
