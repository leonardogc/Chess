package logic.pieces;

import logic.game.Game;
import logic.util.GameUtil.PieceColor;
import logic.util.GameUtil.PieceType;

public class Rook extends Piece{

	public Rook(PieceColor color) {
		super(PieceType.Rook, color);
	}

	@Override
	public boolean move(int x, int y, int dest_x, int dest_y, Game game) {
		return false;
	}
}
