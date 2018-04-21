package gui;

import logic.game.Game.GameState;
import logic.util.GameUtil.PieceType;

import java.util.Scanner;

import logic.game.Move;

public class GameLoop extends Thread{
	private GraphicsAndListeners gui;
	
	public GameLoop(GraphicsAndListeners gui) {
		this.gui = gui;
	}
	
	@Override
	public void run() {
		Scanner scanner = new Scanner(System.in);
		
		while(gui.game.calculateMoves().size() > 0) {
			gui.queue.clear();

			System.out.println("It's "+gui.game.getTurn()+"'s turn");

			if(gui.game.getState() == GameState.RegularMove) {
				gui.move_piece = true;
				Move move = null;
				while(move == null) {
					move = gui.queue.poll();
				}
				gui.move_piece = false;

				gui.game.move(move);
			}
			else {
				System.out.println("Write:\n\tb for Bishop\n\tq for Queen\n\tk for Knight\n\tr for Rook");
				String piece = scanner.nextLine();
				
				switch(piece.toLowerCase()) {
				case "r":
					gui.game.promotePawn(PieceType.Rook);
					break;
				case "q":
					gui.game.promotePawn(PieceType.Queen);
					break;
				case "b":
					gui.game.promotePawn(PieceType.Bishop);
					break;
				case "k":
					gui.game.promotePawn(PieceType.Knight);
					break;
				default:
					System.out.println("Invalid Character");
					break;
				}
			}
			
			gui.repaint();
		}
		scanner.close();
		System.out.println(gui.game.getTurn().change()+" wins!");
	}
}
