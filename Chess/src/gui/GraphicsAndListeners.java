package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import logic.game.Game;
import logic.util.GameUtil;

public class GraphicsAndListeners extends JPanel implements MouseListener{
	
	public Game game;
	private GameLoop thread;
	
	private final int square_size=75;
	private final int dx = (660-(square_size*GameUtil.boardSize))/2;
	private final int dy = (660-(square_size*GameUtil.boardSize))/2;
	
	public GraphicsAndListeners() {
		addMouseListener(this);
		
		this.game = new Game();
		this.thread = new GameLoop(this);
		this.thread.start();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawBoard(g);
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
		
		g.setColor(Color.BLACK);
		for(int i = 0; i < GameUtil.boardSize + 1; i++) {
			//vertical
			g.drawLine(i*square_size+dx, dy, i*square_size+dx, dy+GameUtil.boardSize*square_size);
			
			//horizontal
			g.drawLine(dx, i*square_size+dy, dx+GameUtil.boardSize*square_size, i*square_size+dy);
		}
	}
	
	private void drawPieces(Graphics g) {
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getX() > dx + GameUtil.boardSize * square_size  || e.getX() < dx) {
			return;
		}
		
		if(e.getY() > dy + GameUtil.boardSize * square_size  || e.getY() < dy) {
			return;
		}
		
		int x = (e.getX() - dx)/square_size;
		int y = (GameUtil.boardSize - 1) - (e.getY() - dy)/square_size;
		
		System.out.println("Clicked on: " + "(" + x + ", " + y + ")");
		
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
