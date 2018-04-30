package io;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;

import logic.pieces.Bishop;
import logic.pieces.King;
import logic.pieces.Knight;
import logic.pieces.Pawn;
import logic.pieces.Queen;
import logic.pieces.Rook;
import logic.util.GameUtil;
import logic.util.GameUtil.PieceColor;
import logic.util.GameUtil.PieceType;

public class InputThread extends Thread {
	private Client c;
	public byte[] input_buffer;
	private int inputBufferSize = 65536;
	
	public InputThread(Client c) {
		this.c=c;
		input_buffer=new byte[inputBufferSize];
	}
	
	@Override
	public void run() {
		while(!c.stop) {
				try {
					c.server.getInputStream().read(input_buffer);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					c.closeGame();
        			return;
				}
				
				parseData();
		}

	}

	private void parseData() {
		String[] data = new String(input_buffer).split(";"); 
		System.out.println("Message: "+new String(input_buffer));
		if(data[0].equals("r")) {
			parseBoard(Arrays.copyOfRange(data, 1, 65));
			c.turn = 1;
			c.repaint = true;
		}
		else if(data[0].equals("c")) {
			parseBoard(Arrays.copyOfRange(data, 1, 65));
			c.turn = 0; 
			c.repaint = true;

			Scanner scanner = new Scanner(System.in);
			String piece = "";
			while(piece.equals("")) {
				System.out.println("Write:\n\tb for Bishop\n\tq for Queen\n\tk for Knight\n\tr for Rook");
				String piece_read = scanner.nextLine();

				switch(piece_read.toLowerCase()) {
				case "r":
					piece = "r";
					break;
				case "q":
					piece = "q";
					break;
				case "b":
					piece = "b";
					break;
				case "k":
					piece = "k";
					break;
				}
			}
			
			//scanner.close();

			try {
    			c.server.getOutputStream().write((piece+";").getBytes());
    		} catch (IOException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    			c.closeGame();
    		}
		}
		else {
			parseBoard(Arrays.copyOfRange(data, 0, 64));
			c.turn = 0;
			c.repaint = true;
		}
	}
	
	private void parseBoard(String[] data) {
		for(int y = 0; y < GameUtil.boardSize; y++) {
    		for(int x = 0; x < GameUtil.boardSize; x++) {
    			switch(data[y*GameUtil.boardSize+x]) {
    			case "-":
    				c.board[x][y] = null;
    				break;
    			default:
    				PieceColor color = null;
    				if(data[y*GameUtil.boardSize+x].charAt(0) == 'b') {
    					color = PieceColor.Black;
    				}
    				else {
    					color = PieceColor.White;
    				}
    				
    				String piece = data[y*GameUtil.boardSize+x].substring(1);
    				
    				switch(piece) {
    				case "k":
    					c.board[x][y] = new King(color);
    					break;
    				case "r":
    					c.board[x][y] = new Rook(color);
    					break;
    				case "q":
    					c.board[x][y] = new Queen(color);
    					break;
    				case "p":
    					c.board[x][y] = new Pawn(color);
    					break;
    				case "kn":
    					c.board[x][y] = new Knight(color);
    					break;
    				case "b":
    					c.board[x][y] = new Bishop(color);
    					break;
    				}
    				
    				break;
    			}
    		}
		}
		
	}
}
