package logic.pieces;

import logic.game.Game;
import logic.util.GameUtil;
import logic.util.GameUtil.PieceColor;
import logic.util.GameUtil.PieceType;

public class Queen extends Piece{

	public Queen(PieceColor color) {
		super(PieceType.Queen, color);
	}

	@Override
	public boolean isMoveValid(int x, int y, int dest_x, int dest_y, Game game) {
		if(!GameUtil.isPathValid(game.getBoard(), x, y, dest_x, dest_y)) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public boolean move(int x, int y, int dest_x, int dest_y, Game game) {
		if(!isMoveValid(x, y, dest_x, dest_y, game)){
			return false;
		}
		
		Piece[][] board = game.getBoard();

		board[dest_x][dest_y] = board[x][y];

		board[x][y] = null;
		
		return true;
	}
	
	@Override
	public Piece makeCopy() {
		return new Queen(this.color);
	}
}
