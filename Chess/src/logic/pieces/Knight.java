package logic.pieces;

import logic.game.Game;
import logic.util.GameUtil.PieceColor;
import logic.util.GameUtil.PieceType;

public class Knight extends Piece{

	public Knight(PieceColor color) {
		super(PieceType.Knight, color);
	}

	@Override
	public boolean move(int x, int y, int dest_x, int dest_y, Game game) {
		return false;
	}
}
