package io;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.Server;
import logic.game.Game.GameState;
import logic.util.GameUtil;
import logic.util.GameUtil.PieceColor;


public class OutputThread extends Thread{
	
	 private boolean running;
	 public boolean sendMessage;
	 private Server s;

	public OutputThread(Server s){
		   running=false;
		   this.sendMessage = true;
	       this.s=s;
	}
	
	   public void setRunning(boolean running){
	       this.running=running;
	   }

	   
	    @Override
	    public void run() {
	       while(running) {
	    	   if(sendMessage) {
	    		   System.out.println("sending board");
	    		  sendBoard(); 
	    		  sendMessage = false;
	    	   }
	    	   try {
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	       }
	    }
	    

	    private void sendBoard() {
	    	String m="";
	    	String m1="";
	    	
	    	if(s.game.getState() == GameState.RegularMove) {
	    		m1+="r;";
	    	}
	    	else {
	    		m1+="c;";
	    	}

	    	for(int y = 0; y < GameUtil.boardSize; y++) {
	    		for(int x = 0; x < GameUtil.boardSize; x++) {
	    			if(s.game.getBoard()[x][y] != null) {
	    				String piece = "";
	    				switch(s.game.getBoard()[x][y].getType()) {
						case King:
							if(s.game.getBoard()[x][y].getColor() == PieceColor.White) {
								piece = "wk";
							}
							else {
								piece = "bk";
							}
							break;
						case Rook:
							if(s.game.getBoard()[x][y].getColor() == PieceColor.White) {
								piece = "wr";
							}
							else {
								piece = "br";
							}
							break;
						case Queen:
							if(s.game.getBoard()[x][y].getColor() == PieceColor.White) {
								piece = "wq";
							}
							else {
								piece = "bq";
							}
							break;
						case Pawn:
							if(s.game.getBoard()[x][y].getColor() == PieceColor.White) {
								piece = "wp";
							}
							else {
								piece = "bp";
							}
							break;
						case Knight:
							if(s.game.getBoard()[x][y].getColor() == PieceColor.White) {
								piece = "wkn";
							}
							else {
								piece = "bkn";
							}
							break;
						case Bishop:
							if(s.game.getBoard()[x][y].getColor() == PieceColor.White) {
								piece = "wb";
							}
							else {
								piece = "bb";
							}
							break;
						}
	    				
	    				piece += ";";
	    				
	    				m+=piece;
	    				m1+=piece;
	    			}
	    			else {
	    				m+="-;";
	    				m1+="-;";
	    			}
	    		}
	    	}
	    	
	    	try {
	    		if(s.game.getTurn() == PieceColor.White) {
	    			s.player1.getOutputStream().write(m1.getBytes());
	    			s.player2.getOutputStream().write(m.getBytes());
	    		}
	    		else {
	    			s.player1.getOutputStream().write(m.getBytes());
	    			s.player2.getOutputStream().write(m1.getBytes());
	    		}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				s.closeServer();
				return;
			}
	    }

}
