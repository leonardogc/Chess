package logic.ai;

import java.util.ArrayList;


import logic.game.Game;
import logic.game.Move;
import logic.util.GameUtil.PieceColor;
import logic.util.StopWatch;


public class Minimax {
	public static enum Turn {
		Max, Min
	}
	
	public static final int win = Integer.MAX_VALUE-1;
	public static final int loss = Integer.MIN_VALUE+1;
	public static final int depth = 6;
	public static int var_depth = depth;
	
	public static final double max_time = 20;
	public static final double inc_at = 0.8; //-1;

	
	public static Object minimax_alpha_beta(Game game, Turn turn, int curr_depth, int alpha, int beta, StopWatch t) {
		if(curr_depth == 0) {
			t = new StopWatch();
			t.start();
		}
		else {
			if(var_depth > depth) {
				if(t.lap() > max_time) {
					var_depth = depth;
				}
			}
		}

		if(game.tie()) {
			return 0;
		}

		//limit depth
		if(curr_depth >= var_depth) {
			if(game.noAvailableMoves()) {
				if(game.playerInCheck(game.getTurn())) {
					return win_loss_score(turn, curr_depth);
				}
				else {
					//stalemate
					return 0;
				}
			}
			
			PieceColor color;

			if(turn == Turn.Max) {
				color = game.getTurn();
			}
			else{
				color = game.getTurn().change();
			}
			
			return Eval_v6.eval(game, color);
		}
		
		int max_score=Integer.MIN_VALUE;
		int min_score=Integer.MAX_VALUE;
		int result=0;

		Move best_move=null;
		
		ArrayList<Move> moves = game.calculateMoves();

		//check if game ended
		if(moves.size() == 0) {
			if(game.playerInCheck(game.getTurn())) {
				return win_loss_score(turn, curr_depth);
			}
			else {
				//stalemate
				return 0;
			}
		}

		Game game_copy = null;
		
		//iterate through possible plays calling minimax each time
		while(moves.size() > 0) {
			/*if(curr_depth == 0) {
				System.out.println("Size: " + moves.size());
			}*/
			Move move = moves.remove(0);
			
			if(game_copy == null) {
				game_copy = game.makeCopy();
			}
			else {
				game_copy.copyFrom(game);
			}

			game_copy.applyMove(move);
			
			if(turn == Turn.Max) {
				if(game_copy.getTurn() == game.getTurn()) {
					//max plays again
					result = (int)minimax_alpha_beta(game_copy, Turn.Max, curr_depth+1, alpha, beta, t);
				}
				else {
					result = (int)minimax_alpha_beta(game_copy, Turn.Min, curr_depth+1, alpha, beta, t);
				}

				if(result > alpha) {
					alpha = result;
				}

				if(result > max_score) {
					max_score = result;

					//save move
					if(curr_depth==0) {
						best_move = move;
					}
				}
			}
			else if(turn == Turn.Min){
				if(game_copy.getTurn() == game.getTurn()) {
					//min plays again
					result = (int)minimax_alpha_beta(game_copy, Turn.Min, curr_depth+1, alpha, beta, t);
				}
				else {
					result = (int)minimax_alpha_beta(game_copy, Turn.Max, curr_depth+1, alpha, beta, t);
				}

				if(result < beta) {
					beta = result;
				}

				if(result < min_score) {
					min_score = result;
				}
			}

			if(beta <= alpha) {
				break;
			}
		}


		if(curr_depth == 0) {
			if(best_move != null) {
				if(t.lap() < inc_at) {
					var_depth++;
				}
				
				System.out.println("Score: " + max_score);
			}
			else {
				System.out.println("Something went very wrong on Minimax algorithm");
			}
			
			return best_move;
		}
		
		if(turn == Turn.Max) {
			return max_score;
		}
		else {
			return min_score;
		}
	}
	
	public static int win_loss_score(Turn turn, int node) {
		if(turn == Turn.Max) {
			return loss+node;
		}
		else {
			return win-node;
		}
	}
}
