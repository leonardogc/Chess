package logic.player;

import logic.util.GameUtil.PieceColor;

public class Player {
	private PieceColor color;
	private boolean enPassant;
	private boolean castling;
 	
	public Player(PieceColor color) {
		this.color = color;
		this.enPassant = false;
		this.castling = true;
	}
	
	public Player(PieceColor color, boolean enPassant, boolean castling) {
		this.color = color;
		this.enPassant = enPassant;
		this.castling = castling;
	}
	
	public Player makeCopy() {
		return new Player(this.color, this.enPassant, this.castling);
	}
	
	public void setCastling(boolean status) {
		this.castling = status;
	}
	
	public void setEnPassant(boolean status) {
		this.enPassant = status;
	}
	
	public boolean getCastling() {
		return this.castling;
	}
	
	public boolean getEnPassant() {
		return this.enPassant;
	}
	
	public PieceColor getColor() {
		return this.color;
	}
}
