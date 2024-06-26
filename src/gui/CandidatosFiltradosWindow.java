package gui;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import entities.Candidato;
import entities.Competencia;
import entities.Empresa;
import service.EmpresaService;

import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import org.json.JSONException;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.awt.event.ActionEvent;

public class CandidatosFiltradosWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;

	
	private List<Candidato> candidatoFiltrados;
	private Empresa sessao;
	private Socket clienteSocket;
	private EmpresaService empresaService;
	public CandidatosFiltradosWindow(List<Candidato> candidatoFiltrados,Empresa sessao, Socket clienteSocket) {
		this.sessao=sessao;
		this.clienteSocket = clienteSocket;
		this.candidatoFiltrados = candidatoFiltrados;
		this.empresaService = new EmpresaService(this.clienteSocket);
		initComponents();
		popularTable();
	}
	
	public void popularTable() {
		DefaultTableModel modelo = (DefaultTableModel) table.getModel();
		
		modelo.fireTableDataChanged();
		
		modelo.setRowCount(0);
	
			for(Candidato cand: candidatoFiltrados) {
				
				System.out.println(cand.getNome());
				modelo.addRow(new Object[] {
						cand.getId(),
						cand.getNome(),
						cand.getEmail(),
						cand.getListaCompetencia().toString()
				});
			}
			
	}
	
	public void initComponents() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 410, 128);
		contentPane.add(scrollPane);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"id","Nome", "Email", "Competencias"
			}
		));
		scrollPane.setViewportView(table);
		
		JButton btnNewButton = new JButton("Voltar");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			dispose();
			}
		});
		btnNewButton.setBounds(30, 160, 171, 23);
		contentPane.add(btnNewButton);
		
		JButton btnEnviarMensagem = new JButton("Enviar mensagem");
		btnEnviarMensagem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				enviarMensagem();
			}
		});
		btnEnviarMensagem.setBounds(231, 160, 171, 23);
		contentPane.add(btnEnviarMensagem);
	}
	
	public void enviarMensagem() {
		
	    int[] selectedRows = table.getSelectedRows();
	    
	 
	    List<Integer> selectedIds = new ArrayList<>();

	  
	    for (int rowIndex : selectedRows) {
	       
	        int id = (int) table.getValueAt(rowIndex, 0);
	        selectedIds.add(id);
	    }

	   
	    int[] idsArray = selectedIds.stream().mapToInt(i -> i).toArray();
		try {
			if(empresaService.enviarMensagem(sessao, idsArray)) {
		
			}
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
