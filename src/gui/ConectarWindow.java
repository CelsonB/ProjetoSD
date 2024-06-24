package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import cliente.Cliente;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.awt.event.ActionEvent;

public class ConectarWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldIp;
	private JTextField textFieldPorta;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConectarWindow frame = new ConectarWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ConectarWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 318, 238);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblconectarIp = new JLabel("IP");
		lblconectarIp.setBounds(35, 75, 46, 14);
		contentPane.add(lblconectarIp);
		
		textFieldIp = new JTextField();
		textFieldIp.setText("Localhost");
		textFieldIp.setBounds(80, 72, 163, 20);
		contentPane.add(textFieldIp);
		textFieldIp.setColumns(10);
		
		textFieldPorta = new JTextField();
		textFieldPorta.setText("22222");
		textFieldPorta.setBounds(80, 117, 163, 20);
		contentPane.add(textFieldPorta);
		textFieldPorta.setColumns(10);
		
		JLabel lblPorta = new JLabel("Porta");
		lblPorta.setBounds(35, 120, 46, 14);
		contentPane.add(lblPorta);
		
		JToggleButton tglbtnNewToggleButton = new JToggleButton("Conectar");
		tglbtnNewToggleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int porta = Integer.parseInt(textFieldPorta.getText());
				
				try {
					
					String ip = textFieldIp.getText();
					Socket clienteSocket = new Socket(ip,porta);
					
					if(clienteSocket.isConnected()) {
						JOptionPane.showMessageDialog(null, "Conectado com sucesso", "Conectar", JOptionPane.WARNING_MESSAGE); 
						new LoginWindow(clienteSocket).setVisible(true);
						dispose();
					}else {
						JOptionPane.showMessageDialog(null, "erro ao realizar a conexão", "Conectar", JOptionPane.WARNING_MESSAGE);
					}
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, e1, "Conectar", JOptionPane.WARNING_MESSAGE);
				};
				
			}
		});
		tglbtnNewToggleButton.setBounds(93, 165, 121, 23);
		contentPane.add(tglbtnNewToggleButton);
	}
}
