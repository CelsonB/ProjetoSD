package gui;

import java.awt.EventQueue;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.json.JSONException;

import entities.Candidato;
import entities.Empresa;
import service.EmpresaService;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PerfilEmpresaWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;


	
	public static Socket clienteSocket = null;
	public Empresa sessao; 
	private EmpresaService empresaService; 
	
	public PerfilEmpresaWindow(Empresa sessao,Socket clienteSocket){
	
		
		this.clienteSocket = clienteSocket;
		this.empresaService = new EmpresaService(this.clienteSocket);
		this.sessao=sessao;
		try {
			this.sessao = empresaService.visualizarEmpresa(sessao);
			this.sessao.setEmail(sessao.getEmail());
			this.sessao.setToken(sessao.getToken());
			
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		initComponents();
	}

	public void initComponents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 282, 279);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 242, 153);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblRazaoSocial = new JLabel("Raz\u00E3o social: " + sessao.getRazaoSocial());
		lblRazaoSocial.setBounds(10, 13, 222, 14);
		panel.add(lblRazaoSocial);
		
		JLabel lblNomeEmail = new JLabel("Email: "+ sessao.getEmail());
		lblNomeEmail.setBounds(10, 40, 222, 14);
		panel.add(lblNomeEmail);
		
		JLabel lblRamo = new JLabel("Ramo: "+ sessao.getRamo());
		lblRamo.setBounds(10, 67, 222, 14);
		panel.add(lblRamo);
		
		JLabel lblDescricao = new JLabel("Descri\u00E7\u00E3o: " + sessao.getDescricao());
		lblDescricao.setBounds(10, 94, 222, 14);
		panel.add(lblDescricao);
		
		JLabel lblCnpj = new JLabel("CNPJ: " + sessao.getCnpj());
		lblCnpj.setBounds(10, 121, 222, 14);
		panel.add(lblCnpj);
		
		JButton btnAtualizarCadastro = new JButton("Atualizar cadastro");
		btnAtualizarCadastro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new CadastrarEmpresaWindow(clienteSocket,sessao).setVisible(true);
				dispose();
			}
		});
		btnAtualizarCadastro.setBounds(10, 175, 119, 23);
		contentPane.add(btnAtualizarCadastro);
		
		JButton btnDeletarCadastro = new JButton("Deletar cadastro");
		btnDeletarCadastro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deletarCadastro();
			}
		});
		btnDeletarCadastro.setBounds(139, 175, 113, 23);
		contentPane.add(btnDeletarCadastro);
		
		JButton btnVoltar = new JButton("Voltar");
		btnVoltar.setBounds(10, 206, 119, 23);
		contentPane.add(btnVoltar);
		
		JButton btnVagas = new JButton("Vagas");
		btnVagas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new VagasWindow(sessao,clienteSocket).setVisible(true);
				dispose();
			}
		});
		btnVagas.setBounds(139, 206, 113, 23);
		contentPane.add(btnVagas);
	}

	public void deletarCadastro() {
		
		try {
		
			if(	empresaService.deletarEmpresa(sessao)) {
				new LoginWindow(clienteSocket).setVisible(true);
				dispose();
			}
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e,"erro", JOptionPane.WARNING_MESSAGE);
		}
		
	
	}
	
	
	
	
	
	
}
