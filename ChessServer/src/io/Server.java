package io;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import logic.game.Game;

public class Server {
	public static void main(String[] args) {
		Server s=new Server(25565);
		s.startServer();
	}
	
	private ServerSocket ts=null;
	public Socket player1;
	public Socket player2;
	private int port;
	
	public Game game;
	public OutputThread outputThread;
	
	private InputThread inputThread_p1;
	private InputThread inputThread_p2;
	
	public Server(int port) {
		this.port=port;
	}
	
	public void startServer() {
		findPlayers();
		sendPlayer();
		
		game=new Game();
		
		outputThread= new OutputThread(this);
		outputThread.setRunning(true);
		
		inputThread_p1=new InputThread(player1, this);
		inputThread_p1.setRunning(true);
		
		inputThread_p2=new InputThread(player2, this);
		inputThread_p2.setRunning(true);
		
		inputThread_p1.start();
		inputThread_p2.start();
		outputThread.start();
	}
	
	private void sendPlayer() {
		try {
			player1.getOutputStream().write("white;".getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			player2.getOutputStream().write("black;".getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void closeServer(){
		outputThread.setRunning(false);
		inputThread_p1.setRunning(false);
		inputThread_p2.setRunning(false);

		try {
			player1.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			player2.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private void findPlayers() {
		try {
			ts = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			player1 = ts.accept();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			player1.setTcpNoDelay(true);
			player1.setSoTimeout(0);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Player 1 Connected");
		
		
		try {
			player2 = ts.accept();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			player2.setTcpNoDelay(true);
			player2.setSoTimeout(0);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Player 2 Connected");
		
		try {
			ts.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ts=null;
	}
}
