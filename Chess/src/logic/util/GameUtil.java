package logic.util;

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
	
	//for each player
	public static final int KingNumber = 1;
	public static final int QueenNumber = 1;
	public static final int RookNumber = 2;
	public static final int BishopNumber = 2;
	public static final int KnightNumber = 2;
	public static final int PawnNumber = 8;
	
	public static final int boardSize = 8;
	
	public static String toString(int x, int y) {
		return (""+x)+(""+y);
	}
}
