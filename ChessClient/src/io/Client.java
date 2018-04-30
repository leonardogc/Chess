package io;

import java.awt.EventQueue;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import gui.GraphicInterface;
import logic.pieces.Piece;
import logic.util.GameUtil;

public class Client {
	public static void main(String[] args) {
		Client c= new Client("94.60.13.255",25565);
		if(c.startGame()==-1) {
			System.out.println("An error occured trying to connect to the server :( try again later");
			return;
		}
		
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

	public Socket server;
	
	private String address; 
	private int port;
	
	public int turn;  //0 no, 1 yes, -1 unknown
	public boolean repaint;
	
	public boolean stop=false;
	
	public Piece[][] board;
	
	public Client(String address, int port) {
		this.address=address;
		this.port=port;
		
		board = new Piece[GameUtil.boardSize][GameUtil.boardSize];
		this.repaint = false;
	}
	
	public Client(String address) {
		String[] add_port=address.split(":");
		this.address=add_port[0];
		this.port=Integer.parseInt(add_port[1]);
		
		board = new Piece[GameUtil.boardSize][GameUtil.boardSize];
		this.repaint = false;
	}
	
	public void closeGame() {
		stop=true;
		try {
			server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int startGame() {
		if(connectToServer() == -1) {
			return -1;
		}
		
		initialize();
		
		InputThread it= new InputThread(this);
		
		it.start();
		
		return 0;
	}
	
	private void initialize() {
		byte[] buffer=new byte[1024]; 
		String[] s;
		
		try {
			server.getInputStream().read(buffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		s=new String(buffer).split(";");
		if(s[0].equals("white")) {
			this.turn = 1;
		}
		else {
			this.turn = 0;
		}
	}

	private int connectToServer() {
		try {
			server=new Socket(InetAddress.getByName(address),port);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		
		try {
			server.setTcpNoDelay(true);
			server.setSoTimeout(0);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		
		return 0;
	}
}
