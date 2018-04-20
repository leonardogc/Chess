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
		// TODO Auto-generated method stub
		
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
