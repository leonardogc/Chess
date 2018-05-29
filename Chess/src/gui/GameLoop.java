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

		while(!gui.game.noAvailableMoves() && !gui.game.tie()) {
			System.out.println("It's "+gui.game.getTurn()+"'s turn");
			
			if(gui.game.getTurn() == PieceColor.White) {
				StopWatch total = new StopWatch();
				
				System.out.println("Depth: " + Minimax.var_depth);

				total.start();
				Minimax.minimax_alpha_beta(gui.game, Turn.Max, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, null);
				total.stop();

				System.out.println(total.time + " s");
			}
			/*else if(gui.game.getTurn() == PieceColor.Black) {
				MonteCarlo mt = new MonteCarlo(gui.game);
				gui.game.applyMove(mt.calculate(10));
			}*/
			else {
				gui.queue.clear();
				
				if(gui.game.getState() == GameState.RegularMove) {
					gui.move_piece = true;
					gui.sel_square = false;
					
					Move move = null;
					while(move == null) {
						move = gui.queue.poll();
					}
					
					gui.move_piece = false;
					gui.sel_square = false;

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
			
			System.out.println("");
			
			gui.copy = gui.game.makeCopy();
			gui.repaint();
		}
		
		scanner.close();
		
		if(gui.game.tie()) {
			System.out.println("It's a Tie!");
			return;
		}
		
		if(gui.game.playerInCheck(gui.game.getTurn())) {
			System.out.println(gui.game.getTurn().change()+" wins!");
			return;
		}
		else {
			System.out.println("It's a Tie!");
			return;
		}
	}
}
