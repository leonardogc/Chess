package logic.pieces;

import logic.game.Game;
import logic.util.GameUtil.PieceColor;
import logic.util.GameUtil.PieceType;

public class Queen extends Piece{

	public Queen(PieceColor color) {
		super(PieceType.Queen, color);
	}

	@Override
	public boolean move(int x, int y, int dest_x, int dest_y, Game game) {
		return false;
	}
	
	@Override
	public Piece makeCopy() {
		return new Queen(this.color);
	}
}
