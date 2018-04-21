package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;

import io.Server;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTextArea;

public class ServerApplication {

	private JFrame frame;
	private JTextField textField;
	public JTextArea textArea;
	public JButton btnCloseServer;
	public Server s;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerApplication window = new ServerApplication();
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
	public ServerApplication() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		ServerApplication sApp=this;
		
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 287);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		textField = new JTextField();
		textField.setFont(new Font("Arial", Font.PLAIN, 14));
		textField.setBounds(74, 11, 119, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblPort = new JLabel("Port");
		lblPort.setFont(new Font("Arial", Font.PLAIN, 14));
		lblPort.setBounds(28, 11, 42, 20);
		frame.getContentPane().add(lblPort);
		
		JButton btnCreateServer = new JButton("Create Server");
		btnCreateServer.setBounds(45, 65, 148, 23);
		frame.getContentPane().add(btnCreateServer);
		
		textArea = new JTextArea();
		textArea.setBounds(10, 99, 414, 138);
		textArea.setEditable(false);
		frame.getContentPane().add(textArea);
		
		btnCloseServer = new JButton("Close Server");
		btnCloseServer.setBounds(248, 65, 148, 23);
		btnCloseServer.setEnabled(false);
		frame.getContentPane().add(btnCloseServer);
		
		btnCloseServer.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				s.closeServer();
				btnCreateServer.setEnabled(true);
				btnCloseServer.setEnabled(false);
				textArea.setText("Server Closed!");
			}
		});
		
		

		btnCreateServer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new ServerThread(Integer.parseInt(textField.getText()), sApp);
				btnCreateServer.setEnabled(false);
				textArea.setText("Server Created!\nWaiting for players...");
			}
		});
		
	}
}
