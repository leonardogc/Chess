package logic.ai;

import logic.game.Game;
import logic.util.GameUtil;
import logic.util.GameUtil.PieceColor;

public class Eval_v2 {
	public static final int king_score = 200000;
	public static final int queen_score = 9000;
	public static final int rook_score = 5000;
	public static final int knight_score = 3000;
	public static final int bishop_score = 3000;
	public static final int pawn_score = 1000;
	
	//for white pieces
	//use mirrored for black pieces
	public static final int[][] king_board_middle = new int[][] {{200, -200, -700, -900, -900, -900, -900, -900},
																	{300, -200, -700, -900, -900, -900, -900, -900},
																	{100, -500, -700, -900, -900, -900, -900, -900},
																	{-300, -500, -700, -900, -900, -900, -900, -900},
																	{300, -500, -700, -900, -900, -900, -900, -900},
																	{100, -500, -700, -900, -900, -900, -900, -900},
																	{300, -200, -700, -900, -900, -900, -900, -900},
																	{200, -200, -700, -900, -900, -900, -900, -900}};
																	
															
	public static final int[][] king_board_end = new int[][] {{0, 30, 50, 200, 200, 50, 30, 0},
																 {30, 50, 100, 300, 300, 100, 50, 30},
																 {50, 100, 200, 400, 400, 200, 100, 50},
																 {200, 300, 400, 500, 500, 400, 300, 200},
																 {200, 300, 400, 500, 500, 400, 300, 200},
																 {50, 100, 200, 400, 400, 200, 100, 50},
																 {30, 50, 100, 300, 300, 100, 50, 30},
																 {0, 30, 50, 200, 200, 50, 30, 0}};
																	
															
	public static final int[][] queen_board = new int[][] {{0, 0, 0, 0, 0, 0, 0, 0},
															  {0, 0, 0, 0, 0, 0, 0, 0},
															  {0, 0, 75, 75, 75, 75, 0, 0},
															  {0, 0, 75, 100, 100, 75, 0, 0},
															  {0, 0, 75, 100, 100, 75, 0, 0},
															  {0, 0, 75, 75, 75, 75, 0, 0},
															  {0, 0, 0, 0, 0, 0, 0, 0},
															  {0, 0, 0, 0, 0, 0, 0, 0}};
															  
														   
	public static final int[][] rook_board = new int[][] {{0, 0, 0, 0, 0, 0, 0, 0},
															 {0, 0, 0, 0, 0, 0, 0, 0},
														     {10, 10, 10, 10, 10, 10, 10, 10},
													    	 {15, 15, 15, 15, 15, 15, 15, 15},
														     {15, 15, 15, 15, 15, 15, 15, 15},
														     {10, 10, 10, 10, 10, 10, 10, 10},
														     {0, 0, 0, 0, 0, 0, 0, 0},
														     {0, 0, 0, 0, 0, 0, 0, 0}};
		  													 
														  
	public static final int[][] knight_board = new int[][] {{-120, -120, -120, -120, -120, -120, -120, -120},
															   {-120, 25, 25, 25, 25, 25, 25, -120},
															   {-120, 25, 50, 50, 50, 50, 25, -120},
															   {-120, 25, 50, 100, 100, 50, 25, -120},
															   {-120, 25, 50, 100, 100, 50, 25, -120},
															   {-120, 25, 50, 50, 50, 50, 25, -120},
															   {-120, 25, 25, 25, 25, 25, 25, -120},
															   {-120, -120, -120, -120, -120, -120, -120, -120}};
															   
															   
															
	public static final int[][] bishop_board = new int[][] {{-40, -40, -40, -40, -40, -40, -40, -40},
															   {-40, 20, 20, 20, 20, 20, 20, -40},
															   {-40, 20, 30, 30, 30, 30, 20, -40},
															   {-40, 20, 30, 45, 45, 30, 20, -40},
															   {-40, 20, 30, 45, 45, 30, 20, -40},
															   {-40, 20, 30, 30, 30, 30, 20, -40},
															   {-40, 20, 20, 20, 20, 20, 20, -40},
															   {-40, -40, -40, -40, -40, -40, -40, -40}};
															
	public static final int[][] pawn_board = new int[][] {{0, 0, 0, 0, 0, 0, 0, 0},
															 {0, 0, 0, 0, 0, 0, 0, 0},
															 {0, 0, 0, 15, 0, 0, 0, 0},
															 {0, -100, 40, 75, 75, 0, 0, 0},
															 {0, -100, 40, 75, 75, 0, 0, 0},
															 {0, 0, 0, 15, 0, 0, 0, 0},
															 {0, 0, 0, 0, 0, 0, 0, 0},
															 {0, 0, 0, 0, 0, 0, 0, 0}};
															 
	 public static int eval(Game game, PieceColor max) {
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
}
