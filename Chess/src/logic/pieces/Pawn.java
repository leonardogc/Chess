package logic.pieces;

import logic.game.Game;
import logic.util.GameUtil.PieceColor;
import logic.util.GameUtil.PieceType;

public class Pawn extends Piece{

	public Pawn(PieceColor color) {
		super(PieceType.Pawn, color);
	}

	@Override
	public boolean move(int x, int y, int dest_x, int dest_y, Game game) {
		return false;
	}
	
	@Override
	public Piece makeCopy() {
		return new Pawn(this.color);
	}
}
