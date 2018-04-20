package logic.pieces;

import logic.game.Game;
import logic.util.GameUtil;
import logic.util.GameUtil.PieceColor;
import logic.util.GameUtil.PieceType;

public class King extends Piece{
	private boolean moved;

	public King(PieceColor color) {
		super(PieceType.King, color);
		this.moved = false;
	}
	
	public King(PieceColor color, boolean moved) {
		super(PieceType.King, color);
		this.moved = moved;
	}

	@Override
	public boolean isMoveValid(int x, int y, int dest_x, int dest_y, Game game) {
		if(!GameUtil.isPathValid(game.getBoard(), x, y, dest_x, dest_y)) {
			return false;
		}
		
		int dx = dest_x - x;
		int dy = dest_y - y;
		int amount;

		if(dx == 0 || dy == 0) {
			amount = Math.abs(dx) + Math.abs(dy);
			
			if(amount != 1) {
				if(amount != 2) {
					return false;
				}
				
				if(this.moved) {
					return false;
				}
				
				
			}
		}
		else {
			amount = Math.abs(dx);
			
			if(amount != 1) {
				return false;
			}
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
	
	@Override
	public Piece makeCopy() {
		return new King(this.color, this.moved);
	}
}
