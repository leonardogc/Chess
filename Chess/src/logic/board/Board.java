package logic.board;

import java.util.Hashtable;

import logic.pieces.Bishop;
import logic.pieces.King;
import logic.pieces.Knight;
import logic.pieces.Pawn;
import logic.pieces.Piece;
import logic.pieces.Queen;
import logic.pieces.Rook;
import logic.util.GameUtil;
import logic.util.GameUtil.PieceColor;

public class Board {
	private Hashtable<String, Piece> board;

	public Board() {
		board = new Hashtable<>();

		//add white pawns
		for(int x=1; x <= GameUtil.boardSize; x++) {
			board.put(GameUtil.toString(x, 2), new Pawn(PieceColor.White));
		}

		//add white rooks
		board.put(GameUtil.toString(1, 1), new Rook(PieceColor.White));
		board.put(GameUtil.toString(8, 1), new Rook(PieceColor.White));

		//add white knights
		board.put(GameUtil.toString(2, 1), new Knight(PieceColor.White));
		board.put(GameUtil.toString(7, 1), new Knight(PieceColor.White));

		//add white bishops
		board.put(GameUtil.toString(3, 1), new Bishop(PieceColor.White));
		board.put(GameUtil.toString(6, 1), new Bishop(PieceColor.White));

		//add white queen
		board.put(GameUtil.toString(4, 1), new Queen(PieceColor.White));
		
		//add white king
		board.put(GameUtil.toString(5, 1), new King(PieceColor.White));

		

		//add black pawns
		for(int x=1; x <= GameUtil.boardSize; x++) {
			board.put(GameUtil.toString(x, 7), new Pawn(PieceColor.Black));
		}

		//add black rooks
		board.put(GameUtil.toString(1, 8), new Rook(PieceColor.Black));
		board.put(GameUtil.toString(8, 8), new Rook(PieceColor.Black));

		//add black knights
		board.put(GameUtil.toString(2, 8), new Knight(PieceColor.Black));
		board.put(GameUtil.toString(7, 8), new Knight(PieceColor.Black));

		//add black bishops
		board.put(GameUtil.toString(3, 8), new Bishop(PieceColor.Black));
		board.put(GameUtil.toString(6, 8), new Bishop(PieceColor.Black));

		//add black queen
		board.put(GameUtil.toString(4, 8), new Queen(PieceColor.Black));
		
		//add black king
		board.put(GameUtil.toString(5, 8), new King(PieceColor.Black));

	}
	
	public Board(Hashtable<String, Piece> board) {
		this.board = board;
	}
	
	public Piece get(int x, int y) {
		return this.board.get(GameUtil.toString(x, y));
	}
}
