package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import logic.game.Game;
import logic.game.Move;
import logic.util.GameUtil;
import logic.util.GameUtil.PieceColor;

public class GraphicsAndListeners extends JPanel implements MouseListener, KeyListener {
	
	public Game game;
	public Game copy;
	private GameLoop thread;
	
	private final int square_size=75;
	private final int circle_diameter=20;
	private final int dx = (660-(square_size*GameUtil.boardSize))/2;
	private final int dy = (660-(square_size*GameUtil.boardSize))/2;
	private final PieceColor perspective = PieceColor.Black; 
	
	private int sel_x;
	private int sel_y;
	private int x;
	private int y;
	public boolean sel_square;
	public boolean move_piece;
	
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
	
	
	public ConcurrentLinkedQueue<Move> queue;
	
	public Move lastMove;
	
	public GraphicsAndListeners() {
		addMouseListener(this);
		addKeyListener(this);
		
		try {
			this.rook_b = ImageIO.read(GraphicsAndListeners.class.getResource("/gui/resources/rook_b.png"));
			this.bishop_b = ImageIO.read(GraphicsAndListeners.class.getResource("/gui/resources/bishop_b.png"));
			this.queen_b = ImageIO.read(GraphicsAndListeners.class.getResource("/gui/resources/queen_b.png"));
			this.pawn_b = ImageIO.read(GraphicsAndListeners.class.getResource("/gui/resources/pawn_b.png"));
			this.king_b = ImageIO.read(GraphicsAndListeners.class.getResource("/gui/resources/king_b.png"));
			this.knight_b = ImageIO.read(GraphicsAndListeners.class.getResource("/gui/resources/knight_b.png"));
			
			this.rook_w = ImageIO.read(GraphicsAndListeners.class.getResource("/gui/resources/rook_w.png"));
			this.bishop_w = ImageIO.read(GraphicsAndListeners.class.getResource("/gui/resources/bishop_w.png"));
			this.queen_w = ImageIO.read(GraphicsAndListeners.class.getResource("/gui/resources/queen_w.png"));
			this.pawn_w = ImageIO.read(GraphicsAndListeners.class.getResource("/gui/resources/pawn_w.png"));
			this.king_w = ImageIO.read(GraphicsAndListeners.class.getResource("/gui/resources/king_w.png"));
			this.knight_w = ImageIO.read(GraphicsAndListeners.class.getResource("/gui/resources/knight_w.png"));
		} 
		catch (IOException e) {
			System.out.println("Could not read images");
		}
		
		this.queue = new ConcurrentLinkedQueue<>();
		
		this.sel_x=-1;
		this.sel_y=-1;
		this.sel_square = false;
		this.move_piece = false;
		
		this.lastMove = null;
		
		this.game = new Game();
		this.copy = this.game.makeCopy();
		
		this.thread = new GameLoop(this);
		this.thread.start();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawBoard(g);
		drawPieces(g, copy);
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
		
		if(lastMove != null) {
			g.setColor(new Color(255, 255, 0, 140));
			
			if(perspective == PieceColor.White) {
				g.fillRect(dx+lastMove.x*square_size, dy+(GameUtil.boardSize - 1 - lastMove.y)*square_size, square_size, square_size);
				g.fillRect(dx+lastMove.dest_x*square_size, dy+(GameUtil.boardSize - 1 - lastMove.dest_y)*square_size, square_size, square_size);
			}
			else {
				g.fillRect(dx+(GameUtil.boardSize - 1 - lastMove.x)*square_size, dy+lastMove.y*square_size, square_size, square_size);
				g.fillRect(dx+(GameUtil.boardSize - 1 - lastMove.dest_x)*square_size, dy+lastMove.dest_y*square_size, square_size, square_size);
			}
		}
		
		if(sel_square && move_piece) {
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
	
	private void drawPieces(Graphics g, Game game) {
		for(int y=0; y < GameUtil.boardSize; y++) {
			for(int x=0; x < GameUtil.boardSize; x++) {
				if(game.getBoard()[x][y] != null) {
					int pos_x = x;
					int pos_y = (GameUtil.boardSize-1)-y;
					
					if(perspective == PieceColor.Black) {
						pos_x = (GameUtil.boardSize-1)-pos_x;
						pos_y = (GameUtil.boardSize-1)-pos_y;
					}
					
					pos_x*=square_size;
					pos_y*=square_size;
					
					pos_x+=dx;
					pos_y+=dy;
					
					switch(game.getBoard()[x][y].getType()) {
					case King:
						if(game.getBoard()[x][y].getColor() == PieceColor.White) {
							g.drawImage(king_w,pos_x,pos_y,square_size,square_size, null);
						}
						else {
							g.drawImage(king_b,pos_x,pos_y,square_size,square_size, null);
						}
						break;
					case Rook:
						if(game.getBoard()[x][y].getColor() == PieceColor.White) {
							g.drawImage(rook_w,pos_x,pos_y,square_size,square_size, null);
						}
						else {
							g.drawImage(rook_b,pos_x,pos_y,square_size,square_size, null);
						}
						break;
					case Queen:
						if(game.getBoard()[x][y].getColor() == PieceColor.White) {
							g.drawImage(queen_w,pos_x,pos_y,square_size,square_size, null);
						}
						else {
							g.drawImage(queen_b,pos_x,pos_y,square_size,square_size, null);
						}
						break;
					case Pawn:
						if(game.getBoard()[x][y].getColor() == PieceColor.White) {
							g.drawImage(pawn_w,pos_x,pos_y,square_size,square_size, null);
						}
						else {
							g.drawImage(pawn_b,pos_x,pos_y,square_size,square_size, null);
						}
						break;
					case Knight:
						if(game.getBoard()[x][y].getColor() == PieceColor.White) {
							g.drawImage(knight_w,pos_x,pos_y,square_size,square_size, null);
						}
						else {
							g.drawImage(knight_b,pos_x,pos_y,square_size,square_size, null);
						}
						break;
					case Bishop:
						if(game.getBoard()[x][y].getColor() == PieceColor.White) {
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
		
		if(sel_square && move_piece) {
			ArrayList<Move> moves = game.calculateMoves(false);
			
			for(int i = 0; i < moves.size(); i++) {
				Move move = moves.get(i);
				
				if(move.type == null) {
					int x = move.x;
					int y = move.y;		
							
					int dest_x = move.dest_x;
					int dest_y = move.dest_y;
					
					if(perspective == PieceColor.Black) {
						x = (GameUtil.boardSize-1)-x;
						y = (GameUtil.boardSize-1)-y;
						dest_x = (GameUtil.boardSize-1)-dest_x;
						dest_y = (GameUtil.boardSize-1)-dest_y;
					}
					
					if(x == this.x && y == this.y) {
						dest_y = (GameUtil.boardSize-1)-dest_y;
						
						g.setColor(Color.RED);
						g.fillOval(dx+dest_x*square_size+square_size/2-circle_diameter/2, dy+dest_y*square_size+square_size/2-circle_diameter/2, circle_diameter, circle_diameter);
					}
				
				}
			}
		}
	}

	private void saveGame() {
		try {
			FileOutputStream fileOut = new FileOutputStream("game.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(this.game);
			out.close();
			fileOut.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Game loadGame(String path) {
		Game game = null;
		
		try {
			FileInputStream fileIn = new FileInputStream(path);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			game = (Game)in.readObject();
			in.close();
			fileIn.close();
		} 
		catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return game;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		this.requestFocus();
		
		if(move_piece) {
			if(e.getX() >= dx + GameUtil.boardSize * square_size  || e.getX() <= dx) {
				return;
			}

			if(e.getY() >= dy + GameUtil.boardSize * square_size  || e.getY() <= dy) {
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
				repaint();
				
				if(perspective == PieceColor.White) {
					queue.add(new Move(x, y, x2, y2));
				}
				else {
					queue.add(new Move((GameUtil.boardSize-1)-x, (GameUtil.boardSize-1)-y, (GameUtil.boardSize-1)-x2, (GameUtil.boardSize-1)-y2));
				}
			}
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

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode() == KeyEvent.VK_S) {
			saveGame();
			System.out.println("\nSaved Game!\n");
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
