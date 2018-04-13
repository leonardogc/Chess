package logic.pieces;

import logic.game.Game;
import logic.util.GameUtil.PieceColor;
import logic.util.GameUtil.PieceType;

public abstract class Piece {
	protected PieceType type;
	protected PieceColor color;
	
	public Piece(PieceType type, PieceColor color) {
		this.type = type;
	}
	
	public abstract boolean move(int x, int y, int dest_x, int dest_y, Game game);
	
	public PieceType getType() {
		return this.type;
	}
	
	public PieceColor getColor() {
		return this.color;
	}
}
