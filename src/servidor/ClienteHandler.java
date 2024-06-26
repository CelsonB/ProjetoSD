package servidor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClienteHandler implements Runnable {
    private Socket clienteSocket;
    private ObjectMapper mapper;
    private List<String> usuariosLogados;

    public ClienteHandler(Socket socket) {
        this.clienteSocket = socket;
        this.mapper = new ObjectMapper();
    }

    @Override
    public void run() {
        try (
            InputStreamReader input = new InputStreamReader(clienteSocket.getInputStream());
            BufferedReader reader = new BufferedReader(input);
            PrintStream saida = new PrintStream(clienteSocket.getOutputStream());
        ) {
            ServidorCandidato candidatoServer = new ServidorCandidato(clienteSocket);
            ServidorEmpresa empresaServer = new ServidorEmpresa(clienteSocket);
            
            ServidorCompetencia competenciaSever = new ServidorCompetencia(clienteSocket);
            ServidorVaga vagaServer = new ServidorVaga(clienteSocket);

            String op = "";
            do {
                Map<String, Object> userData = mapper.readValue(reader.readLine(), new TypeReference<Map<String, Object>>() {});
                System.out.println("Entrada: [" + userData.toString() + "]");

                op = userData.get("operacao").toString();

                switch (op) {
                    case "cadastrarCompetenciaExperiencia":
                        competenciaSever.cadastrarCandidatoCompetencia(userData);
                        break;
                    case "visualizarCompetenciaExperiencia":
                        competenciaSever.visualizarCompetenciaExperiencia(userData);
                        break;
                    case "apagarCompetenciaExperiencia":
                        competenciaSever.apagarCompetenciaExperiencia(userData);
                        break;
                    case "atualizarCompetenciaExperiencia":
                        competenciaSever.atualizarCompetencia(userData);
                        break;
                    case "filtrarVagas":
                        competenciaSever.filtrarVagas(userData);
                        break;
                    case "cadastrarVaga":
                        vagaServer.cadastrarVaga(userData);
                        break;
                    case "visualizarVaga":
                        vagaServer.visualizarVaga(userData);
                        break;
                    case "apagarVaga":
                        vagaServer.apagarVaga(userData);
                        break;
                    case "atualizarVaga":
                        vagaServer.atualizarVaga(userData);
                        break;
                    case "listarVagas":
                        vagaServer.listarVagas("listarVagas", userData);
                        break;
                    case "loginCandidato":
                        String emailLogado = candidatoServer.SolicitarLogin(userData);
                        if(emailLogado!=null) {
                        	//usuariosLogados.add(emailLogado);
                        	Main.adicionarUsuarioLogado(candidatoServer.emailLogado);
                        	Main.mostrarLogados();
                        }	
                        	
                        break;
                    case "cadastrarCandidato":
                        candidatoServer.SolicitarCadastro(userData);
                        break;
                    case "visualizarCandidato":
                        candidatoServer.SolicitarVisualizacao(userData.get("email").toString());
                        break;
                    case "logout":
                    	  if (empresaServer.getToken() != null) {
                    		  Main.removerUsuarioLogado(candidatoServer.emailLogado);
                    		  Main.mostrarLogados();
                              candidatoServer.setToken(null);
                              empresaServer.logout();
                          } else if (candidatoServer.getToken() != null) {
                        	  Main.removerUsuarioLogado(candidatoServer.emailLogado);
                        	  Main.mostrarLogados();
                              empresaServer.setToken(null);
                              candidatoServer.logout();
                          }
                        break;
                    case "atualizarCandidato":
                        candidatoServer.atualizarCadastro(userData);
                        break;
                    case "apagarCandidato":
                        candidatoServer.apagarCandidato(userData.get("email").toString());
                        break;
                    case "cadastrarEmpresa":
                        empresaServer.cadastrarEmpresa(userData);
                        break;
                    case "loginEmpresa":
                      
                        
                        if(  empresaServer.realizarLogin(userData)) {
                        	 Main.adicionarUsuarioLogado(empresaServer.emailLogado);
                        	 Main.mostrarLogados();
                        }
                       
                        break;
                    case "visualizarEmpresa":
                        empresaServer.visualizarEmpresa(userData);
                        break;
                    case "atualizarEmpresa":
                        empresaServer.atualizarEmpresa(userData);
                        break;
                    case "apagarEmpresa":
                        empresaServer.apagarEmpresa(userData);
                        break;
                        //quarta entrega
                    case "receberMensagem":
                    	candidatoServer.receberMensagem(userData);
                    	break;
                    case "enviarMensagem":
                    	empresaServer.enviarMensagem(userData);
                    	break;
                    case "filtrarCandidatos":
                    	vagaServer.filtrarCandidato(userData);
                    	break;
                   
                    
                }
            } while (op != null && !op.isEmpty());
        } catch (Exception ex) {
            System.err.println("Erro na comunicação com o cliente: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                clienteSocket.close();
            } catch (Exception e) {
                System.err.println("Erro ao fechar o socket do cliente: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
//    private void listarUsuariosLogados(PrintStream saida) {
//        Set<String> usuariosLogados = Main.getUsuariosLogados();
//        StringBuilder sb = new StringBuilder();
//        sb.append("Usuários logados:\n");
//        for (String usuario : usuariosLogados) {
//            sb.append(usuario).append("\n");
//        }
//        saida.println(sb.toString());
//    }

    
}
