package logic.ai;

import logic.game.Game;
import logic.util.GameUtil;
import logic.util.GameUtil.PieceColor;

public class Eval_v3 {
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
	public static final int[][] king_board_middle = new int[][] {{40, 30, 10, 0, -10, -20, -30, -40},
																 {50, 40, 20, 10, 0, -10, -20, -30},
																 {30, 20, 0, -10, -20, -30, -40, -50},
																 {10, 0, -20, -30, -40, -50, -60, -70},
																 {10, 0, -20, -30, -40, -50, -60, -70},
																 {30, 20, 0, -10, -20, -30, -40, -50},
																 {50, 40, 20, 10, 0, -10, -20, -30},
																 {40, 30, 10, 0, -10, -20, -30, -40}};
																	
															
	public static final int[][] king_board_end = new int[][] {{-72, -48, -36, -24, -24, -36, -48, -72},
															  {-48, -24, -12, 0, 0, -12, -24, -48},
															  {-36, -12, 0, 12, 12, 0, -12, -36},
															  {-24, 0, 12, 24, 24, 12, 0, -24},
															  {-24, 0, 12, 24, 24, 12, 0, -24},
															  {-36, -12, 0, 12, 12, 0, -12, -36},
															  {-48, -24, -12, 0, 0, -12, -24, -48},
															  {-72, -48, -36, -24, -24, -36, -48, -72}};
																
															
	public static final int[][] queen_board = new int[][] {{-5, 0, 0, 0, 0, 0, 0, 0},
														   {-5, 0, 0, 0, 0, 0, 0, 0},
														   {-5, 1, 1, 2, 2, 1, 1, 0},
														   {-5, 1, 2, 3, 3, 2, 1, 0},
														   {-5, 1, 2, 3, 3, 2, 1, 0},
														   {-5, 1, 1, 2, 2, 1, 1, 0},
														   {-5, 0, 0, 0, 0, 0, 0, 0},
														   {-5, 0, 0, 0, 0, 0, 0, 0}};
															  
														   
	public static final int[][] rook_board = new int[][] {{0, -5, -5, -5, -5, -5, 20, 5},
														  {0, 0, 0, 0, 0, 0, 20, 5},
														  {0, 0, 0, 0, 0, 0, 20, 5},
														  {2, 0, 0, 0, 0, 0, 20, 5},
														  {2, 0, 0, 0, 0, 0, 20, 5},
														  {0, 0, 0, 0, 0, 0, 20, 5},
														  {0, 0, 0, 0, 0, 0, 20, 5},
														  {0, -5, -5, -5, -5, -5, 20, 5}};
		  													 
														  
	public static final int[][] knight_board = new int[][] {{-8, -8, -8, -8, -8, -8, -8, -8},
														    {-12, 0, 0, 0, 0, 0, 0, -8},
														    {-8, 1, 4, 4, 4, 4, 0, -8},
														    {-8, 2, 4, 8, 8, 4, 0, -8},
														    {-8, 2, 4, 8, 8, 4, 0, -8},
														    {-8, 1, 4, 4, 4, 4, 0, -8},
														    {-12, 0, 0, 0, 0, 0, 0, -8},
														    {-8, -8, -8, -8, -8, -8, -8, -8}};
															   
															   
															
	public static final int[][] bishop_board = new int[][] {{-4, -4, -4, -4, -4, -4, -4, -4},
														    {-4, 2, 1, 0, 0, 0, 0, -4},
														    {-12, 1, 2, 4, 4, 2, 0, -4},
														    {-4, 1, 4, 6, 6, 4, 0, -4},
														    {-4, 1, 4, 6, 6, 4, 0, -4},
														    {-12, 1, 2, 4, 4, 2, 0, -4},
														    {-4, 2, 1, 0, 0, 0, 0, -4},
														    {-4, -4, -4, -4, -4, -4, -4, -4}};
															
	public static final int[][] pawn_board = new int[][] {{0, -6, -4, -6, -6, -6, -6, 0},
														  {0, -4, -4, -4, -4, -4, -4, 0},
													      {0, 1, 1, 5, 2, 1, 1, 0},
													 	  {0, -24, 5, 10, 8, 2, 1, 0},
														  {0, -24, 5, 10, 8, 2, 1, 0},
														  {0, 1, 1, 5, 2, 1, 1, 0},
														  {0, -4, -4, -4, -4, -4, -4, 0},
														  {0, -6, -4, -6, -6, -6, -6, 0}};	
														  
														  
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
