package gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import entities.Candidato;
import entities.Vaga;

public class VagasFiltradasWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private JTable table;

	
	private List<Vaga> vagasFiltradas;
	private JButton btnEnviarMensagem;

	public VagasFiltradasWindow(List<Vaga> vagasFiltradas) {
		this.vagasFiltradas = vagasFiltradas;
		InitComponents();
		popularTable();
	}
	
	public void popularTable() {
		DefaultTableModel modelo = (DefaultTableModel) table.getModel();
		
		modelo.fireTableDataChanged();
		
		modelo.setRowCount(0);
	
			for(Vaga vaga: vagasFiltradas) {
				modelo.addRow(new Object[] {
						vaga.getNome(),
						vaga.getFaixaSalarial(),
						vaga.getDescricao()
				});
			}
			
	}
	
	
	public void InitComponents() {
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
				"Nome", "Faixa salarial", "Descricao"
			}
		));
		scrollPane.setViewportView(table);
		
		JButton btnNewButton = new JButton("Voltar");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			dispose();
			}
		});
		btnNewButton.setBounds(30, 159, 171, 23);
		contentPane.add(btnNewButton);
		
		btnEnviarMensagem = new JButton("Enviar mensagem");
		btnEnviarMensagem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		btnEnviarMensagem.setBounds(231, 159, 171, 23);
		contentPane.add(btnEnviarMensagem);
	}
}
