package gui;

import java.awt.EventQueue;

import io.Client;

public class ClientThread extends Thread{
	
	private String address;
	private ClientApplication cApp;

	public ClientThread(String address, ClientApplication cApp) {
		this.address=address;
		this.cApp=cApp;
		start();
	}
	
	@Override
	public void run() {
		Client c= new Client(address);
		
		if(c.startGame() == -1) {
			cApp.textArea.setText("An error occured trying to connect to the server :( try again later");
			cApp.btnJoinServer.setEnabled(true);
			return;
		}
		
		cApp.frame.setVisible(false);
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GraphicInterface window = new GraphicInterface(c);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
