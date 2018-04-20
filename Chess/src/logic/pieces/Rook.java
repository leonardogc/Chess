package logic.pieces;

import logic.game.Game;
import logic.util.GameUtil;
import logic.util.GameUtil.PieceColor;
import logic.util.GameUtil.PieceType;

public class Rook extends Piece{
	private boolean moved;

	public Rook(PieceColor color) {
		super(PieceType.Rook, color);
		this.moved = false;
	}

	public Rook(PieceColor color, boolean moved) {
		super(PieceType.Rook, color);
		this.moved = moved;
	}
	
	@Override
	public boolean isMoveValid(int x, int y, int dest_x, int dest_y, Game game) {
		if(!GameUtil.isPathValid(game.getBoard(), x, y, dest_x, dest_y)) {
			return false;
		}
		
		int dx = dest_x - x;
		int dy = dest_y - y;
		
		if(!(dx == 0 || dy == 0)) {
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
		
		this.moved = true;
		
		return true;
	}
	
	public boolean getMoved() {
		return this.moved;
	}
	
	public void setMoved(boolean moved) {
		this.moved = moved;
	}

	@Override
	public Piece makeCopy() {
		return new Rook(this.color, this.moved);
	}
}
