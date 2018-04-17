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
	public boolean move(int x, int y, int dest_x, int dest_y, Game game) {
		if(!GameUtil.isPathFree(game.getBoard(), x, y, dest_x, dest_y)) {
			return false;
		}

		if(game.getBoard()[x][y].getColor() != game.getTurn()) {
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
		return new Queen(this.color);
	}
}
