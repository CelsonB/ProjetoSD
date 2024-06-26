package gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import entities.Candidato;
import entities.Vaga;
import service.CompetenciaService;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class FiltrarVagasWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JList<String> listCompetencias;
	private List<String> selectedCompetencia;
	protected static String [] competenciasNome  = {   "Python", "C#", "C++", "JS", "PHP", "Swift", "Java", "Go", "SQL", "Ruby", 
		    "HTML", "CSS", "NOSQL", "Flutter", "TypeScript", "Perl", "Cobol", "dotNet", 
		    "Kotlin", "Dart"};
	private JButton btnFiltrarVagas;
	private JButton btnVoltar;	
	
	private Socket clienteSocket; 
	private Candidato sessao;
	private CompetenciaService competenciaService;
	private JComboBox comboBox;
	private JScrollPane scrollPane;
	
	
	public FiltrarVagasWindow(Candidato sessao, Socket clienteSocket) {
		this.clienteSocket = clienteSocket;
		this.sessao = sessao;
		competenciaService = new CompetenciaService(this.sessao,this.clienteSocket);
		
		initComponents();
		
	}

	public void initComponents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 317, 226);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		
		JLabel lblCompetencias = new JLabel("Competencias");
		lblCompetencias.setBounds(10, 86, 67, 14);
		contentPane.add(lblCompetencias);
		
		
		 DefaultListModel<String> listModel = new DefaultListModel<>();
	        for (String competencia : competenciasNome) {
	            listModel.addElement(competencia);
	        }
	      
	      btnFiltrarVagas = new JButton("Filtrar vagas");
	      btnFiltrarVagas.addActionListener(new ActionListener() {
	      	public void actionPerformed(ActionEvent e) {
	      		filtrarVagas();
	      	}
	      });
	      btnFiltrarVagas.setBounds(159, 153, 125, 23);
	      contentPane.add(btnFiltrarVagas);
	      
	      btnVoltar = new JButton("Voltar");
	      btnVoltar.addActionListener(new ActionListener() {
	      	public void actionPerformed(ActionEvent e) {
	      		new PerfilCandidatoWindow(sessao,clienteSocket).setVisible(true);
	      		dispose();
	      	
	      	}
	      });
	      btnVoltar.setBounds(17, 153, 125, 23);
	      contentPane.add(btnVoltar);
	      
	      scrollPane = new JScrollPane();
	      scrollPane.setBounds(87, 49, 197, 89);
	      contentPane.add(scrollPane);
	      
	      listCompetencias = new JList<>(listModel);
	      scrollPane.setViewportView(listCompetencias);
	      
	      listCompetencias.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);	
	      listCompetencias.addListSelectionListener(new ListSelectionListener() {
	            public void valueChanged(ListSelectionEvent e) {
	                if (e.getValueIsAdjusting()) {
	                    return;
	                }
	                selectedCompetencia = listCompetencias.getSelectedValuesList();
	                System.out.println("Selected competência: " + selectedCompetencia);
	            }
	        });
	      
	      comboBox = new JComboBox();
	      comboBox.setModel(new DefaultComboBoxModel(new String[] {"AND", "OR", "ALL"}));
	      comboBox.setBounds(87, 16, 197, 22);
	      contentPane.add(comboBox);
	      
	      JLabel lblFiltro = new JLabel("Filtro");
	      lblFiltro.setBounds(10, 20, 67, 14);
	      contentPane.add(lblFiltro);
	}
	
	public void filtrarVagas() {
		String tipo =(String) comboBox.getSelectedItem();
		List<Vaga> listaEmpresa = competenciaService.filtrarVagas(selectedCompetencia,tipo);
		new VagasFiltradasWindow(listaEmpresa).setVisible(true);
		
	}
	
	
	
	
	
	
}






