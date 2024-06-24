package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.json.JSONException;

import entities.Candidato;
import entities.Competencia;
import service.CompetenciaService;

import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;

public class CompetenciasWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;

	
	private Socket clienteSocket;
	private Candidato sessao;
	
	private CompetenciaService competenciaService;
	private List<Competencia> competencias;
	public CompetenciasWindow(Socket clienteSocket, Candidato sessao) {
		this.clienteSocket = clienteSocket;
		this.sessao = sessao;
		
		competenciaService= new CompetenciaService(this.sessao,this.clienteSocket);
		
		
	
		try {
			this.competencias= competenciaService.visualizarCompetencias();
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		System.out.println("chegou aqui 2000");
		initComponents();
		setVisible(true);
		popularTable();
		
	}
	public void popularTable() {
		
		DefaultTableModel modelo = (DefaultTableModel) table.getModel();
		
		modelo.fireTableDataChanged();
		
		modelo.setRowCount(0);
	
			for(Competencia comp: competencias) {
				modelo.addRow(new Object[] {
						comp.getNomeCompetencia(),
						comp.getExperiencia(),	
				});
			}
	}
	
	public void initComponents() {
		
		System.out.println("Ele existe o initcompenents()");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 336, 283);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 300, 159);
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
		
		JButton btnAdicionarCompetencia = new JButton("Adicionar competencia");
		btnAdicionarCompetencia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new CadastrarCompetencia(sessao, clienteSocket).setVisible(true);
				dispose();
			}
		});
		btnAdicionarCompetencia.setBounds(10, 181, 139, 23);
		contentPane.add(btnAdicionarCompetencia);
		
		JButton btnApagarCompetencia = new JButton("Apagar competencia");
		btnApagarCompetencia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				apagarCompetencia();
			}
		});
		btnApagarCompetencia.setBounds(10, 215, 139, 23);
		contentPane.add(btnApagarCompetencia);
		
		JButton btnEditarCompetencia = new JButton("Editar competencia");
		btnEditarCompetencia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editarCompetencia();
			}
		});
		btnEditarCompetencia.setBounds(171, 181, 139, 23);
		contentPane.add(btnEditarCompetencia);
		
		JButton btnVoltar = new JButton("Voltar");
		btnVoltar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnVoltar.setBounds(171, 215, 139, 23);
		contentPane.add(btnVoltar);
	}
	
	public void apagarCompetencia() {
		 int[] rows = table.getSelectedRows();
		    List<Competencia> listaExclusao = new ArrayList<>();

		    for (int row : rows) {
		        String nomeCompetencia = (String) table.getValueAt(row, 0);
		        int experiencia = (int) table.getValueAt(row, 1);

		        for (Competencia comp : competencias) {
		            if (comp.getNomeCompetencia().equals(nomeCompetencia) && comp.getExperiencia()== experiencia ){
		            	listaExclusao.add(comp);
		                break;
		            }
		        }
		    }
		    
		    
		    if(competenciaService.apagarCompetencia(listaExclusao)) {
		    	JOptionPane.showMessageDialog(null, "Competencias apagadas com sucesso");
		    }else {
		    	JOptionPane.showMessageDialog(null, "erro ao apagar competencias");
		    }
		    
		    
		    
		
	}
	
	
	
	public void editarCompetencia() {
		 int[] rows = table.getSelectedRows();
		    List<Competencia> listaExclusao = new ArrayList<>();

		    for (int row : rows) {
		        String nomeCompetencia = (String) table.getValueAt(row, 0);
		        int experiencia = (int) table.getValueAt(row, 1);

		        for (Competencia comp : competencias) {
		            if (comp.getNomeCompetencia().equals(nomeCompetencia) && comp.getExperiencia()== experiencia ){
		            	listaExclusao.add(comp);
		                break;
		            }
		        }
		    }
	}
}
