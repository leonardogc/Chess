package logic.player;

import java.util.LinkedList;

import logic.pieces.Piece;
import logic.util.GameUtil.PieceColor;
import logic.util.GameUtil.PieceType;

public class Player {
	private PieceColor color;
	
	private int queen;
	private int king;
	private int pawns;
	private int rooks;
	private int knights;
	private int bishops;
 	
	public Player(PieceColor color) {
		this.color = color;
		
		this.queen = 0;
		this.king = 0;
		this.pawns = 0;
		this.rooks = 0;
		this.knights = 0;
		this.bishops = 0;
	}
	
	public Player(PieceColor color, int queen, int king, int pawns, int rooks, int knights, int bishops) {
		this.color = color;
		
		this.queen = queen;
		this.king = king;
		this.pawns = pawns;
		this.rooks = rooks;
		this.knights = knights;
		this.bishops = bishops;
	}

	public boolean lostPiece(Piece piece) {
		if(piece.getColor() == this.color) {
			if(piece.getType() == PieceType.King) {
				this.king++;
			}
			else if(piece.getType() == PieceType.Queen) {
				this.queen++;
			}
			else if(piece.getType() == PieceType.Pawn) {
				this.pawns++;
			}
			else if(piece.getType() == PieceType.Bishop) {
				this.bishops++;
			}
			else if(piece.getType() == PieceType.Knight) {
				this.knights++;
			}
			else if(piece.getType() == PieceType.Rook) {
				this.rooks++;
			}
			
			return true;
		}
		else {
			return false;
		}
	}
	
	public Player makeCopy() {
		return new Player(this.color, this.queen, this.king, this.pawns, this.rooks, this.knights, this.bishops);
	}
}
