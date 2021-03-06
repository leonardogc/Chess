package logic.util;

import logic.pieces.Piece;

public class GameUtil {
	public enum PieceColor {
		White, Black;
		
		public PieceColor change() {
			if(this == PieceColor.White) {
				return PieceColor.Black;
			}
			else {
				return PieceColor.White;
			}
		}
	}
	
	public enum PieceType {
		King, Queen, Bishop, Rook, Knight, Pawn;
	}
	
	public static final int boardSize = 8;
	
	public static boolean isPathValid(Piece[][] board, int x, int y, int dest_x, int dest_y) {
		if(!validCoordinates(x, y) || !validCoordinates(dest_x, dest_y)) {
			return false;
		}
		
		if(board[x][y]==null) {
			return false;
		}
		
		if(board[dest_x][dest_y] != null) {
			if(board[x][y].getColor() == board[dest_x][dest_y].getColor()) {
				return false;
			}
		}

		int dx = dest_x - x;
		int dy = dest_y - y;
		
		if(dx == 0 && dy == 0) {
			return false;
		}
		
		
		int amount;

		if(dx == 0 || dy == 0) {
			amount = Math.abs(dx) + Math.abs(dy);
		}
		else if(Math.abs(dx) == Math.abs(dy)) {
			amount = Math.abs(dx);
		}
		else {
			return false;
		}
		
		int vx = dx/amount;
		int vy = dy/amount;
		
		for(int i = 1; i < amount; i++) {
			if(board[x+i*vx][y+i*vy] != null) {
				return false;
			}
		}

		return true;
	}
	
	//both coordinates are inclusive
	public static boolean isBoardFree(Piece[][] board, int x, int y, int dest_x, int dest_y) {
		if(!validCoordinates(x, y) || !validCoordinates(dest_x, dest_y)) {
			return false;
		}

		int dx = dest_x - x;
		int dy = dest_y - y;
		
		if(dx == 0 && dy == 0) {
			return false;
		}
		
		
		int amount;

		if(dx == 0 || dy == 0) {
			amount = Math.abs(dx) + Math.abs(dy);
		}
		else if(Math.abs(dx) == Math.abs(dy)) {
			amount = Math.abs(dx);
		}
		else {
			return false;
		}
		
		int vx = dx/amount;
		int vy = dy/amount;
		
		for(int i = 0; i <= amount; i++) {
			if(board[x+i*vx][y+i*vy] != null) {
				return false;
			}
		}

		return true;
	}
	
	public static boolean validCoordinates(int x, int y) {
		if(x < 0 || y < 0 || x > boardSize - 1 || y > boardSize - 1) {
			return false;
		}
		return true;
	}
}
