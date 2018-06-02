package logic.ai;

import java.util.LinkedList;


import logic.game.Game;
import logic.game.Move;
import logic.game.Game.GameState;
import logic.util.GameUtil;
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
	
	

	public static final double king_score = 20000;
	public static final double queen_score = 900;
	public static final double rook_score = 500;
	public static final double knight_score = 320;
	public static final double bishop_score = 330;
	public static final double pawn_score = 100;
	
	//for white pieces
	//use mirrored for black pieces
	public static final int[][] king_board_middle = new int[][] {{20,20,-10,-20,-30,-30,-30,-30},
																 {30,20,-20,-30,-40,-40,-40,-40},
																 {10,0,-20,-30,-40,-40,-40,-40},
																 {0,0,-20,-40,-50,-50,-50,-50},
																 {0,0,-20,-40,-50,-50,-50,-50},
																 {10,0,-20,-30,-40,-40,-40,-40},
																 {30,20,-20,-30,-40,-40,-40,-40},
																 {20,20,-10,-20,-30,-30,-30,-30}};
															
	public static final int[][] king_board_end = new int[][] {{-50,-30,-30,-30,-30,-30,-30,-50},
															  {-30,-30,-10,-10,-10,-10,-20,-40},
															  {-30,0,20,30,30,20,-10,-30},
															  {-30,0,30,40,40,30,0,-20},
															  {-30,0,30,40,40,30,0,-20},
															  {-30,0,20,30,30,20,-10,-30},
															  {-30,-30,-10,-10,-10,-10,-20,-40},
															  {-50,-30,-30,-30,-30,-30,-30,-50}};
															
	public static final int[][] queen_board = new int[][] {{-20,-10,-10,0,-5,-10,-10,-20},
														   {-10,0,5,0,0,0,0,-10},
														   {-10,5,5,5,5,5,0,-10},
														   {-5,0,5,5,5,5,0,-5},
														   {-5,0,5,5,5,5,0,-5},
														   {-10,0,5,5,5,5,0,-10},
														   {-10,0,0,0,0,0,0,-10},
														   {-20,-10,-10,-5,-5,-10,-10,-20}};
														   
	public static final int[][] rook_board = new int[][] {{0,-5,-5,-5,-5,-5,5,0},
														  {0,0,0,0,0,0,10,0},
														  {0,0,0,0,0,0,10,0},
														  {5,0,0,0,0,0,10,0},
														  {5,0,0,0,0,0,10,0},
														  {0,0,0,0,0,0,10,0},
														  {0,0,0,0,0,0,10,0},
														  {0,-5,-5,-5,-5,-5,5,0}};
														  
	public static final int[][] knight_board = new int[][] {{-50,-40,-30,-30,-30,-30,-40,-50},
															{-40,-20,5,0,5,0,-20,-40},
															{-30,0,10,15,15,10,0,-30},
															{-30,5,15,20,20,15,0,-30},
															{-30,5,15,20,20,15,0,-30},
															{-30,0,10,15,15,10,0,-30},
															{-40,-20,5,0,5,0,-20,-40},
															{-50,-40,-30,-30,-30,-30,-40,-50}};
															
	public static final int[][] bishop_board = new int[][] {{-20,-10,-10,-10,-10,-10,-10,-20},
															{-10,5,10,0,5,0,0,-10},
															{-10,0,10,10,5,5,0,-10},
															{-10,0,10,10,10,10,0,-10},
															{-10,0,10,10,10,10,0,-10},
															{-10,0,10,10,5,5,0,-10},
															{-10,5,10,0,5,0,0,-10},
															{-20,-10,-10,-10,-10,-10,-10,-20}};
															
	public static final int[][] pawn_board = new int[][] {{0,5,5,0,5,10,50,60},
														  {0,10,-5,0,5,10,50,60},
														  {0,10,-10,0,10,20,50,60},
														  {0,-20,0,20,25,30,50,60},
														  {0,-20,0,20,25,30,50,60},
														  {0,10,-10,0,10,20,50,60},
														  {0,10,-5,0,5,10,50,60},
														  {0,5,5,0,5,10,50,60}};

	
	public static int minimax_alpha_beta(Game game, Turn turn, int curr_depth, int alpha, int beta, StopWatch t) {
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
			
			return better_heuristic(game, color);
		}
		
		int max_score=Integer.MIN_VALUE;
		int min_score=Integer.MAX_VALUE;
		int result=0;

		Move best_move=null;
		
		LinkedList<Move> moves = game.calculateMoves();

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
			Move move = moves.poll();
			
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
					result = minimax_alpha_beta(game_copy, Turn.Max, curr_depth+1, alpha, beta, t);
				}
				else {
					result = minimax_alpha_beta(game_copy, Turn.Min, curr_depth+1, alpha, beta, t);
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
					result = minimax_alpha_beta(game_copy, Turn.Min, curr_depth+1, alpha, beta, t);
				}
				else {
					result = minimax_alpha_beta(game_copy, Turn.Max, curr_depth+1, alpha, beta, t);
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
				
				game.applyMove(best_move);
				best_move.print();
				System.out.println("Score: " + max_score);
				return 0;
			}
			else {
				System.out.println("Something went very wrong on Minimax algorithm");
				return -1;
			}
		}
		
		if(turn == Turn.Max) {
			return max_score;
		}
		else {
			return min_score;
		}
	}

	public static int heuristic(Game game, PieceColor max) {
		int white_score = 0;
		int black_score = 0;
		
		for(int y=0; y < GameUtil.boardSize; y++) {
			for(int x=0; x < GameUtil.boardSize; x++) {
				if(game.getBoard()[x][y]!=null) {
					switch(game.getBoard()[x][y].getType()) {
					case King:
						if(game.getBoard()[x][y].getColor() == PieceColor.White) {
							white_score+=king_score;
						}
						else {
							black_score+=king_score;
						}
						break;
					case Rook:
						if(game.getBoard()[x][y].getColor() == PieceColor.White) {
							white_score+=rook_score;
						}
						else {
							black_score+=rook_score;
						}
						break;
					case Queen:
						if(game.getBoard()[x][y].getColor() == PieceColor.White) {
							white_score+=queen_score;
						}
						else {
							black_score+=queen_score;
						}
						break;
					case Pawn:
						if(game.getBoard()[x][y].getColor() == PieceColor.White) {
							white_score+=pawn_score;
						}
						else {
							black_score+=pawn_score;
						}
						break;
					case Knight:
						if(game.getBoard()[x][y].getColor() == PieceColor.White) {
							white_score+=knight_score;
						}
						else {
							black_score+=knight_score;
						}
						break;
					case Bishop:
						if(game.getBoard()[x][y].getColor() == PieceColor.White) {
							white_score+=bishop_score;
						}
						else {
							black_score+=bishop_score;
						}
						break;
					}
				}
			}
		}
		
		if(max == PieceColor.White) {
			return white_score - black_score; 
		}
		else {
			return black_score - white_score;
		}
		
	}
	
	public static int better_heuristic(Game game, PieceColor max) {
		int white_score = 0;
		int black_score = 0;
		
		boolean white_queen = false;
		boolean black_queen = false;
		
		int white_minor_pieces = 0;
		int black_minor_pieces = 0;
		
		int white_major_pieces = 0;
		int black_major_pieces = 0;
		
		int black_king_x = -1;
		int black_king_y = -1;
		
		int white_king_x = -1;
		int white_king_y = -1;
		
		for(int y=0; y < GameUtil.boardSize; y++) {
			for(int x=0; x < GameUtil.boardSize; x++) {
				if(game.getBoard()[x][y]!=null) {
					switch(game.getBoard()[x][y].getType()) {
					case King:
						if(game.getBoard()[x][y].getColor() == PieceColor.White) {
							white_score+=king_score;
							white_king_x = x;
							white_king_y = y;
						}
						else {
							black_score+=king_score;
							black_king_x = x;
							black_king_y = y;
						}
						break;
					case Rook:
						if(game.getBoard()[x][y].getColor() == PieceColor.White) {
							white_score+=rook_score;
							white_score+=rook_board[x][y];
							white_major_pieces++;
						}
						else {
							black_score+=rook_score;
							black_score+=rook_board[x][GameUtil.boardSize-1-y];
							black_major_pieces++;
						}
						break;
					case Queen:
						if(game.getBoard()[x][y].getColor() == PieceColor.White) {
							white_score+=queen_score;
							white_score+=queen_board[x][y];
							white_queen = true;
							white_major_pieces++;
						}
						else {
							black_score+=queen_score;
							black_score+=queen_board[x][GameUtil.boardSize-1-y];
							black_queen = true;
							black_major_pieces++;
						}
						break;
					case Pawn:
						if(game.getBoard()[x][y].getColor() == PieceColor.White) {
							white_score+=pawn_score;
							white_score+=pawn_board[x][y];
						}
						else {
							black_score+=pawn_score;
							black_score+=pawn_board[x][GameUtil.boardSize-1-y];
						}
						break;
					case Knight:
						if(game.getBoard()[x][y].getColor() == PieceColor.White) {
							white_score+=knight_score;
							white_score+=knight_board[x][y];
							white_minor_pieces++;
						}
						else {
							black_score+=knight_score;
							black_score+=knight_board[x][GameUtil.boardSize-1-y];
							black_minor_pieces++;
						}
						break;
					case Bishop:
						if(game.getBoard()[x][y].getColor() == PieceColor.White) {
							white_score+=bishop_score;
							white_score+=bishop_board[x][y];
							white_minor_pieces++;
						}
						else {
							black_score+=bishop_score;
							black_score+=bishop_board[x][GameUtil.boardSize-1-y];
							black_minor_pieces++;
						}
						break;
					}
				}
			}
		}
		
		boolean endgame = false;
		
		if(!white_queen && !black_queen) {
			endgame = true;
		}
		else if(!white_queen && (black_queen && black_minor_pieces <= 1 && black_major_pieces == 1)) {
			endgame = true;
		}
		else if(!black_queen && (white_queen && white_minor_pieces <= 1 && white_major_pieces == 1)) {
			endgame = true;
		}
		else if((black_queen && black_minor_pieces <= 1 && black_major_pieces == 1) && (white_queen && white_minor_pieces <= 1 && white_major_pieces == 1)) {
			endgame = true;
		}
		
		if(endgame) {
			white_score+=king_board_end[white_king_x][white_king_y];
			black_score+=king_board_end[black_king_x][GameUtil.boardSize-1-black_king_y];
		}
		else {
			white_score+=king_board_middle[white_king_x][white_king_y];
			black_score+=king_board_middle[black_king_x][GameUtil.boardSize-1-black_king_y];
		}
		
		if(max == PieceColor.White) {
			return (white_score - black_score); 
		}
		else {
			return (black_score - white_score);
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
