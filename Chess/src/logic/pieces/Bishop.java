package logic.pieces;

import logic.game.Game;
import logic.util.GameUtil;
import logic.util.GameUtil.PieceColor;
import logic.util.GameUtil.PieceType;

public class Bishop extends Piece{

	public Bishop(PieceColor color) {
		super(PieceType.Bishop, color);
	}

	@Override
	public boolean move(int x, int y, int dest_x, int dest_y, Game game) {
		int dx = dest_x - x;
		int dy = dest_y - y;
		
		if(!(Math.abs(dx) == Math.abs(dy))) {
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
		return new Bishop(this.color);
	}
}
