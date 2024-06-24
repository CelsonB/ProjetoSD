package gui;

import java.awt.EventQueue;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import entities.Empresa;
import service.EmpresaService;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class CadastrarEmpresaWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldRazaoSocial;
	private JTextField textField_1;
	private JTextField textFieldEmail;
	private JTextField textFieldRamo;
	private JTextField textFieldDescricao;
	private JTextField textFieldCnpj;
	private JTextField textFieldSenha;
	private 	JButton btnRealizarCadastro;
	
	private EmpresaService empresaService;
	public static Socket clienteSocket = null;
	public CadastrarEmpresaWindow(Socket clienteSocket) {	
		this.clienteSocket = clienteSocket;
		this.empresaService = new EmpresaService(this.clienteSocket);
		this.sessao=null; 
		initComponents();	 
	}

	private Empresa sessao; 
	
	public CadastrarEmpresaWindow(Socket clienteSocket, Empresa sessao) {	
		
		this.sessao = sessao;
		this.clienteSocket = clienteSocket;
		this.empresaService = new EmpresaService(this.clienteSocket);
		initComponents();
		
		btnRealizarCadastro.setText("Atualizar cadastro");
	
		textFieldCnpj.setText(sessao.getCnpj());
		textFieldEmail.setText(sessao.getEmail());
		textFieldSenha.setText(sessao.getSenha());
		textFieldRamo.setText(sessao.getRamo());
		textFieldRazaoSocial.setText(sessao.getRazaoSocial());
		textFieldDescricao.setText(sessao.getDescricao());
		
		
		 
	}
	
	public void initComponents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 306, 313);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(19, 25, 251, 174);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblRazaoSocial = new JLabel("Raz\u00E3o social");
		lblRazaoSocial.setBounds(10, 3, 63, 14);
		panel.add(lblRazaoSocial);
		
		textFieldRazaoSocial = new JTextField();
		textFieldRazaoSocial.setBounds(83, 0, 158, 20);
		panel.add(textFieldRazaoSocial);
		textFieldRazaoSocial.setColumns(10);
		
		JLabel lblNewLabelEmail = new JLabel("Email");
		lblNewLabelEmail.setBounds(10, 31, 46, 14);
		panel.add(lblNewLabelEmail);
		
		textFieldEmail = new JTextField();
		textFieldEmail.setBounds(83, 31, 158, 20);
		panel.add(textFieldEmail);
		textFieldEmail.setColumns(10);
		
		JLabel lblNewLabelSenha = new JLabel("Senha");
		lblNewLabelSenha.setBounds(10, 62, 46, 14);
		panel.add(lblNewLabelSenha);
		
		textFieldRamo = new JTextField();
		textFieldRamo.setBounds(83, 90, 158, 20);
		panel.add(textFieldRamo);
		textFieldRamo.setColumns(10);
		
		JLabel lblNewLabelRamo = new JLabel("Ramo");
		lblNewLabelRamo.setBounds(10, 93, 46, 14);
		panel.add(lblNewLabelRamo);
		
		textFieldDescricao = new JTextField();
		textFieldDescricao.setBounds(83, 123, 158, 20);
		panel.add(textFieldDescricao);
		textFieldDescricao.setColumns(10);
		
		JLabel lblNewLabelDescricao = new JLabel("Descri\u00E7\u00E3o");
		lblNewLabelDescricao.setBounds(10, 126, 46, 14);
		panel.add(lblNewLabelDescricao);
		
		textFieldCnpj = new JTextField();
		textFieldCnpj.setBounds(83, 154, 158, 20);
		panel.add(textFieldCnpj);
		textFieldCnpj.setColumns(10);
		
		JLabel lblNewLabelCnpj = new JLabel("CNPJ");
		lblNewLabelCnpj.setBounds(10, 157, 46, 14);
		panel.add(lblNewLabelCnpj);
		
		textFieldSenha = new JTextField();
		textFieldSenha.setColumns(10);
		textFieldSenha.setBounds(83, 59, 158, 20);
		panel.add(textFieldSenha);
		
		btnRealizarCadastro = new JButton("Realizar cadastro");
		btnRealizarCadastro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(sessao==null) {
					realizarCadastro();
				}{
					atualizarCadastro();
				}
			
			}
		});
		btnRealizarCadastro.setBounds(19, 240, 116, 23);
		contentPane.add(btnRealizarCadastro);
		
		JButton btnVoltar = new JButton("Voltar");
		btnVoltar.setBounds(154, 240, 116, 23);
		contentPane.add(btnVoltar);
	}
	
	
	public void atualizarCadastro() {
		Empresa user = new Empresa();
		
		user.setCnpj(textFieldCnpj.getText());
		user.setDescricao(textFieldDescricao.getText());
		user.setEmail(textFieldEmail.getText());
		user.setRamo(textFieldRamo.getText());
		user.setRazaoSocial(textFieldRazaoSocial.getText());
		user.setSenha(textFieldSenha.getText());
		user.setToken(sessao.getToken());
		try {
			
			if(empresaService.atualizarEmpresa(user)) {
				JOptionPane.showMessageDialog(null, "Atualização realizada com sucesso", "atualizar empresa", JOptionPane.INFORMATION_MESSAGE);
				new PerfilEmpresaWindow(user,this.clienteSocket).setVisible(true);
				dispose();
			}
			
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Realizar cadastros empresa", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	
	public void realizarCadastro() {
		Empresa user = new Empresa();
		
		user.setCnpj(textFieldCnpj.getText());
		user.setDescricao(textFieldDescricao.getText());
		user.setEmail(textFieldEmail.getText());
		user.setRamo(textFieldRamo.getText());
		user.setRazaoSocial(textFieldRazaoSocial.getText());
		user.setSenha(textFieldSenha.getText());
		
		
		try {
			
			if(empresaService.cadastrarEmpresa(user)) {
				JOptionPane.showMessageDialog(null, "Cadastro realizado com sucesso", "Realizar cadastro empresa", JOptionPane.INFORMATION_MESSAGE);
				new LoginWindow(this.clienteSocket).setVisible(true);
				dispose();
			}
			
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Realizar cadastros empresa", JOptionPane.WARNING_MESSAGE);
		}
		
		
	}
	
	
	
}
