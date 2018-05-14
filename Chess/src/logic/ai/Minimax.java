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
	public static int max_depth = 5;
	
	public static final int king_score = 10;
	public static final int queen_score = 9;
	public static final int rook_score = 5;
	public static final int knight_score = 3;
	public static final int bishop_score = 3;
	public static final int pawn_score = 1;

	public static final double king_score_better = 20000;
	public static final double queen_score_better = 900;
	public static final double rook_score_better = 500;
	public static final double knight_score_better = 320;
	public static final double bishop_score_better = 330;
	public static final double pawn_score_better = 100;
	
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


	public static int minimax_alpha_beta(Game game, Turn turn, int depth, int alpha, int beta) {
		int max_score=Integer.MIN_VALUE;
		int min_score=Integer.MAX_VALUE;
		int result=0;

		Move best_move=null;

		//limit depth
		if(depth == max_depth) {
			
			if(game.gameEnded()) {
				return win_loss_score(turn);
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
		
		
		LinkedList<Move> moves = game.calculateMoves();

		//check if game ended
		if(moves.size() == 0) {
			return win_loss_score(turn);
		}

		//iterate through possible plays calling minimax each time
		while(moves.size() > 0) {
			/*if(depth == 0) {
				System.out.println("Size: " + moves.size());
			}*/
			Move move = moves.poll();
			Game game_copy = game.makeCopy();

			game_copy.applyMove(move);
			
			if(turn == Turn.Max) {
				if(game_copy.getState() == GameState.ChoosingPiece) {
					//max plays again
					result = minimax_alpha_beta(game_copy, Turn.Max, depth+1, alpha, beta);
				}
				else {
					result = minimax_alpha_beta(game_copy, Turn.Min, depth+1, alpha, beta);
				}

				if(result > alpha) {
					alpha = result;
				}

				if(result > max_score) {
					max_score = result;

					//save move
					if(depth==0) {
						best_move = move;
						if(max_score == win) {
							break;
						}
					}
				}
			}
			else if(turn == Turn.Min){
				if(game_copy.getState() == GameState.ChoosingPiece) {
					//min plays again
					result = minimax_alpha_beta(game_copy, Turn.Min, depth+1, alpha, beta);
				}
				else {
					result = minimax_alpha_beta(game_copy, Turn.Max, depth+1, alpha, beta);
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


		if(depth == 0) {
			if(best_move != null) {
				game.applyMove(best_move);
				best_move.print();
				return 0;
			}
		}
		
		if(turn == Turn.Max) {
			return max_score;
		}
		else {
			return min_score;
		}
	}

	private static int heuristic(Game game, PieceColor max) {
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
	
	private static int better_heuristic(Game game, PieceColor max) {
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
							white_score+=king_score_better;
							white_king_x = x;
							white_king_y = y;
						}
						else {
							black_score+=king_score_better;
							black_king_x = x;
							black_king_y = y;
						}
						break;
					case Rook:
						if(game.getBoard()[x][y].getColor() == PieceColor.White) {
							white_score+=rook_score_better;
							white_score+=rook_board[x][y];
							white_major_pieces++;
						}
						else {
							black_score+=rook_score_better;
							black_score+=rook_board[x][GameUtil.boardSize-1-y];
							black_major_pieces++;
						}
						break;
					case Queen:
						if(game.getBoard()[x][y].getColor() == PieceColor.White) {
							white_score+=queen_score_better;
							white_score+=queen_board[x][y];
							white_queen = true;
							white_major_pieces++;
						}
						else {
							black_score+=queen_score_better;
							black_score+=queen_board[x][GameUtil.boardSize-1-y];
							black_queen = true;
							black_major_pieces++;
						}
						break;
					case Pawn:
						if(game.getBoard()[x][y].getColor() == PieceColor.White) {
							white_score+=pawn_score_better;
							white_score+=pawn_board[x][y];
						}
						else {
							black_score+=pawn_score_better;
							black_score+=pawn_board[x][GameUtil.boardSize-1-y];
						}
						break;
					case Knight:
						if(game.getBoard()[x][y].getColor() == PieceColor.White) {
							white_score+=knight_score_better;
							white_score+=knight_board[x][y];
							white_minor_pieces++;
						}
						else {
							black_score+=knight_score_better;
							black_score+=knight_board[x][GameUtil.boardSize-1-y];
							black_minor_pieces++;
						}
						break;
					case Bishop:
						if(game.getBoard()[x][y].getColor() == PieceColor.White) {
							white_score+=bishop_score_better;
							white_score+=bishop_board[x][y];
							white_minor_pieces++;
						}
						else {
							black_score+=bishop_score_better;
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
	
	private static int win_loss_score(Turn turn) {
		if(turn == Turn.Max) {
			return loss;
		}
		else {
			return win;
		}
	}
}
