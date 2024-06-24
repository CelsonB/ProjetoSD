package gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.json.JSONException;

import entities.Competencia;
import entities.Empresa;
import service.CompetenciaService;
import service.VagasService;
import javax.swing.ComboBoxModel;

public class FiltrarCandidatosWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	protected static String [] competenciasNome  = {   "Python", "C#", "C++", "JS", "PHP", "Swift", "Java", "Go", "SQL", "Ruby", 
		    "HTML", "CSS", "NOSQL", "Flutter", "TypeScript", "Perl", "Cobol", "dotNet", 
		    "Kotlin", "Dart"};
	private List<Competencia> competencias = new ArrayList<>();
	private Socket clienteSocket;
	private Empresa sessao;
	private VagasService vagaService;
	private JComboBox listFiltro ;
	public FiltrarCandidatosWindow(Socket clienteSocket, Empresa sessao) {
		this.clienteSocket = clienteSocket;
		this.sessao = sessao;
		vagaService = new VagasService(clienteSocket,sessao);
		
		initComponents();
	}

	
	public void atualizarTable() {
		DefaultTableModel modelo = (DefaultTableModel) table.getModel();
			
			modelo.fireTableDataChanged();
			
			modelo.setRowCount(0);
		
				for(Competencia comp: this.competencias) {
					modelo.addRow(new Object[] {
							comp.getNomeCompetencia(),
							comp.getExperiencia(),	
					});
				}
				
		
		}
	
	public void initComponents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 262, 346);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblCompetencia = new JLabel("Competencia");
		lblCompetencia.setBounds(10, 119, 62, 14);
		contentPane.add(lblCompetencia);
		
		
		DefaultComboBoxModel model = new DefaultComboBoxModel (competenciasNome);
		
		JComboBox listCompetencia = new JComboBox(model);
		listCompetencia.setBounds(111, 116, 125, 20);
		contentPane.add(listCompetencia);
		
		JLabel lblExperiencia = new JLabel("Experiencia");
		lblExperiencia.setBounds(10, 144, 62, 14);
		contentPane.add(lblExperiencia);
		
		JSpinner spinnerExperiencia = new JSpinner();
		spinnerExperiencia.setBounds(111, 144, 125, 20);
		contentPane.add(spinnerExperiencia);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 226, 97);
		contentPane.add(scrollPane);
		
		 table = new JTable();
		scrollPane.setViewportView(table);
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Competencia", "Experiencia"
			}
		));
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 202, 226, 94);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JButton btnNewButton = new JButton("Adicionar");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object selectedItem = listCompetencia.getSelectedItem();
				String nomeCompetencia = (String) selectedItem;
				
			int exp = (int)spinnerExperiencia.getValue();
				Competencia comp = new Competencia(exp,nomeCompetencia);
				
				competencias.add(comp);
				spinnerExperiencia.setValue(0);
				atualizarTable();

			}
		});
		btnNewButton.setBounds(0, 6, 226, 23);
		panel.add(btnNewButton);
		
		JButton btnCadastrarCompetencias = new JButton("Filtrar candidatos");
		btnCadastrarCompetencias.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				filtrarCandidatos();
			}
		});
		btnCadastrarCompetencias.setBounds(0, 35, 226, 23);
		panel.add(btnCadastrarCompetencias);
		
		JButton btnVoltar = new JButton("Voltar");
		btnVoltar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new VagasWindow(sessao,clienteSocket).setVisible(true);
				dispose();
			}
		});
		btnVoltar.setBounds(0, 64, 226, 23);
		panel.add(btnVoltar);
		
		JLabel lblFiltro = new JLabel("Filtro");
		lblFiltro.setBounds(10, 177, 62, 14);
		contentPane.add(lblFiltro);
		
		 listFiltro = new JComboBox(new DefaultComboBoxModel(new String[] {"OR", "AND"}));
		listFiltro.setBounds(111, 171, 125, 20);
		contentPane.add(listFiltro);
	}
	public void filtrarCandidatos() {
		
		String filtro = (String) listFiltro.getSelectedItem();
		
		List<Candidato> candidatoFiltrados = vagaService.filtrarCandidatos(competencias, filtro);
	}
}
