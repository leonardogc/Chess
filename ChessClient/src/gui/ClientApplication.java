package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;


import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTextArea;

public class ClientApplication {

	public JFrame frame;
	private JTextField textField;
	public JTextArea textArea;
	public JButton btnJoinServer;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientApplication window = new ClientApplication();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ClientApplication() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		ClientApplication cApp=this;
		
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 287);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		textField = new JTextField();
		textField.setFont(new Font("Arial", Font.PLAIN, 14));
		textField.setBounds(147, 11, 249, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblPort = new JLabel("Server Address");
		lblPort.setFont(new Font("Arial", Font.PLAIN, 14));
		lblPort.setBounds(28, 11, 109, 20);
		frame.getContentPane().add(lblPort);
		
		btnJoinServer = new JButton("Join Server");
		btnJoinServer.setBounds(138, 65, 148, 23);
		frame.getContentPane().add(btnJoinServer);
		
		textArea = new JTextArea();
		textArea.setBounds(10, 99, 414, 138);
		textArea.setEditable(false);
		frame.getContentPane().add(textArea);
		
		btnJoinServer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new ClientThread(textField.getText(), cApp);
				textArea.setText("Waiting for the other player...");
				btnJoinServer.setEnabled(false);
			}
		});
		
	}
}
