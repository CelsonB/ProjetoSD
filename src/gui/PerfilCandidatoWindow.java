package gui;

import java.awt.EventQueue;
import java.net.Socket;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.json.JSONException;

import entities.Candidato;
import entities.Empresa;
import service.CandidatoService;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class PerfilCandidatoWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;


	
	public static Socket clienteSocket = null;
	private Candidato sessao; 
	private CandidatoService candidatoService;
	
	public PerfilCandidatoWindow(Candidato sessao, Socket clienteSocket) {
		this.clienteSocket = clienteSocket;
		this.sessao = sessao;
		this.candidatoService = new CandidatoService(this.clienteSocket, this.sessao);
		
		this.sessao = candidatoService.visualizarUsuario();
		this.sessao.setToken(sessao.getToken());
		try {
			List<Empresa> empresas = this.candidatoService.receberMensagem();
			if(empresas!=null) {
				for(Empresa emp : empresas) {
					JOptionPane.showMessageDialog(null, "a empresa " + emp.getRazaoSocial()+"quer te contatar","empresa interessada",JOptionPane.INFORMATION_MESSAGE);
				}
			}
		} catch (JSONException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initComponents();
	}
	
	public void initComponents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 275, 214);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 242, 62);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblNome = new JLabel("Nome: "+sessao.getNome());
		lblNome.setBounds(10, 11, 222, 14);
		panel.add(lblNome);
		
		JLabel lblNomeEmail = new JLabel("Email: "+sessao.getEmail());
		lblNomeEmail.setBounds(10, 36, 222, 14);
		panel.add(lblNomeEmail);
		
		JButton btnAtualizarCadastro = new JButton("Atualizar cadastro");
		btnAtualizarCadastro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				atualizarCadastro();
			}
		});
		btnAtualizarCadastro.setBounds(10, 84, 119, 23);
		contentPane.add(btnAtualizarCadastro);
		
		JButton btnDeletarCadastro = new JButton("Deletar cadastro");
		btnDeletarCadastro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deletarCadastro();
				
			}
		});
		btnDeletarCadastro.setBounds(139, 84, 113, 23);
		contentPane.add(btnDeletarCadastro);
		
		JButton btnVoltar = new JButton("Voltar");
		btnVoltar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new LoginWindow(clienteSocket).setVisible(true);
				dispose();
				
			}
		});
		btnVoltar.setBounds(10, 142, 119, 23);
		contentPane.add(btnVoltar);
		
		JButton btnCompetencia = new JButton("Competencias");
		
		btnCompetencia.setBounds(139, 115, 113, 23);
		btnCompetencia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				callCompetenciaWindow();
			}
		});
		contentPane.add(btnCompetencia);
		
		JButton btnFiltrarVagas = new JButton("Filtrar vagas");
		btnFiltrarVagas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new FiltrarVagasWindow(sessao,clienteSocket).setVisible(true);
			}
		});
		btnFiltrarVagas.setBounds(10, 115, 119, 23);
		contentPane.add(btnFiltrarVagas);
	}
	
	
	public void atualizarCadastro() {
		new CadastrarUsuarioWindow(clienteSocket,sessao).setVisible(true);	
	}
	public void callCompetenciaWindow() {
		new CompetenciasWindow(clienteSocket,sessao).setVisible(true);
		dispose();
	}
	
	public void deletarCadastro() {
		candidatoService.deletarUsuario();
		new LoginWindow(clienteSocket);
		dispose();
	}
}
