package servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {

    public static ServerSocket servidorSocket;
    public static Set<String> usuariosLogados = Collections.synchronizedSet(new HashSet<>());
    
    public static void Main(String[] args) {
    	
        System.out.println("Programa 1: Servidor");

        Scanner leia = new Scanner(System.in);
        System.out.println("Digite a porta");
        int porta = leia.nextInt();

        try {
            servidorSocket = new ServerSocket(porta, 5);
            System.out.println("Servidor iniciado na porta " + porta);

            while (true) {
                Socket clienteSocket = servidorSocket.accept();
                System.out.println("Cliente conectado: " + clienteSocket.getInetAddress());

                
                new Thread(new ClienteHandler(clienteSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Erro ao iniciar o servidor: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (servidorSocket != null && !servidorSocket.isClosed()) {
                try {
                    servidorSocket.close();
                } catch (IOException e) {
                    System.err.println("Erro ao fechar o servidor: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            leia.close();
        }
    }
    
    public static void adicionarUsuarioLogado(String email) {
        usuariosLogados.add(email);
    }

    public static void removerUsuarioLogado(String email) {
        usuariosLogados.remove(email);
    }

    public static Set<String> getUsuariosLogados() {
        return usuariosLogados;
    }
    
    public static void mostrarLogados() {
    	for(String str: usuariosLogados) {
    		System.out.println("Email: "+ str);
    	}
    }
}