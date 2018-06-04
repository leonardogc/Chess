package logic.ai;

import logic.game.Game;
import logic.util.GameUtil;
import logic.util.GameUtil.PieceColor;

public class Eval_v5 {
	//combined 1, 2 and 3
	public static final int king_score = 200000;
	public static final int queen_score = 9750;
	public static final int rook_score = 5000;
	public static final int knight_score = 3250;
	public static final int bishop_score = 3350;
	public static final int pawn_score = 1000;
	
	public static final int bishop_pair = 300;
	public static final int knight_pair = 160;
	public static final int rook_pair = 80;
	
	public static final int[] knight_adj = new int[]{-200, -160, -120, -80, -40, 0, 40, 80, 120};
	public static final int[] rook_adj = new int[]{150, 120, 90, 60, 30, 0, -30, -60, -90};

	
	//for white pieces
	//use mirrored for black pieces
	public static final int[][] king_board_middle = new int[][] {{26, 10, -23, -36, -43, -46, -50, -53},
																	{36, 13, -23, -36, -43, -46, -50, -53},
																	{16, -10, -30, -43, -50, -53, -56, -60},
																	{-6, -16, -36, -53, -60, -63, -66, -70},
																	{13, -16, -36, -53, -60, -63, -66, -70},
																	{16, -10, -30, -43, -50, -53, -56, -60},
																	{36, 13, -23, -36, -43, -46, -50, -53},
																	{26, 10, -23, -36, -43, -46, -50, -53}};
																	
															
	public static final int[][] king_board_end = new int[][] {{-40, -25, -20, -11, -11, -20, -25, -40},
																{-25, -16, -4, 6, 6, -4, -13, -28},
																{-20, 0, 13, 27, 27, 13, -4, -20},
																{-11, 10, 27, 38, 38, 27, 10, -8},
																{-11, 10, 27, 38, 38, 27, 10, -8},
																{-20, 0, 13, 27, 27, 13, -4, -20},
																{-25, -16, -4, 6, 6, -4, -13, -28},
																{-40, -25, -20, -11, -11, -20, -25, -40}};
																
															
	public static final int[][] queen_board = new int[][] {{-8, -3, -3, 0, -1, -3, -3, -6},
															{-5, 0, 1, 0, 0, 0, 0, -3},
															{-5, 2, 4, 4, 4, 4, 0, -3},
															{-3, 0, 4, 6, 6, 4, 0, -1},
															{-3, 0, 4, 6, 6, 4, 0, -1},
															{-5, 0, 4, 4, 4, 4, 0, -3},
															{-5, 0, 0, 0, 0, 0, 0, -3},
															{-8, -3, -3, -1, -1, -3, -3, -6}};
																		  
														   
	public static final int[][] rook_board = new int[][] {{0, -3, -3, -3, -3, -3, 8, 1},
															{0, 0, 0, 0, 0, 0, 10, 1},
															{0, 0, 0, 0, 0, 0, 10, 2},
															{2, 0, 0, 0, 0, 0, 10, 2},
															{2, 0, 0, 0, 0, 0, 10, 2},
															{0, 0, 0, 0, 0, 0, 10, 2},
															{0, 0, 0, 0, 0, 0, 10, 1},
															{0, -3, -3, -3, -3, -3, 8, 1}};
		  													 
																			  
	public static final int[][] knight_board = new int[][] {{-23, -20, -16, -16, -16, -16, -20, -23},
															{-21, -6, 2, 0, 2, 0, -6, -20},
															{-16, 1, 6, 8, 8, 6, 0, -16},
															{-16, 3, 8, 12, 12, 8, 0, -16},
															{-16, 3, 8, 12, 12, 8, 0, -16},
															{-16, 1, 6, 8, 8, 6, 0, -16},
															{-21, -6, 2, 0, 2, 0, -6, -20},
															{-23, -20, -16, -16, -16, -16, -20, -23}};
															   
															   
																			
