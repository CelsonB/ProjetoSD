package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.json.JSONException;

import entities.Candidato;
import service.CandidatoService;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.awt.event.ActionEvent;

public class CadastrarUsuarioWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldNome;
	private JTextField textFieldSenha;
	private JTextField textFieldEmail;
	private JButton btnNewButton;
	
	
	
	
	public static Socket clienteSocket = null;
	


	private CandidatoService candidatoService;
	
	public CadastrarUsuarioWindow(Socket ss) {
		this.clienteSocket = ss;
		candidatoService = new CandidatoService(this.clienteSocket);
		initComponents();
		
	}
	
	
	private Candidato sessao = null;
	public CadastrarUsuarioWindow(Socket ss, Candidato sessao) {
		this.clienteSocket = ss;
		this.sessao = sessao;
		candidatoService = new CandidatoService(this.clienteSocket,sessao);
	
		initComponents();
		
		textFieldEmail.setText(sessao.getEmail());
		textFieldNome.setText(sessao.getNome());
		textFieldSenha.setText(sessao.getSenha());
		btnNewButton.setText("Atualizar cadastro");
		
	}
	
	public void initComponents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 306, 254);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 270, 151);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblNome = new JLabel("Nome");
		lblNome.setBounds(10, 14, 46, 14);
		panel.add(lblNome);
		
		JLabel lblSenha = new JLabel("Senha");
		lblSenha.setBounds(10, 42, 46, 14);
		panel.add(lblSenha);
		
		JLabel lblEmail = new JLabel("Email");
		lblEmail.setBounds(10, 70, 46, 14);
		panel.add(lblEmail);
		
		textFieldNome = new JTextField();
		textFieldNome.setBounds(99, 11, 161, 20);
		panel.add(textFieldNome);
		textFieldNome.setColumns(10);
		
		textFieldSenha = new JTextField();
		textFieldSenha.setBounds(99, 41, 161, 20);
		panel.add(textFieldSenha);
		textFieldSenha.setColumns(10);
		
		textFieldEmail = new JTextField();
		textFieldEmail.setBounds(99, 70, 161, 20);
		panel.add(textFieldEmail);
		textFieldEmail.setColumns(10);
		
		btnNewButton = new JButton("Finalizar cadastro");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(sessao==null) {
					cadastrarUsuario();
				}else {
					atualizarCadastro();
				}
				
				
			}
		});
		btnNewButton.setBounds(10, 173, 125, 23);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("New button");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(sessao==null) {
					new LoginWindow(clienteSocket).setVisible(true);
					dispose();
				}else {
					new PerfilCandidatoWindow(sessao,clienteSocket).setVisible(true);
					dispose();
				}
			}
		});
		btnNewButton_1.setBounds(145, 173, 135, 23);
		contentPane.add(btnNewButton_1);
	}
	public void atualizarCadastro() {
	Candidato user = new Candidato();
		
		
		
		user.setEmail(textFieldEmail.getText());
		user.setSenha(textFieldSenha.getText());
		user.setNome(textFieldNome.getText());
		try {
			
			if(candidatoService.atualizarCadastro(user)) {
				new PerfilCandidatoWindow(sessao,clienteSocket).setVisible(true);
				dispose();
			}
			
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e.getMessage(), "Conectar", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	
	public void cadastrarUsuario() {
		
		
		
	
		
		Candidato user = new Candidato();
		
		
		
		user.setEmail(textFieldEmail.getText());
		user.setSenha(textFieldSenha.getText());
		user.setNome(textFieldNome.getText());
		try {
			
			candidatoService.cadastrarUsuario(user);
			
				new LoginWindow(clienteSocket).setVisible(true);
				dispose();
			
			
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e.getMessage(), "Conectar", JOptionPane.WARNING_MESSAGE);
		}
	}
}



























