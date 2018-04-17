package logic.pieces;

import logic.game.Game;
import logic.util.GameUtil;
import logic.util.GameUtil.PieceColor;
import logic.util.GameUtil.PieceType;

public class Rook extends Piece{

	public Rook(PieceColor color) {
		super(PieceType.Rook, color);
	}

	@Override
	public boolean move(int x, int y, int dest_x, int dest_y, Game game) {
		int dx = dest_x - x;
		int dy = dest_y - y;
		
		if(!(dx == 0 || dy == 0)) {
			return false;
		}
		
		Piece[][] board = game.getBoard();
		
		board[dest_x][dest_y] = board[x][y];
		
		board[x][y] = null;
		
		game.setTurn(game.getTurn().change());
		
		return true;
	}

	@Override
	public Piece makeCopy() {
		return new Rook(this.color);
	}
}