	public static final int[][] bishop_board = new int[][] {{-9, -6, -6, -6, -6, -6, -6, -9},
															{-6, 3, 4, 0, 2, 0, 0, -6},
															{-8, 1, 5, 5, 4, 3, 0, -6},
															{-6, 1, 5, 6, 6, 5, 0, -6},
															{-6, 1, 5, 6, 6, 5, 0, -6},
															{-8, 1, 5, 5, 4, 3, 0, -6},
															{-6, 3, 4, 0, 2, 0, 0, -6},
															{-9, -6, -6, -6, -6, -6, -6, -9}};
															
															
	public static final int[][] pawn_board = new int[][] {{0, 0, 0, -2, 0, 1, 14, 20},
															{0, 2, -3, -1, 0, 2, 15, 20},
															{0, 3, -3, 2, 4, 7, 17, 20},
															{0, -18, 3, 12, 13, 10, 17, 20},
															{0, -18, 3, 12, 13, 10, 17, 20},
															{0, 3, -3, 2, 4, 7, 17, 20},
															{0, 2, -3, -1, 0, 2, 15, 20},
															{0, 0, 0, -2, 0, 1, 14, 20}};	
														  
														  
	  public static int eval(Game game, PieceColor max) {
			int white_score = 0;
			int black_score = 0;
			
			boolean white_queen = false;
			boolean black_queen = false;
			
			int n_pawns_w = 0;
			int n_knights_w = 0;
			int n_bishops_w = 0;
			int n_rooks_w = 0;
			
			int n_pawns_b = 0;
			int n_knights_b = 0;
			int n_bishops_b = 0;
			int n_rooks_b = 0;
			
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
								n_rooks_w++;
							}
							else {
								black_score+=rook_score;
								black_score+=rook_board[x][GameUtil.boardSize-1-y];
								n_rooks_b++;
							}
							break;
						case Queen:
							if(game.getBoard()[x][y].getColor() == PieceColor.White) {
								white_score+=queen_score;
								white_score+=queen_board[x][y];
								white_queen = true;
							}
							else {
								black_score+=queen_score;
								black_score+=queen_board[x][GameUtil.boardSize-1-y];
								black_queen = true;
							}
							break;
						case Pawn:
							if(game.getBoard()[x][y].getColor() == PieceColor.White) {
								white_score+=pawn_score;
								white_score+=pawn_board[x][y];
								n_pawns_w++;
							}
							else {
								black_score+=pawn_score;
								black_score+=pawn_board[x][GameUtil.boardSize-1-y];
								n_pawns_b++;
							}
							break;
						case Knight:
							if(game.getBoard()[x][y].getColor() == PieceColor.White) {
								white_score+=knight_score;
								white_score+=knight_board[x][y];
								n_knights_w++;
							}
							else {
								black_score+=knight_score;
								black_score+=knight_board[x][GameUtil.boardSize-1-y];
								n_knights_b++;
							}
							break;
						case Bishop:
							if(game.getBoard()[x][y].getColor() == PieceColor.White) {
								white_score+=bishop_score;
								white_score+=bishop_board[x][y];
								n_bishops_w++;
							}
							else {
								black_score+=bishop_score;
								black_score+=bishop_board[x][GameUtil.boardSize-1-y];
								n_bishops_b++;
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
			else if(!white_queen && (black_queen && (n_bishops_b+n_knights_b) <= 1 && n_rooks_b == 0)) {
				endgame = true;
			}
			else if(!black_queen && (white_queen && (n_bishops_w+n_knights_w) <= 1 && n_rooks_w == 0)) {
				endgame = true;
			}
			else if((black_queen && (n_bishops_b+n_knights_b) <= 1 && n_rooks_b == 0) && (white_queen && (n_bishops_w+n_knights_w) <= 1 && n_rooks_w == 0)) {
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
	
			if(n_bishops_w > 1) {
				white_score += bishop_pair;
			}
	
			if(n_rooks_w > 1) {
				white_score += rook_pair;
			}
	
			if(n_knights_w > 1) {
				white_score += knight_pair;
			}
			
			
			if(n_bishops_b > 1) {
				black_score += bishop_pair;
			}
	
			if(n_rooks_b > 1) {
				black_score += rook_pair;
			}
	
			if(n_knights_b > 1) {
				black_score += knight_pair;
			}
			
			white_score += n_knights_w*knight_adj[n_pawns_w];
			white_score += n_rooks_w*rook_adj[n_pawns_w];
			
			black_score += n_knights_b*knight_adj[n_pawns_b];
			black_score += n_rooks_b*rook_adj[n_pawns_b];
	
			if(max == PieceColor.White) {
				return (white_score - black_score); 
			}
			else {
				return (black_score - white_score);
			}
		}
}
