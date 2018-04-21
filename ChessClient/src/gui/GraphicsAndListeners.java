package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import io.Client;
import logic.game.Game;
import logic.game.Move;
import logic.pieces.Piece;
import logic.util.GameUtil;
import logic.util.GameUtil.PieceColor;

public class GraphicsAndListeners extends JPanel implements MouseListener{
	public class Loop extends Thread{
		private Client c;
		private GraphicsAndListeners g;
		
		public Loop(Client c, GraphicsAndListeners g) {
			this.c = c;
			this.g = g;
		}
		@Override
		public void run() {
			while(true) {
				if(c.repaint) {
					g.repaint();
					g.repaint();
					c.repaint = false;
				}
			}
		}
	}
	
	
	private final int square_size=75;
	private final int dx = (660-(square_size*GameUtil.boardSize))/2;
	private final int dy = (660-(square_size*GameUtil.boardSize))/2;
	
	private int sel_x;
	private int sel_y;
	private int x;
	private int y;
	private boolean sel_square;
	
	private BufferedImage rook_b;
	private BufferedImage bishop_b;
	private BufferedImage queen_b;
	private BufferedImage pawn_b;
	private BufferedImage king_b;
	private BufferedImage knight_b;
	
	private BufferedImage rook_w;
	private BufferedImage bishop_w;
	private BufferedImage queen_w;
	private BufferedImage pawn_w;
	private BufferedImage king_w;
	private BufferedImage knight_w;
	
	private Client client;
	
	public GraphicsAndListeners(Client c) {
		addMouseListener(this);
		
		this.client = c;
		
		try {
			this.rook_b = ImageIO.read(new File("resources/rook_b.png"));
			this.bishop_b = ImageIO.read(new File("resources/bishop_b.png"));
			this.queen_b = ImageIO.read(new File("resources/queen_b.png"));
			this.pawn_b = ImageIO.read(new File("resources/pawn_b.png"));
			this.king_b = ImageIO.read(new File("resources/king_b.png"));
			this.knight_b = ImageIO.read(new File("resources/knight_b.png"));
			
			this.rook_w = ImageIO.read(new File("resources/rook_w.png"));
			this.bishop_w = ImageIO.read(new File("resources/bishop_w.png"));
			this.queen_w = ImageIO.read(new File("resources/queen_w.png"));
			this.pawn_w = ImageIO.read(new File("resources/pawn_w.png"));
			this.king_w = ImageIO.read(new File("resources/king_w.png"));
			this.knight_w = ImageIO.read(new File("resources/knight_w.png"));
		} 
		catch (IOException e) {
			System.out.println("Could not read images");
		}
		
		this.sel_x=-1;
		this.sel_y=-1;
		this.sel_square = false;
		
		new Loop(client, this).start();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawBoard(g);
		drawPieces(g, client.board);
	}
	
	private void drawBoard(Graphics g) {
		for(int y = 0; y < GameUtil.boardSize; y++) {
			for(int x = 0; x < GameUtil.boardSize; x++) {
				if((x % 2 == 0 && y % 2 == 0) || (x % 2 != 0 && y % 2 != 0)) {
					g.setColor(new Color(245,222,179));
				}
				else {
					g.setColor(new Color(205,133,63));
				}
				
				g.fillRect(dx+x*square_size, dy+y*square_size, square_size, square_size);
			}
		}
		
		if(sel_square) {
			g.setColor(Color.GREEN);
			g.fillRect(dx+sel_x*square_size, dy+sel_y*square_size, square_size, square_size);
		}
		
		g.setColor(Color.BLACK);
		for(int i = 0; i < GameUtil.boardSize + 1; i++) {
			//vertical
			g.drawLine(i*square_size+dx, dy, i*square_size+dx, dy+GameUtil.boardSize*square_size);
			
			//horizontal
			g.drawLine(dx, i*square_size+dy, dx+GameUtil.boardSize*square_size, i*square_size+dy);
		}
	}
	
	private void drawPieces(Graphics g, Piece[][] board) {
		for(int y=0; y < GameUtil.boardSize; y++) {
			for(int x=0; x < GameUtil.boardSize; x++) {
				if(board[x][y] != null) {
					int pos_x = dx + x*square_size;
					int pos_y = dy + (GameUtil.boardSize-1-y)*square_size;
					
					switch(board[x][y].getType()) {
					case King:
						if(board[x][y].getColor() == PieceColor.White) {
							g.drawImage(king_w,pos_x,pos_y,square_size,square_size, null);
						}
						else {
							g.drawImage(king_b,pos_x,pos_y,square_size,square_size, null);
						}
						break;
					case Rook:
						if(board[x][y].getColor() == PieceColor.White) {
							g.drawImage(rook_w,pos_x,pos_y,square_size,square_size, null);
						}
						else {
							g.drawImage(rook_b,pos_x,pos_y,square_size,square_size, null);
						}
						break;
					case Queen:
						if(board[x][y].getColor() == PieceColor.White) {
							g.drawImage(queen_w,pos_x,pos_y,square_size,square_size, null);
						}
						else {
							g.drawImage(queen_b,pos_x,pos_y,square_size,square_size, null);
						}
						break;
					case Pawn:
						if(board[x][y].getColor() == PieceColor.White) {
							g.drawImage(pawn_w,pos_x,pos_y,square_size,square_size, null);
						}
						else {
							g.drawImage(pawn_b,pos_x,pos_y,square_size,square_size, null);
						}
						break;
					case Knight:
						if(board[x][y].getColor() == PieceColor.White) {
							g.drawImage(knight_w,pos_x,pos_y,square_size,square_size, null);
						}
						else {
							g.drawImage(knight_b,pos_x,pos_y,square_size,square_size, null);
						}
						break;
					case Bishop:
						if(board[x][y].getColor() == PieceColor.White) {
							g.drawImage(bishop_w,pos_x,pos_y,square_size,square_size, null);
						}
						else {
							g.drawImage(bishop_b,pos_x,pos_y,square_size,square_size, null);
						}
						break;
					}
				}
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
	
		if(client.turn == 1) {
			if(e.getX() > dx + GameUtil.boardSize * square_size  || e.getX() < dx) {
				return;
			}

			if(e.getY() > dy + GameUtil.boardSize * square_size  || e.getY() < dy) {
				return;
			}
			
			if(!sel_square) {
				sel_x = (e.getX() - dx)/square_size;
				sel_y = (e.getY() - dy)/square_size;
				
				x = (e.getX() - dx)/square_size;
				y = (GameUtil.boardSize - 1) - (e.getY() - dy)/square_size;
				sel_square = true;
				repaint();
			}
			else {
				int x2 = (e.getX() - dx)/square_size;
				int y2 = (GameUtil.boardSize - 1) - (e.getY() - dy)/square_size;
				
				sel_square = false;
				
				try {
        			client.server.getOutputStream().write((x+";"+y+";"+x2+";"+y2+";").getBytes());
        			System.out.println("Sent!");
        		} catch (IOException e1) {
        			// TODO Auto-generated catch block
        			e1.printStackTrace();
        			client.closeGame();
        		}
			}
		}
		else {
			sel_square = false;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
