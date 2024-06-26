package teste;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.UUID;

import org.json.JSONArray;


import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dao.DaoCompetencia;

public class Teste {
	protected static String [] competenciasNomes  = {"python" , "c#", "c++"};
	public static Scanner leia = new Scanner (System.in);
	public static void main(String[] args) throws JSONException, JsonMappingException, JsonProcessingException {
		// TODO Auto-generated method stub
		
		
//		JSONObject obj = new JSONObject();
//		JSONArray jsonarry = new JSONArray();
//		
////		jsonarry.put("JS").put("Java").put("python");
////		
//// 		obj.put("competencias",jsonarry).put("tipo", "or");
////		
////		String myStr = new JSONObject()
////				.put("operacao", "filtrarVagas")
////				.put("filtros", obj)
////				.put("token", UUID.randomUUID())
////				.toString();	
////				
////		System.out.println(myStr);
//				
//		
//		String mystr  = "[{experiencia=3, competencia=c++}, {experiencia=3, competencia=c++}]";
//		DaoCompetencia bd = new DaoCompetencia();
//		ObjectMapper mapper = new ObjectMapper();  
//		
//		
//		//Map<String, Object> data = mapper.readValue(myStr, new TypeReference<Map<String, Object>>() {}); 
////		System.out.println("Primeiro map" + data.toString());
////		System.out.println("Apenas filtro" + data.get("filtros"));
//		
//	
//		
//		JSONArray jsonarray = new JSONArray(mystr);
//		
//		//Map<String, Object> comps = mapper.readValue(mystr, new TypeReference<Map<String, Object>>() {}); 
//		//List<String> competencias =  converterJsonArrayToList (comps.get("competencias").toString());
//	
//		
//		
//		for(int i =0 ; i< jsonarray.length();i++) {
//			System.out.println("Json array: " + jsonarray.getJSONObject(1).toString());
//		}
//		
//		PreparedStatement st = null;
//	//	String tipo = comps.get("tipo").toString();
//		
//
		
		
		
		
		String	str = "[{operacao=filtrarCandidatos, filtros={competenciasExperiencias=[{experiencia=0, competencia=Python}], tipo=OR}, token=f13c6dda-7e96-42de-ac8f-ba603b8b74b1}]";
//	
//		
//		String competencias = "[Python, C#]".replace("#", "sharp");
//	
//		ArrayList<String > comps = new ArrayList<>();
//		try {
//			JSONArray jsonarr = new JSONArray(competencias);
//			System.out.print(competencias);
//			System.out.print(jsonarr.toString());
//			
//			
//			System.out.println(0 + "nome: " + jsonarr.getString(0));
//			System.out.println(1 + "nome: " + jsonarr.getString(1));
//			
//			for(int i = 0; i<jsonarr.length();i++) {
//				
//				System.out.println(i + "nome: " + jsonarr.getString(i));
//				
//					comps.add(jsonarr.getString(i));
//			}
//			
//			
//			} catch (JSONException e) {
//				System.out.println(e);
//			}
//		
//		System.out.println("lista : " + comps.toString());
		
		
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
	
	private static List<String> converterJsonArrayToList (String data) {
		
		data = data.replace("[", "").replace("]", "");
	    String[] array = data.split(", ");
	    List<String> list = new ArrayList<>();
	    for (String s : array) {
	        list.add(s);
	    }
	    return list;
	}
	
	private static void segundoTeste() {
		//JSONArray newArray = new JSONArray(data.get("filtros").toString());
		//JSONObject newObj = new JSONObject (data.get("filtros").toString()); 
		
		
	//	Map<String, Object> comps = mapper.readValue(new JSONObject (data.get("filtros").toString()).toString(), new TypeReference<Map<String, Object>>() {}); 
		
		//System.out.println("Json objeto : " + newObj.toString());
		//System.out.println(newArray.toString());
		
		//Map<String, Object> comps = mapper.readValue(newObj.toString(), new TypeReference<Map<String, Object>>() {}); 
		
//		
//		System.out.println("Segundo map" + comps.toString());
//		
//		List<String> competencias =  converterJsonArrayToList (comps.get("competencias").toString());
//		
//		System.out.println("Lista competencias"+competencias.toString());
	}
	private static void primeiroTeste() throws JSONException, JsonMappingException, JsonProcessingException {
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
	}
	
}



