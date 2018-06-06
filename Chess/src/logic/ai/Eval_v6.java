package logic.ai;

import logic.game.Game;
import logic.util.GameUtil;
import logic.util.GameUtil.PieceColor;

public class Eval_v6 {
	public static final int king_score = 200000;
	public static final int queen_score = 9750;
	public static final int rook_score = 5000;
	public static final int knight_score = 3250;
	public static final int bishop_score = 3350;
	public static final int pawn_score = 1000;
	
	public static final int bishop_pair = 300;
	
	public static final int center_control_bonus = 50; //per piece
	
	public static final int[] knight_adj = new int[]{-200, -160, -120, -80, -40, 0, 40, 80, 120};
	public static final int[] rook_adj = new int[]{150, 120, 90, 60, 30, 0, -30, -60, -90};

	
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
														  
														  
	  public static int eval(Game game, PieceColor max) {
			int white_score = 0;
			int black_score = 0;
			
			double white_avg = 0;
			double black_avg = 0;
			
			int white_queen = 0;
			int black_queen = 0;
			
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
								white_avg+=y;
							}
							else {
								black_score+=rook_score;
								black_score+=rook_board[x][GameUtil.boardSize-1-y];
								n_rooks_b++;
								black_avg+=y;
							}
							break;
						case Queen:
							if(game.getBoard()[x][y].getColor() == PieceColor.White) {
								white_score+=queen_score;
								white_score+=queen_board[x][y];
								white_queen++;
								white_avg+=y;
							}
							else {
								black_score+=queen_score;
								black_score+=queen_board[x][GameUtil.boardSize-1-y];
								black_queen++;
								black_avg+=y;
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
								white_avg+=y;
							}
							else {
								black_score+=knight_score;
								black_score+=knight_board[x][GameUtil.boardSize-1-y];
								n_knights_b++;
								black_avg+=y;
							}
							break;
						case Bishop:
							if(game.getBoard()[x][y].getColor() == PieceColor.White) {
								white_score+=bishop_score;
								white_score+=bishop_board[x][y];
								n_bishops_w++;
								white_avg+=y;
							}
							else {
								black_score+=bishop_score;
								black_score+=bishop_board[x][GameUtil.boardSize-1-y];
								n_bishops_b++;
								black_avg+=y;
							}
							break;
						}
					}
				}
			}
			
			boolean endgame = false;
			
			if(white_queen==0 && black_queen==0) {
				endgame = true;
			}
			else if(white_queen==0 && (black_queen==1 && (n_bishops_b+n_knights_b) <= 1 && n_rooks_b == 0)) {
				endgame = true;
			}
			else if(black_queen==0 && (white_queen==1 && (n_bishops_w+n_knights_w) <= 1 && n_rooks_w == 0)) {
				endgame = true;
			}
			else if((black_queen==1 && (n_bishops_b+n_knights_b) <= 1 && n_rooks_b == 0) && (white_queen==1 && (n_bishops_w+n_knights_w) <= 1 && n_rooks_w == 0)) {
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
			
			if(n_bishops_b > 1) {
				black_score += bishop_pair;
			}
			
			white_score += n_knights_w*knight_adj[n_pawns_w];
			white_score += n_rooks_w*rook_adj[n_pawns_w];
			
			black_score += n_knights_b*knight_adj[n_pawns_b];
			black_score += n_rooks_b*rook_adj[n_pawns_b];
			
			double tw = n_bishops_w+n_knights_w+n_rooks_w+white_queen;
			double tb = n_bishops_b+n_knights_b+n_rooks_b+black_queen;
			
			white_avg/=tw;
			black_avg/=tb;
			
			white_score+=tw*center_control_bonus*(1.0-(Math.abs(white_avg-3.5)/3.5));
			black_score+=tb*center_control_bonus*(1.0-(Math.abs(black_avg-3.5)/3.5));
	
			if(max == PieceColor.White) {
				return (white_score - black_score); 
			}
			else {
				return (black_score - white_score);
			}
		}
}
