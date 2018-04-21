package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;

import io.Client;

public class GraphicInterface{

	public JFrame frame;
	public JPanel panel;

	
	public GraphicInterface(Client c) {
		initialize(c);
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(Client c) {
		frame = new JFrame();
		frame.setBounds(100, 100, 676, 699);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		panel = new GraphicsAndListeners(c);
		panel.setBounds(0, 0, 660, 660);
		frame.getContentPane().add(panel);
	}
}
