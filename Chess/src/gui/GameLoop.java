package gui;

import logic.ai.Minimax;
import logic.ai.Minimax.Turn;
import logic.game.Game.GameState;
import logic.game.Move;
import logic.util.GameUtil.PieceColor;
import logic.util.GameUtil.PieceType;
import logic.util.StopWatch;

import java.util.Scanner;

public class GameLoop extends Thread{
	private GraphicsAndListeners gui;
	
	public GameLoop(GraphicsAndListeners gui) {
		this.gui = gui;
	}

	@Override
	public void run() {
		Scanner scanner = new Scanner(System.in);

		while(gui.game.calculateMoves().size() > 0) {
			System.out.println("It's "+gui.game.getTurn()+"'s turn");
			
			if(gui.game.getTurn() == PieceColor.White) {
				StopWatch total = new StopWatch();
				StopWatch time = new StopWatch();
				
				total.start();
				Minimax.minimax_alpha_beta(gui.game, Turn.Max, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, time);
				total.stop();
				
				System.out.println(time.time*100/total.time + "%");
			}
			else {
				gui.queue.clear();
				
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

			}

			gui.repaint();
		}
		scanner.close();
		System.out.println(gui.game.getTurn().change()+" wins!");
	}
}
