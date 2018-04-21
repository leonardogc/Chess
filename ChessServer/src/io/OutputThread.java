package io;

import java.io.IOException;

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
	    		  sendBoard(); 
	    		  sendMessage = false;
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
								piece = "kw";
							}
							else {
								piece = "kb";
							}
							break;
						case Rook:
							if(s.game.getBoard()[x][y].getColor() == PieceColor.White) {
								piece = "rw";
							}
							else {
								piece = "rb";
							}
							break;
						case Queen:
							if(s.game.getBoard()[x][y].getColor() == PieceColor.White) {
								piece = "qw";
							}
							else {
								piece = "qb";
							}
							break;
						case Pawn:
							if(s.game.getBoard()[x][y].getColor() == PieceColor.White) {
								piece = "pw";
							}
							else {
								piece = "pb";
							}
							break;
						case Knight:
							if(s.game.getBoard()[x][y].getColor() == PieceColor.White) {
								piece = "knw";
							}
							else {
								piece = "knb";
							}
							break;
						case Bishop:
							if(s.game.getBoard()[x][y].getColor() == PieceColor.White) {
								piece = "bw";
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
