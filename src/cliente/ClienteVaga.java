package cliente;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import entities.Vaga;

public class ClienteVaga extends Cliente {
	
	private Socket clienteSocket = super.clienteSocket ;
	private static Scanner leia  = new Scanner(System.in);	
	
	public void cadastrarVagas() {
		
		enviarJsonCadastroAtualizacao(cadastrarAtualizarJson(), "cadastrarVaga");
		receberRespostaServidor();
	}
	public void atualizarVagas() {
		enviarJsonCadastroAtualizacao(cadastrarAtualizarJson(), "cadastrarVaga");
		receberRespostaServidor();
	}
	public void visualizarVagas() {
		//{"operacao":"visualizarVaga","idVaga":0,"email":"xx@xxx.xxx","token": "UUID"}
		enviarJsonVisualizarVaga("visualizarVaga");
		receberRespostaServidorVisualizar() ;
	}
	public void apagarVaga() {
		//{"operacao": "apagarVaga","idVaga":0,"email":"xx@xxx.xxx","token": "UUID"}
		enviarJsonVisualizarVaga("apagarVaga");
		receberRespostaServidor();
	}
	
	private void enviarJsonVisualizarVaga(String operacao) {
		try {
			PrintStream saida  = new PrintStream (super.clienteSocket.getOutputStream());

			
				String myString = new JSONObject()
				.put("operacao", operacao)
				.put("idVaga", 0)
				.put("email",super.sessaoEmpresa.getEmail())
				.put("token",super.sessaoEmpresa.getToken())
				.toString(); 
		
		System.out.println("Saida: [" +myString + "]");
		
		saida.println(myString);
		   
		} catch (Exception e) {
			e.toString();
		}
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
					.put("competencias", dataVaga.getCompetencias())
					.put("token",super.sessaoEmpresa.getToken())
					.toString(); 
			
			System.out.println("Saida: [" +myString + "]");
			
			saida.println(myString);
			   
			} catch (Exception e) {
				e.toString();
			}
	}
	
	private Vaga cadastrarAtualizarJson() {
		Vaga vaga = new Vaga();
		
		String dado; 
		
		

		
		System.out.println("Por favor digite a descriçao da vaga:");
		dado = leia.nextLine();
		vaga.setDescricao(dado);
		
		System.out.println("Por favor digite o estado da vaga:");
		dado = leia.nextLine();
		vaga.setEstado(dado);
		
		
		System.out.println("Por favor digite a descricao da empresa:");
		dado = leia.nextLine();
		vaga.setDescricao(dado);
		
		
		
		System.out.println("Por favor digite a faixa salarial da vaga:");
		float faixa = leia.nextFloat();
		vaga.setFaixaSalarial(faixa);
		
		vaga.setCompetencias(selecionarCompetencia());
		
		
		
		
		return vaga;
	}
	
	private ArrayList<String> selecionarCompetencia() {
		System.out.println("por favor selecione a competencia que vc deseja: ");
		ArrayList<String> comps = new ArrayList<String>();
		int op = 0; 
		String [] competencias = super.competenciasNomes;
		do { op = 1;
			for(String comp :competencias) {
			
					System.out.println(op + " : " + comp);

				
				op++;
			}
			System.out.println("0 : Sair da seleção");
			op = leia.nextInt();
			if(op!=0) {
				String sr = super.competenciasNomes[op-1] ; 
			
				comps.add(sr); 
				
			}
			
		}while(op!=0);
		
		return comps;
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
	
private void receberRespostaServidorVisualizar() {
	try {
		InputStreamReader input = new InputStreamReader(clienteSocket.getInputStream());
		BufferedReader reader = new BufferedReader(input);
		ObjectMapper mapper = new ObjectMapper(); 
		Map<String, Object> data = mapper.readValue(reader.readLine(), new TypeReference<Map<String, Object>>() {});
		System.out.println("Entrada: [" +data.toString()+ "]");
		String op = data.get("status").toString();
		
//		{
//			  "operacao": "visualizarVaga",
//			  "faixaSalarial":12345,
//			  "descricao":"xxxxx",
//			  "estado":"xxxxx",
//			  "competencias": ["xxxx","xxxx","xxxx"],
//			   "status": "201"
//			}
		if(op.equals("201") || op.equals("200")) {			
			if(data.get("operacao").toString().equals("cadastrarCompetenciaExperiencia") || data.get("operacao").toString().equals("atualizarCompetenciaExperiencia") ) {
		
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
}
