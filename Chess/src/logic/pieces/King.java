package logic.pieces;

import logic.game.Game;
import logic.util.GameUtil;
import logic.util.GameUtil.PieceColor;
import logic.util.GameUtil.PieceType;

public class King extends Piece{

	public King(PieceColor color) {
		super(PieceType.King, color);
	}

	@Override
	public boolean move(int x, int y, int dest_x, int dest_y, Game game) {
		if(!GameUtil.isPathFree(game.getBoard(), x, y, dest_x, dest_y)) {
			return false;
		}
		
		int dx = dest_x - x;
		int dy = dest_y - y;
		int amount;

		if(dx == 0 || dy == 0) {
			amount = Math.abs(dx) + Math.abs(dy);
		}
		else {
			amount = Math.abs(dx);
		}
		
		if(amount != 1) {
			//TODO special move
			return false;
		}
		
		Piece[][] board = game.getBoard();
		
		board[dest_x][dest_y] = board[x][y];
		
		board[x][y] = null;
		
		return true;
	}
	
	@Override
	public Piece makeCopy() {
		return new King(this.color);
	}
}
