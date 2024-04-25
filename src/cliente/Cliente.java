package cliente;

import java.io.PrintStream;
import java.net.*;
import java.util.Scanner;
import org.json.JSONObject;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class Cliente {

	
	public static void main (String[] args) {
		try 
		{
			JSONObject json = new JSONObject();
			json.put("type", "CONNECT");
			
			Socket clienteSocket = new Socket("teste",8787);
			
			try (OutputStreamWriter out = new OutputStreamWriter(
					clienteSocket.getOutputStream(), StandardCharsets.UTF_8)) {
				
				 String myString = new JSONObject()
				         .put("Email:", "Celsond@alunos.utfpr.edu.br").put("Senha:", "121212333").put("nome:", "Celson").toString();
				 
				 
				 
				 
			    out.write(myString);
			}
			
			
		}
		catch(Exception ex)
		{
			System.out.println(ex);
				
		}
	}
}
