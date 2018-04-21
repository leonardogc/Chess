package gui;

import io.Server;

public class ServerThread extends Thread{
	
	private int port;
	private ServerApplication sApp;

	public ServerThread(int port, ServerApplication sApp) {
		this.port=port;
		this.sApp=sApp;
		
		start();
	}
	
	@Override
	public void run() {
		sApp.s = new Server(port);
		sApp.s.startServer();
		
		sApp.textArea.setText("Players Connected Successfully!\nStarted Game");
		sApp.btnCloseServer.setEnabled(true);
	}
}
