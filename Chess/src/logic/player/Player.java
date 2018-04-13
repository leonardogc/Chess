package logic.player;

import java.util.LinkedList;

import logic.pieces.Bishop;
import logic.pieces.King;
import logic.pieces.Knight;
import logic.pieces.Pawn;
import logic.pieces.Piece;
import logic.pieces.Queen;
import logic.pieces.Rook;
import logic.util.GameUtil.PieceColor;

public class Player {
	private PieceColor color;
	
	private LinkedList<Queen> queen;
	private LinkedList<King> king;
	private LinkedList<Pawn> pawns;
	private LinkedList<Rook> rooks;
	private LinkedList<Knight> knights;
	private LinkedList<Bishop> bishops;
 	
	public Player(PieceColor color) {
		this.color = color;
		
		this.queen = new LinkedList<>();
		this.king = new LinkedList<>();
		this.pawns = new LinkedList<>();
		this.rooks = new LinkedList<>();
		this.knights = new LinkedList<>();
		this.bishops = new LinkedList<>();
	}

	public boolean lostPiece(Piece piece) {
		if(piece.getColor() == this.color) {
			if(piece instanceof King) {
				this.king.add((King) piece);
			}
			else if(piece instanceof Queen) {
				this.queen.add((Queen) piece);
			}
			else if(piece instanceof Pawn) {
				this.pawns.add((Pawn) piece);
			}
			else if(piece instanceof Bishop) {
				this.bishops.add((Bishop) piece);
			}
			else if(piece instanceof Knight) {
				this.knights.add((Knight) piece);
			}
			else if(piece instanceof Rook) {
				this.rooks.add((Rook) piece);
			}
			
			return true;
		}
		else {
			return false;
		}
	}
}
