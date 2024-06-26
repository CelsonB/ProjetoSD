package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.json.JSONException;

import entities.Empresa;
import entities.Vaga;
import service.EmpresaService;
import service.VagasService;

import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;
import java.awt.event.ActionEvent;

public class VagasWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;


	
	public static Socket clienteSocket = null;
	public Empresa sessao; 
	private VagasService vagasService; 
	
	
	public VagasWindow(Empresa sessao, Socket clienteSocket) {
		
		this.clienteSocket = clienteSocket;
		this.vagasService = new VagasService(this.clienteSocket,sessao);
		this.sessao=sessao;
	
		initComponents();
		try {
			buscarVagas();
		} catch (JSONException | IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
	
	public void buscarVagas() throws JSONException, IOException {
		DefaultTableModel modelo = (DefaultTableModel) table.getModel();
		
		modelo.fireTableDataChanged();
		
		modelo.setRowCount(0);
		
		
		List<Vaga> vagaLista;
		
			vagaLista = this.vagasService.listarVagas();
			
			for(Vaga vaga : vagaLista) {
				
				modelo.addRow(new Object[] {
						
						vaga.getIdVaga(),	
						vaga.getNome(),
					
				});
			}
			
		
	}
	
	public void initComponents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 350, 339);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 315, 144);
		contentPane.add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
					"id","Nome"
			}
		));
		
		JButton CadastrarNovaVaga = new JButton("Cadastrar vaga");
		CadastrarNovaVaga.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cadastrarVaga();
			}
		});
		CadastrarNovaVaga.setBounds(10, 170, 155, 23);
		contentPane.add(CadastrarNovaVaga);
		
		JButton btnAtualizarVaga = new JButton("Atualizar vaga");
		btnAtualizarVaga.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			atualizarVaga();
			}
		});
		btnAtualizarVaga.setBounds(10, 204, 155, 23);
		contentPane.add(btnAtualizarVaga);
		
		JButton btnApagarVaga = new JButton("Apagar vaga");
		btnApagarVaga.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deletarVaga();
			}
		});
		btnApagarVaga.setBounds(175, 170, 155, 23);
		contentPane.add(btnApagarVaga);
		
		JButton btnVisualizarVaga = new JButton("Visualizar vaga");
		btnVisualizarVaga.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				visualizarVaga();
			}
		});
		btnVisualizarVaga.setBounds(175, 204, 155, 23);
		contentPane.add(btnVisualizarVaga);
		
		JButton btnVotlar = new JButton("Voltar");
		btnVotlar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new PerfilEmpresaWindow(sessao,clienteSocket).setVisible(true);
				dispose();
			}
		});
		btnVotlar.setBounds(10, 272, 155, 23);
		contentPane.add(btnVotlar);
		
		JButton btnMarcarVagaDisponivel = new JButton("Alterar estado vaga");
		btnMarcarVagaDisponivel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				alterarEstadoVaga();
			}
		});
		btnMarcarVagaDisponivel.setBounds(175, 238, 155, 23);
		contentPane.add(btnMarcarVagaDisponivel);
		
		JButton btnFiltrarCandidatos = new JButton("Filtrar candidatos");
		btnFiltrarCandidatos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new FiltrarCandidatosWindow(clienteSocket,sessao).setVisible(true);
				dispose();
			}
		});
		btnFiltrarCandidatos.setBounds(10, 238, 155, 23);
		contentPane.add(btnFiltrarCandidatos);
	}
	
	public void alterarEstadoVaga() {
		int idVaga = Integer.parseInt(table.getValueAt(table.getSelectedRow(), 0).toString());
		String nomeVaga = table.getValueAt(table.getSelectedRow(), 1).toString();
		Vaga vagaTemp = new Vaga();
		vagaTemp.setIdVaga(idVaga);
		vagaTemp.setNome(nomeVaga);
		
		
		Vaga vagaSelecionada = vagasService.visualizarVaga(vagaTemp.getIdVaga());
		vagaSelecionada.setIdVaga(idVaga);
		vagaSelecionada.setNome(nomeVaga);
		if(vagaSelecionada.getEstado().equals("Disponivel")) {
			vagaSelecionada.setEstado("Divulgavel");
		}else{
			vagaSelecionada.setEstado("Disponivel");
		}
	
		
		vagasService.atualizarVaga(vagaSelecionada, vagaSelecionada.getCompetencias());
		
	}
	public void atualizarVaga() {
		int idVaga = Integer.parseInt(table.getValueAt(table.getSelectedRow(), 0).toString());
		Vaga vagaTemp = new Vaga();
		vagaTemp.setIdVaga(idVaga);
		new CadastrarVagaWindow(sessao,clienteSocket,vagaTemp).setVisible(true);;
	}
	
	public void deletarVaga() {
		int idVaga = Integer.parseInt(table.getValueAt(table.getSelectedRow(), 0).toString());
		vagasService.deletarVaga(idVaga);
	}
	
	public void cadastrarVaga() {
		new CadastrarVagaWindow(sessao,clienteSocket).setVisible(true);
		dispose();
	}
	public void visualizarVaga() {
		int idVaga = Integer.parseInt(table.getValueAt(table.getSelectedRow(), 0).toString());
		Vaga visu = vagasService.visualizarVaga(idVaga);
		
	}
}
