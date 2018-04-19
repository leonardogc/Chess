package logic.player;

import logic.util.GameUtil.PieceColor;

public class Player {
	private PieceColor color;
	private boolean castling;
 	
	public Player(PieceColor color) {
		this.color = color;
		this.castling = true;
	}
	
	public Player(PieceColor color, boolean castling) {
		this.color = color;
		this.castling = castling;
	}
	
	public Player makeCopy() {
		return new Player(this.color, this.castling);
	}
	
	public void setCastling(boolean status) {
		this.castling = status;
	}
	
	public boolean getCastling() {
		return this.castling;
	}
	
	public PieceColor getColor() {
		return this.color;
	}
}
