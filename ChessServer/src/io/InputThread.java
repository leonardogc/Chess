package io;

import java.io.IOException;
import java.net.Socket;

import logic.game.Move;
import logic.util.GameUtil.PieceType;

public class InputThread extends Thread {
	private boolean running;
	private Socket player;
	private Server s;
	private byte[] input_buffer;
	private int inputBufferSize = 65536;
	
	public InputThread(Socket player, Server s) {
		running=false;
		this.player=player;
		this.s=s;
		input_buffer=new byte[inputBufferSize];
	}
	
	public void setRunning(boolean running) {
		this.running=running;
	}
	
	@Override
	public void run() {
		while(running) {
				try {
					player.getInputStream().read(input_buffer);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					s.closeServer();
					return;
				}
				
				parseData();
		}
	}
	
	private void parseData() {
		String[] data = new String(input_buffer).split(";"); 
		System.out.println(new String(input_buffer));
		
		switch(data[0]) {
		case "q":
			if(s.game.promotePawn(PieceType.Queen)) {
				s.outputThread.sendMessage = true;
			}
			break;
		case "r":
			if(s.game.promotePawn(PieceType.Rook)) {
				s.outputThread.sendMessage = true;
			}
			break;
		case "k":
			if(s.game.promotePawn(PieceType.Knight)) {
				s.outputThread.sendMessage = true;
			}
			break;
		case "b":
			if(s.game.promotePawn(PieceType.Bishop)) {
				s.outputThread.sendMessage = true;
			}
			break;
		default:
			if(s.game.move(new Move(Integer.parseInt(data[0]),Integer.parseInt(data[1]),Integer.parseInt(data[2]),Integer.parseInt(data[3])))) {
				s.outputThread.sendMessage = true;
			}
			break;
		}
	}

}
