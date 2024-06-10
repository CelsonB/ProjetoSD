package teste;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.json.JSONArray;


import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Teste {
	protected static String [] competenciasNomes  = {"python" , "c#", "c++"};
	public static Scanner leia = new Scanner (System.in);
	public static void main(String[] args) throws JSONException, JsonMappingException, JsonProcessingException {
		// TODO Auto-generated method stub
		
		
		
		JSONArray testeJson = selecionarCompetencia();
		
		System.out.println(testeJson.toString());
		
		JSONObject jsonObjectTeste = new JSONObject();
		jsonObjectTeste.put("competencias", testeJson);
		
		System.out.println(jsonObjectTeste.toString());
		String str = jsonObjectTeste.toString();
		System.out.println(str);
		
		ObjectMapper mapper = new ObjectMapper();  
		Map<String, Object> stringArray = mapper.readValue(str, new TypeReference<Map<String, Object>>() {}); 
		
		System.out.println("String array dps do mapper " + stringArray.get("competencias"));
		
		List<String> novaLista = converterStringArrayToMap(stringArray.get("competencias").toString());
		
		for(String str2 : novaLista) {
			System.out.println("Deppois do passer: " + str2);
		}
		//System.out.println("Deppois do passer: " + novaLista.toString());
		//SONArray testeJson2 = new JSONArray(stringArray.get("competencias"));
		//System.out.println(testeJson2.toString());
	}

	
	
	private static JSONArray selecionarCompetencia() {
		System.out.println("por favor selecione a competencia que vc deseja: ");
		JSONArray jArry = new JSONArray();
		
		int op = 0; 
		String [] competencias = competenciasNomes;
		do {	 
			op = 1;
			for(String comp :competencias) {
					System.out.println(op + " : " + comp);
					
				op++;
			}
			System.out.println("0 : Sair da seleção");
			op = leia.nextInt();
			if(op!=0) {
				
				String sr = competenciasNomes[op-1] ; 
				jArry.put(sr);
			
				
			}
			
		}while(op!=0);
		
		return jArry;
	}
	
	private static List<String> converterStringArrayToMap (String data) {
		
	    	data = data.replace("[", "").replace("]", "");
	        String[] array = data.split(", ");
	        List<String> list = new ArrayList<>();
	        for (String s : array) {
	            list.add(s);
	        }
	        return list;
	}
	
}



