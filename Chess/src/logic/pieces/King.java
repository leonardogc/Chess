package logic.pieces;

import logic.game.Game;
import logic.util.GameUtil.PieceColor;
import logic.util.GameUtil.PieceType;

public class King extends Piece{

	public King(PieceColor color) {
		super(PieceType.King, color);
	}

	@Override
	public boolean move(int x, int y, int dest_x, int dest_y, Game game) {
		return false;
	}
}
