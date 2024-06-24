package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.json.JSONException;

import cliente.Cliente;
import entities.Candidato;
import entities.Empresa;
import service.CandidatoService;
import service.EmpresaService;

import javax.swing.JRadioButton;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.ButtonGroup;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.awt.event.ActionEvent;

public class LoginWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldEmail;
	private JPasswordField textFieldSenha;
	private final ButtonGroup buttonGroup = new ButtonGroup();

	

	
	public static Socket clienteSocket = null;
	
	

	public LoginWindow(Socket clienteSocket) {	
	
		this.clienteSocket = clienteSocket;
		initComponents();
		
		
	}
	
	
	
	public void initComponents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 296, 279);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(41, 35, 199, 23);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JRadioButton rdbtnCliente = new JRadioButton("Cliente");
		buttonGroup.add(rdbtnCliente);
		rdbtnCliente.setBounds(0, 0, 109, 23);
		panel.add(rdbtnCliente);
		rdbtnCliente.setSelected(true);
		JRadioButton rdbtnEmpresa = new JRadioButton("Empresa");
		buttonGroup.add(rdbtnEmpresa);
		rdbtnEmpresa.setBounds(111, 0, 109, 23);
		panel.add(rdbtnEmpresa);
		
		JButton btnNewButton = new JButton("Realizar login");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				if(buttonGroup.getSelection().equals(rdbtnCliente.getModel())) {
					
					CandidatoService candidatoService = new CandidatoService(clienteSocket);
					Candidato user = new Candidato();
					
					user.setEmail(textFieldEmail.getText());
					user.setSenha(textFieldSenha.getText());
					
					try {
						Candidato sessao = candidatoService.realizarLogin(user);
						
						if(sessao!=null) {
							new PerfilCandidatoWindow(sessao,clienteSocket).setVisible(true);;
							dispose();
						}else {
							JOptionPane.showMessageDialog(null, "Senha ou email incorretos", "Realizar login", JOptionPane.WARNING_MESSAGE);
						}
						
					} catch (JSONException | IOException e1) {
				
						
					}
					
					dispose();
				}
				else
				if(buttonGroup.getSelection().equals(rdbtnEmpresa.getModel())) 
				{
					 EmpresaService empresaService = new EmpresaService(clienteSocket);
					 
					Empresa userLogin = new Empresa();
					userLogin.setEmail(textFieldEmail.getText());
					userLogin.setSenha(textFieldSenha.getText());
					Empresa sessao;
					try {
						sessao = empresaService.realizarLogin(userLogin);
						
						if(sessao!=null) {
							new PerfilEmpresaWindow(sessao, clienteSocket).setVisible(true);
							dispose();
						}else {
							JOptionPane.showMessageDialog(null, "Senha ou email incorretos", "Realizar login", JOptionPane.WARNING_MESSAGE);
						}
						
					
				
						
					} catch (IOException e1) {
					
						JOptionPane.showMessageDialog(null, e1.getMessage(), "Realizar login", JOptionPane.WARNING_MESSAGE);
					}
					
					
					
				}
	
	
			}
		});
		btnNewButton.setBounds(20, 205, 105, 23);
		contentPane.add(btnNewButton);
		
		JButton btnCadastrar = new JButton("Cadastrar");
		btnCadastrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(buttonGroup.getSelection().equals(rdbtnCliente.getModel())) {
					
					new CadastrarUsuarioWindow(clienteSocket).setVisible(true);
					dispose();
				}
				else
				if(buttonGroup.getSelection().equals(rdbtnEmpresa.getModel())) 
				{
					new CadastrarEmpresaWindow(clienteSocket).setVisible(true);
					dispose();
				}
				
			}
		});
		btnCadastrar.setBounds(146, 205, 105, 23);
		contentPane.add(btnCadastrar);
		
		JLabel lblEmail = new JLabel("Email");
		lblEmail.setBounds(41, 97, 46, 14);
		contentPane.add(lblEmail);
		
		JLabel lblSenha = new JLabel("Senha");
		lblSenha.setBounds(41, 126, 46, 14);
		contentPane.add(lblSenha);
		
		textFieldEmail = new JTextField();
		textFieldEmail.setBounds(111, 94, 129, 20);
		contentPane.add(textFieldEmail);
		textFieldEmail.setColumns(10);
		
		textFieldSenha = new JPasswordField();
		textFieldSenha.setColumns(10);
		textFieldSenha.setBounds(111, 123, 129, 20);
		contentPane.add(textFieldSenha);
	}
}
