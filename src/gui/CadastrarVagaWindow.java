package gui;

import java.awt.EventQueue;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import entities.Empresa;
import entities.Vaga;
import service.EmpresaService;
import service.VagasService;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.awt.event.ActionEvent;

public class CadastrarVagaWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldNome;
	private JTextField textFieldDescricao;
	private JTextField textFieldFaixaSalarial;
	private JList<String> listCompetencias;
	
	private List<String> selectedCompetencia;
	protected static String [] competenciasNome  = {   "Python", "C#", "C++", "JS", "PHP", "Swift", "Java", "Go", "SQL", "Ruby", 
		    "HTML", "CSS", "NOSQL", "Flutter", "TypeScript", "Perl", "Cobol", "dotNet", 
		    "Kotlin", "Dart"};
	private JScrollPane scrollPane;
	private JButton btnRealizarCadastro;
	private JButton btnVoltar;
	

	
	
	
	public static Socket clienteSocket;
	public Empresa sessao; 
	private VagasService vagaService; 
	
	
	
	public CadastrarVagaWindow(Empresa sessao,Socket clienteSocket) {
		this.sessao=sessao;
		this.clienteSocket = clienteSocket;
		this.vagaService = new VagasService(this.clienteSocket,sessao);
		initComponents();
	}
	
	private Vaga vagaSelecionada = null;
	
	
	public CadastrarVagaWindow(Empresa sessao,Socket clienteSocket,Vaga idVaga) {
		
		this.sessao = sessao;
		
		this.clienteSocket = clienteSocket;
		this.vagaService = new VagasService(this.clienteSocket,sessao);
		this.vagaSelecionada = new Vaga();
		this.vagaSelecionada = vagaService.visualizarVaga(idVaga.getIdVaga());
		this.vagaSelecionada.setIdVaga(idVaga.getIdVaga());
		
		initComponents();
		
		textFieldDescricao.setText(this.vagaSelecionada.getDescricao());
		textFieldFaixaSalarial.setText(String.valueOf(this.vagaSelecionada.getFaixaSalarial()));
		textFieldNome.setText(this.vagaSelecionada.getNome());
		selectedCompetencia = this.vagaSelecionada.getCompetencias();
		btnRealizarCadastro.setText("Atualizar cadastro");
	}
	
	public void initComponents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 329, 306);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNome = new JLabel("Nome");
		lblNome.setBounds(20, 35, 46, 14);
		contentPane.add(lblNome);
		
		JLabel lblDescricao = new JLabel("Descri\u00E7\u00E3o");
		lblDescricao.setBounds(20, 66, 46, 14);
		contentPane.add(lblDescricao);
		
		textFieldNome = new JTextField();
		textFieldNome.setBounds(76, 32, 220, 20);
		contentPane.add(textFieldNome);
		textFieldNome.setColumns(10);
		
		textFieldDescricao = new JTextField();
		textFieldDescricao.setColumns(10);
		textFieldDescricao.setBounds(76, 63, 220, 20);
		contentPane.add(textFieldDescricao);
		
		textFieldFaixaSalarial = new JTextField();
		textFieldFaixaSalarial.setColumns(10);
		textFieldFaixaSalarial.setBounds(94, 91, 202, 20);
		contentPane.add(textFieldFaixaSalarial);
		
		JLabel lblFaixaSalarial = new JLabel("Faixa salarial");
		lblFaixaSalarial.setBounds(20, 94, 62, 14);
		contentPane.add(lblFaixaSalarial);
		
		JLabel lblCompetencias = new JLabel("Competencias");
		lblCompetencias.setBounds(20, 123, 75, 14);
		contentPane.add(lblCompetencias);
		
		
		 DefaultListModel<String> listModel = new DefaultListModel<>();
	        for (String competencia : competenciasNome) {
	            listModel.addElement(competencia);
	        }       
	      
	      scrollPane = new JScrollPane();
	      scrollPane.setBounds(104, 122, 192, 82);
	      contentPane.add(scrollPane);
		
	      listCompetencias = new JList<>(listModel);
	      scrollPane.setViewportView(listCompetencias);
	      
	      listCompetencias.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);	
	      
	      btnRealizarCadastro = new JButton("Realizar cadastro");
	      btnRealizarCadastro.addActionListener(new ActionListener() {
	      	public void actionPerformed(ActionEvent e) {

	      		if(vagaSelecionada==null) {
	      		
	      			realizarCadastro();
	      			
	      		}else if(vagaSelecionada!=null){
	      			atualizarCadastro();
	      		}
	      	}
	      });
	      btnRealizarCadastro.setBounds(165, 233, 131, 23);
	      contentPane.add(btnRealizarCadastro);
	      
	      btnVoltar = new JButton("Voltar");
	      btnVoltar.addActionListener(new ActionListener() {
	      	public void actionPerformed(ActionEvent e) {
	      		new VagasWindow(sessao,clienteSocket).setVisible(true);
	      		dispose();
	      	}
	      });
	      btnVoltar.setBounds(10, 233, 113, 23);
	      contentPane.add(btnVoltar);
	      listCompetencias.addListSelectionListener(new ListSelectionListener() {
	            public void valueChanged(ListSelectionEvent e) {
	                if (e.getValueIsAdjusting()) {
	                    return;
	                }
	                selectedCompetencia = listCompetencias.getSelectedValuesList();
	                System.out.println("Selected competência: " + selectedCompetencia);
	            }
	        });
	}
	
	public void realizarCadastro() {
		
		
		
		Vaga vagaTemp = new Vaga();
		
		vagaTemp.setDescricao(textFieldDescricao.getText());
		vagaTemp.setNome(textFieldNome.getText());
		vagaTemp.setFaixaSalarial(Float.parseFloat(textFieldFaixaSalarial.getText()));
		vagaTemp.setEstado("Disponivel");
		vagaTemp.setEmail(sessao.getEmail());
		
		if(vagaService.cadastrarVaga(vagaTemp, selectedCompetencia)) {
			
			JOptionPane.showMessageDialog(null, "Vaga cadastrada com sucesso");
			
			new VagasWindow(sessao,clienteSocket).setVisible(true);
      		dispose();
		}else {
			JOptionPane.showMessageDialog(null, "Erro ao cadastrar vaga");
		}
		
		
	}
	
	public void atualizarCadastro() {
		Vaga vagaTemp = new Vaga();
		
		vagaTemp.setDescricao(textFieldDescricao.getText());
		vagaTemp.setNome(textFieldNome.getText());
		vagaTemp.setFaixaSalarial(Float.parseFloat(textFieldFaixaSalarial.getText()));
		vagaTemp.setEstado("Disponivel");
		vagaTemp.setEmail(sessao.getEmail());
		
		if(vagaService.atualizarVaga(vagaTemp, selectedCompetencia)) {
			JOptionPane.showMessageDialog(null, "Vaga cadastrada com sucesso");
			new VagasWindow(sessao,clienteSocket).setVisible(true);
      		dispose();
		}else {
			JOptionPane.showMessageDialog(null, "Erro ao cadastrar vaga");
		}
	}
}

















