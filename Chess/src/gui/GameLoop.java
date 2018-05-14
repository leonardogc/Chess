package gui;

import logic.ai.Minimax;
import logic.ai.Minimax.Turn;
import logic.ai.MonteCarlo;
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

		while(!gui.game.gameEnded()) {
			System.out.println("It's "+gui.game.getTurn()+"'s turn");
			
			if(gui.game.getTurn() == PieceColor.White) {
				StopWatch total = new StopWatch();

				total.start();
				Minimax.minimax_alpha_beta(gui.game, Turn.Max, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
				total.stop();
				
				/*if(total.time < 0.6) {
					Minimax.max_depth++;
				}
				else if(total.time > 25 && Minimax.max_depth > 5){
					Minimax.max_depth--;
				}*/

				System.out.println(total.time + " s" + " depth: " + Minimax.max_depth);
			}
			/*else if(gui.game.getTurn() == PieceColor.Black) {
				MonteCarlo mt = new MonteCarlo(gui.game);
				gui.game.applyMove(mt.calculate(10));
			}*/
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
		
		if(gui.game.playerInCheck(gui.game.getTurn())) {
			System.out.println(gui.game.getTurn().change()+" wins!");
		}
		else {
			System.out.println("It's a Tie!");
		}
	}
}
